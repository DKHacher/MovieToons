package Words.DAL.db;

import Words.BE.Movie;
import Words.DAL.IMovieDataAccess;
import Words.BE.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO_DB implements IMovieDataAccess {

    private DatabaseConnector databaseConnector;

    public MovieDAO_DB() throws Exception {
        databaseConnector = new DatabaseConnector();
    }

    @Override
    public List<Movie> getAllMovies() throws Exception{
        ArrayList<Movie> allMovies = new ArrayList<>();

        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement())
        {
            String sql =
                    """
                    SELECT Movie.id, Movie.name, Movie.ratingIMDB, Movie.ratingPersonal, Movie.fileLink, Movie.lastView
                    FROM Movie
                    """;

            ResultSet rs = stmt.executeQuery(sql);
            // Loop through rows from the database result set

            while (rs.next()) {
                int id = rs.getInt("id");
                String movieTitle = rs.getString("name");
                int ratingIMDB = rs.getInt("ratingIMDB");
                int ratingPersonal = rs.getInt("ratingPersonal");
                String fileLink = rs.getString("fileLink");
                Timestamp lastView = (Timestamp) rs.getObject("lastView");

                Movie movie = new Movie(id, movieTitle, ratingIMDB, ratingPersonal, fileLink, lastView, getAllCategoriesInMovie(id, conn));
                allMovies.add(movie);
            }}
        return allMovies;
    }

    private ArrayList<Category> getAllCategoriesInMovie(int id, Connection conn) throws Exception {
        ArrayList<Category> allCategories = new ArrayList<>();

        try (
             Statement stmt2 = conn.createStatement()) {
            String sql2 = "SELECT * FROM dbo.CatMovie WHERE movieId = "+id;
            ResultSet rs2 = stmt2.executeQuery(sql2);

            while (rs2.next()) {
                int Cid = rs2.getInt("categoryId");
                String categoryToImport = "SELECT * FROM dbo.Category WHERE id = "+Cid+";";
                Statement stmt3 = conn.createStatement();
                ResultSet rs3 = stmt3.executeQuery(categoryToImport);
                while (rs3.next()){
                    int CatId = rs3.getInt("id");
                    String CatName = rs3.getString("name");

                    Category category = new Category(CatId, CatName);
                    allCategories.add(category);
                }
            }
            return allCategories;
        } catch (SQLException e) {
            throw new Exception("Error connecting to the database");
        }
    }

    @Override
    public Movie createMovie(Movie movie) throws Exception {
        try (Connection conn = databaseConnector.getConnection()) {
            // Check for missing indexes
            int newMovieId = findMissingMovieIndex(conn);

            // SQL command
            String sql = "INSERT INTO Movie (id, name, ratingIMDB, ratingPersonal, fileLink, lastView) VALUES (?, ?, ?, ?, ?, ?);";

            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                // Bind parameters
                stmt.setInt(1, newMovieId);
                stmt.setString(2, movie.getMovieTitle());
                stmt.setInt(3, movie.getRatingIMDB());
                stmt.setInt(4, movie.getRatingPersonal());
                stmt.setString(5, movie.getFilePath());

                // Check if lastView is null before setting it
                if (movie.getLastView() != null) {
                    stmt.setTimestamp(6, movie.getLastView());
                } else {
                    stmt.setNull(6, Types.TIMESTAMP);
                }

                // Run the specified SQL statement
                stmt.executeUpdate();

                // Get the generated ID from the DB
                ResultSet rs = stmt.getGeneratedKeys();

                int id = 0;
                if (rs.next()) {
                    id = rs.getInt(1);
                }
                Movie resultMovie = new Movie(newMovieId, movie.getMovieTitle(), movie.getRatingIMDB(), movie.getRatingPersonal(), movie.getFilePath(), movie.getLastView(), movie.getCategories());
                if (!resultMovie.getCategories().isEmpty()){
                    updateMovieCategories(resultMovie);
                }
                return resultMovie;
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new Exception("Could not insert movie.", ex);
            }
        }
    }


    private int findMissingMovieIndex(Connection conn) throws SQLException {
        // SQL command to find missing index
        String findMissingIndexSql = "SELECT MIN(t1.id + 1) AS missing_index " +
                "FROM Movie t1 " +
                "LEFT JOIN Movie t2 ON t1.id + 1 = t2.id " +
                "WHERE t2.id IS NULL";

        try (PreparedStatement stmt = conn.prepareStatement(findMissingIndexSql)) {
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int missingIndex = rs.getInt("missing_index");
                return missingIndex;
            } else {
                // No missing index, get the next available index
                String getMaxIndexSql = "SELECT MAX(id) + 1 AS next_index FROM Movie";
                try (PreparedStatement getMaxIndexStmt = conn.prepareStatement(getMaxIndexSql)) {
                    ResultSet maxIndexRs = getMaxIndexStmt.executeQuery();

                    if (maxIndexRs.next()) {
                        int nextIndex = maxIndexRs.getInt("next_index");
                        return nextIndex;
                    }
                }
            }
        }

        // Default to -1 if something goes wrong
        return -1;
    }


    @Override
    public void updateMovie(Movie movie) throws Exception {
        // SQL command
        String sql = "UPDATE Movie SET name = ?, ratingIMDB = ?, ratingPersonal = ?, fileLink = ?, lastView = ? WHERE id = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Bind parameters
            stmt.setString(1, movie.getMovieTitle());
            stmt.setInt(2, movie.getRatingIMDB());
            stmt.setInt(3, movie.getRatingPersonal());
            stmt.setString(4, movie.getFilePath());
            stmt.setTimestamp(5, movie.getLastView());
            stmt.setInt(6, movie.getId());
            updateMovieCategories(movie);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not update movie.", ex);
        }
    }

    @Override
    public void deleteMovie(Movie movie) throws Exception {
        // SQL command to delete a movie by its ID
        String sql = "DELETE FROM Movie WHERE id = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Bind the ID parameter
            stmt.setInt(1, movie.getId());

            // Execute the delete SQL statement
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not delete movie.", ex);
        }
    }
    private void updateMovieCategories(Movie movie) throws Exception {
        // SQL command
        String sqlDeletePrevious = "DELETE FROM dbo.CatMovie WHERE MovieId = ?;";
        String sqlCreateNew = "INSERT INTO dbo.CatMovie VALUES ";
        for (Category category:movie.getCategories()) {
            String toAdd ="\n" + "(" + movie.getId() + ", " + category.getId() + "),";
            sqlCreateNew += toAdd;
        }
        sqlCreateNew = sqlCreateNew.substring(0,sqlCreateNew.length() - 1);
        sqlCreateNew += ";";
        try (
                Connection conn = databaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sqlDeletePrevious);
                PreparedStatement stmt2 = conn.prepareStatement(sqlCreateNew)
        ) {
            // Bind parameters
            stmt.setInt(1, movie.getId());

            // Run the specified SQL statement
            stmt.executeUpdate();
            stmt2.executeUpdate();
        }
        catch (SQLException e) {
            throw new Exception("Could not update categories associated with movie", e);
        }

    }


    @Override
    public List<Movie> getMoviesByCategory(Category category) throws Exception {
        ArrayList<Movie> moviesByCategory = new ArrayList<>();

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT Movie.id, Movie.name, Movie.ratingIMDB, Movie.ratingPersonal, Movie.fileLink, Movie.lastView " +
                             "FROM Movie " +
                             "JOIN CatMovie ON Movie.id = CatMovie.movieId " +
                             "WHERE CatMovie.categoryId = ?")) {

            stmt.setInt(1, category.getId());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String movieTitle = rs.getString("name");
                int ratingIMDB = rs.getInt("ratingIMDB");
                int ratingPersonal = rs.getInt("ratingPersonal");
                String fileLink = rs.getString("fileLink");
                Timestamp lastView = (Timestamp) rs.getObject("lastView");

                Movie movie = new Movie(id, movieTitle, ratingIMDB, ratingPersonal, fileLink, lastView, getAllCategoriesInMovie(id, conn));
                moviesByCategory.add(movie);
            }
        }

        return moviesByCategory;
    }

}

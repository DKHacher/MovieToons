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
        // SQL command
        String sql = "INSERT INTO Movie (name, ratingIMDB, ratingPersonal, fileLink, lastView) VALUES (?, ?, ?, ?, ?);";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Bind parameters
            stmt.setString(1, movie.getMovieTitle());
            stmt.setString(2, movie.getRatingIMDB());
            stmt.setString(3, movie.getRatingPersonal());
            stmt.setString(4, movie.getFilePath());
            stmt.setObject(5, movie.getLastView());

            // Run the specified SQL statement
            stmt.executeUpdate();

            // Get the generated ID from the DB
            ResultSet rs = stmt.getGeneratedKeys();

            int id = 0;
            if (rs.next()) {
                id = rs.getInt(1);
            }

            //Movie createdMovie = new Movie(id, movie.getMovieTitle(), movie.getIMDB(), movie.getPersonal(), movie.getFilePath(), movie.getLastView());
            Movie createdMovie = new Movie(0, null, 0, 0, null, null, null);

            return createdMovie;
        } catch (SQLException ex) {
            // create entry in log file
            ex.printStackTrace();
            throw new Exception("Could not insert movie.", ex);
        }
    }

    @Override
    public void updateMovie(Movie movie) throws Exception {
        // SQL command
        String sql = "UPDATE Movies SET MovieTitle = ? WHERE id = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Bind parameters
            stmt.setString(1, movie.getMovieTitle());
            stmt.setInt(2, movie.getId());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not update movie.", ex);
        }
    }

    @Override
    public void deleteMovie(Movie movie) throws Exception {
        // SQL command to delete a movie by its ID
        String sql = "DELETE FROM Movies WHERE id = ?;";

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

}

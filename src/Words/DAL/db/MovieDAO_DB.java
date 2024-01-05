package Words.DAL.db;

import Words.BE.Movie;
import Words.DAL.IMovieDataAccess;

import java.sql.*;
import java.time.LocalDateTime;
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
                    SELECT Movies.id, Movies.name, Movies.ratingIMDB, Movies.ratingPersonal, fileLink, lastView
                    FROM Movies
                    """;

            ResultSet rs = stmt.executeQuery(sql);
            // Loop through rows from the database result set

            while (rs.next()) {
                int id = rs.getInt("id");
                String movieTitle = rs.getString("name");
                int ratingIMDB = rs.getInt("ratingIMDB");
                int ratingPersonal = rs.getInt("ratingPersonal");
                String fileLink = rs.getString("fileLink");
                LocalDateTime lastView = (LocalDateTime) rs.getObject("lastView");

                Movie movie = new Movie(id, movieTitle, ratingIMDB, ratingPersonal, fileLink, lastView);
                allMovies.add(movie);
            }}
        return allMovies;
    }

    @Override
    public Movie createMovie(Movie movie) throws Exception {
        // SQL command
        String sql = "INSERT INTO Movies (name, ratingIMDB, ratingPersonal, fileLink, lastView) VALUES (?, ?, ?, ?);";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Bind parameters
            stmt.setString(1, movie.getMovieTitle());
            stmt.setLong(2, movie.getIMDB());
            stmt.setLong(3, movie.getPersonal());
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

            Movie createdMovie = new Movie(id, movie.getMovieTitle(), movie.getIMDB(), movie.getPersonal(), movie.getFilePath(), movie.getLastView());

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

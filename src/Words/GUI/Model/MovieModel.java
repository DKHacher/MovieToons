package Words.GUI.Model;

import Words.BE.Movie;
import Words.BLL.MovieManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class MovieModel {

    private ObservableList<Movie> moviesToBeViewed;
    private MovieManager movieManager;

    public MovieModel() throws Exception {
        movieManager = new MovieManager();
        moviesToBeViewed = FXCollections.observableArrayList();
        moviesToBeViewed.addAll(movieManager.getAllMovies());
    }

    public void createNewMovie(Movie newMovie) throws Exception {
        Movie m = movieManager.createMovie(newMovie);
        moviesToBeViewed.add(m);
    }

    public void deleteMovie(Movie selectedMovie) throws Exception {
        movieManager.deleteMovie(selectedMovie);
        moviesToBeViewed.remove(selectedMovie);
    }

    public void updateMovie(Movie movie) {
        try {
            // Update the movie in the database
            movieManager.updateMovie(movie);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception appropriately (logging, showing an error message, etc.)
        }
    }


    public void loadMovies() {
        try {
            moviesToBeViewed.clear();
            List<Movie> allMovies = movieManager.getAllMovies();
            moviesToBeViewed.addAll(allMovies);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Movie> getObservableMovies() {
        return moviesToBeViewed;
    }

    public void searchMovie(String query) throws Exception {
        List<Movie> searchResults = movieManager.searchMovies(query);
        moviesToBeViewed.clear();
        moviesToBeViewed.addAll(searchResults);
    }


}

package Words.BLL;

import Words.BE.Category;
import Words.BE.Movie;
import Words.DAL.IMovieDataAccess;
import Words.DAL.db.MovieDAO_DB;

import java.util.List;

public class MovieManager implements IMovieDataAccess {

    private IMovieDataAccess movieDAO;
    private MovieSearcher movieSearcher = new MovieSearcher();

    public MovieManager() throws Exception {
        movieDAO = new MovieDAO_DB();
    }

    @Override
    public List<Movie> getAllMovies() throws Exception {
        return movieDAO.getAllMovies();
    }


    @Override
    public Movie createMovie(Movie newMovie) throws Exception {
        return movieDAO.createMovie(newMovie);
    }

    @Override
    public void updateMovie(Movie movie) throws Exception {
        movieDAO.updateMovie(movie);
    }

    @Override
    public void deleteMovie(Movie selectedMovie) throws Exception {
        movieDAO.deleteMovie(selectedMovie);
    }

    @Override
    public List<Movie> getMoviesByCategory(Category category) throws Exception {
        return movieDAO.getMoviesByCategory(category);
    }

    public List<Movie> searchMovies(List<Movie> searchBase, String query) throws Exception {
        return movieSearcher.searchMovies(searchBase, query);
    }

}

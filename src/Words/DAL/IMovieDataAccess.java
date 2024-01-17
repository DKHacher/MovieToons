package Words.DAL;

import Words.BE.Category;
import Words.BE.Movie;

import java.util.List;

public interface IMovieDataAccess {

    public List<Movie> getAllMovies() throws Exception;

    public Movie createMovie(Movie movie) throws Exception;

    public void updateMovie(Movie movie) throws Exception;

    public void deleteMovie(Movie movie) throws Exception;

    public List<Movie> getMoviesByCategory(Category category) throws Exception;

    List<Movie> getMovieByRating(String ratingType, int rating) throws Exception;
}

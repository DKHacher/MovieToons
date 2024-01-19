package Words.BLL;

import Words.BE.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieSearcher {

    public static List<Movie> searchMovies(List<Movie> searchBase, String query) {
        List<Movie> searchResult = new ArrayList<>();
        for (Movie movie : searchBase) {
            if (compareToMovieTitle(query, movie)) {
                searchResult.add(movie);
            }
        }
        return searchResult;
    }

    public static List<Movie> searchRating(List<Movie> searchBase, int rating) {
        List<Movie> searchResult = new ArrayList<>();
        for (Movie movie : searchBase) {
            if (compareToRating(rating, movie)) {
                searchResult.add(movie);
            }
        }
        return searchResult;
    }

    private static boolean compareToMovieTitle(String query, Movie movie) {
        return movie.getMovieTitle().toLowerCase().contains(query.toLowerCase());
    }

    private static boolean compareToRating(int rating, Movie movie) {
        return movie.getRatingIMDB() == rating;
    }
}

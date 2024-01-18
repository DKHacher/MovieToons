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

    private static boolean compareToMovieTitle(String query, Movie movie) {
        return movie.getMovieTitle().toLowerCase().contains(query.toLowerCase());
    }


    public static List<Movie> searchByRating(List<Movie> searchBase, String ratingType, int rating) {
        List<Movie> searchResult = new ArrayList<>();
        for (Movie movie : searchBase) {
            if ("IMDB".equalsIgnoreCase(ratingType) && movie.getRatingIMDB() == rating) {
                searchResult.add(movie);
            } else if ("Personal".equalsIgnoreCase(ratingType) && movie.getRatingPersonal() == rating) {
                searchResult.add(movie);
            }
        }
        return searchResult;
    }
}

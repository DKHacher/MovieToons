package Words.BE;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;

import java.io.File;
import java.time.LocalDateTime;

public class Movie {

    private String fileLink, movieTitle;
    private LocalDateTime lastView;
    private int id, ratingIMDB, ratingPersonal;
    private Media media;

    public Movie(int id, String movieTitle, int ratingIMDB, int ratingPersonal, String fileLink, LocalDateTime lastView) {
        this.id = id;
        this.fileLink = fileLink;
        this.movieTitle = movieTitle;
        this.ratingIMDB = ratingIMDB;
        this.ratingPersonal = ratingPersonal;
        this.lastView = lastView;

        try {
            this.media = new Media(new File(fileLink).toURI().toString());
        } catch (MediaException e) {
            System.out.println("Error creating Media object " + fileLink);
            e.printStackTrace();
        }
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public int getIMDB() {
        return ratingIMDB;
    }

    public int getPersonal() {
        return ratingPersonal;
    }

    public String getFilePath() {
        return fileLink;
    }

    public LocalDateTime getLastView() {
        return lastView;
    }
}

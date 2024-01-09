package Words.BE;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Movie {

    private String fileLink, movieTitle;
    private Timestamp lastView;
    private int id, ratingIMDB, ratingPersonal;
    private Media media;
    private ArrayList<Words.BE.Category> Categories;

    public Movie(int id, String movieTitle, int ratingIMDB, int ratingPersonal, String fileLink, Timestamp lastView, ArrayList<Category> categories) {
        this.id = id;
        this.fileLink = fileLink;
        this.movieTitle = movieTitle;
        this.ratingIMDB = ratingIMDB;
        this.ratingPersonal = ratingPersonal;
        this.lastView = lastView;
        this.Categories = categories;

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

    public String getRatingIMDB() {
        return ratingIMDB + "/10";
    }

    public String getRatingPersonal() {
        return ratingPersonal + "/10";
    }

    public String getFilePath() {
        return fileLink;
    }

    public Timestamp getLastView() {
        return lastView;
    }

    public int getId() {
        return id;
    }
}

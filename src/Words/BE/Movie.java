package Words.BE;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;

import java.io.File;
import java.sql.Timestamp;

public class Movie {

    private String fileLink, movieTitle;
    private Timestamp lastView;
    private int id, ratingIMDB, ratingPersonal;
    private Media media;
    //private ArrayList<Words.BE.Category> categories;

    public Movie(int id, String movieTitle, int ratingIMDB, int ratingPersonal, String fileLink, Timestamp lastView) {
        this.id = id;
        this.fileLink = fileLink;
        this.movieTitle = movieTitle;
        this.ratingIMDB = ratingIMDB;
        this.ratingPersonal = ratingPersonal;
        this.lastView = lastView;
        //this.Categories = categories;

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

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle; }

    public int getRatingIMDB() {
        return ratingIMDB;
    }

    public void setRatingIMDB(int ratingIMDB) {
        this.ratingIMDB = ratingIMDB;
    }

    public int getRatingPersonal() {
        return ratingPersonal;
    }

    public void setRatingPersonal(int ratingPersonal) {
        this.ratingPersonal = ratingPersonal;
    }

    public String getFilePath() {
        return fileLink;
    }

    public void setFilePath(String fileLink) {
        this.fileLink = fileLink;
    }

    public Timestamp getLastView() {
        return lastView;
    }

    public void setLastView(Timestamp lastView) {
        this.lastView = lastView;
    }

    public int getId() {
        return id;
    }

}

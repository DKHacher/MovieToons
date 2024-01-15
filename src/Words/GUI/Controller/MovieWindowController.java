package Words.GUI.Controller;

import Words.BE.Movie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.time.Instant;

public class MovieWindowController {

    @FXML
    private Label movieLabel;
    @FXML
    private Label genresLabel;

    private Movie movie;


    public void setMovieDetails(String name, String genres, Movie movie) {
        movieLabel.setText(name);
        genresLabel.setText("Genres: " + genres);
        this.movie = movie;
    }

    @FXML
    private void handleClose(ActionEvent actionEvent) {
        Stage stage = (Stage) ((javafx.scene.control.Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    private void WatchMovie(ActionEvent actionEvent) {
        Timestamp ts = Timestamp.from(Instant.now());
        movie.setLastView(ts);
    }
}

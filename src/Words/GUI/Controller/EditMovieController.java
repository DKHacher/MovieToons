package Words.GUI.Controller;

import Words.BE.Movie;
import Words.GUI.Model.MovieModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditMovieController {

    private MovieModel movieModel;
    private Movie selectedMovie;
    @FXML
    private TextField txtNewMovieTitle, intNewPersonalRating, intNewIMDBRating, txtNewFilePath;

    public void setManager(MovieModel movieModel) {
        this.movieModel = movieModel;
    }

    @FXML
    private void handleClose(ActionEvent actionEvent) {
        Stage stage = (Stage) ((javafx.scene.control.Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void setSelectedMovie(Movie selectedMovie) {
        this.selectedMovie = selectedMovie;
        if (selectedMovie != null) {
            txtNewMovieTitle.setText(selectedMovie.getMovieTitle());
            intNewPersonalRating.setText(String.valueOf(selectedMovie.getPersonal()));
            intNewIMDBRating.setText(String.valueOf(selectedMovie.getIMDB()));
            txtNewFilePath.setText(selectedMovie.getFilePath());
        }
    }
}

package Words.GUI.Controller;

import Words.GUI.Model.MovieModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class NewMovieController {

    private MovieModel movieModel;

    public void setManager(MovieModel movieModel) {
        this.movieModel = movieModel;
    }

    @FXML
    private void handleClose(ActionEvent actionEvent) {
        Stage stage = (Stage) ((javafx.scene.control.Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

}

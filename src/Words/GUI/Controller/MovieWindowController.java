package Words.GUI.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class MovieWindowController {

    @FXML
    private Label movieLabel;
    @FXML
    private Label genresLabel;


    public void setMovieDetails(String name, String genres) {
        movieLabel.setText(name);
        genresLabel.setText("Genres: " + genres);
    }

    @FXML
    private void handleClose(ActionEvent actionEvent) {
        Stage stage = (Stage) ((javafx.scene.control.Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}

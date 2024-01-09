package Words.GUI.Controller;

import Words.BE.Category;
import Words.GUI.Model.CategoryModel;
import Words.GUI.Model.MovieModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class NewMovieController implements Initializable {

    private MovieModel movieModel;
    private CategoryModel categoryModel;

    @FXML
    private TextField txtMovieTitle;
    @FXML
    private TextField txtFilePath;
    @FXML
    private TextField txtRatingIMDB;
    @FXML
    private TextField txtRatingPersonal;
    @FXML
    private ComboBox<Category> cmbGenres;
    @FXML
    private ListView<Category> lvSelectedGenres;

    private ObservableList<Category> selectedGenres;

    public NewMovieController() {
        try {
            movieModel = new MovieModel();
            categoryModel = new CategoryModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadGenres();

        selectedGenres = FXCollections.observableArrayList();
        lvSelectedGenres.setItems(selectedGenres);
    }

    @FXML
    private void handleAddGenre() {
        Category selectedGenre = cmbGenres.getSelectionModel().getSelectedItem();
        if (selectedGenre != null && !selectedGenres.contains(selectedGenre)) {
            selectedGenres.add(selectedGenre);
        }
    }

    @FXML
    private void handleRemoveGenre() {
        Category selectedGenre = lvSelectedGenres.getSelectionModel().getSelectedItem();
        if (selectedGenre != null) {
            selectedGenres.remove(selectedGenre);
        }
    }

    @FXML
    private void handleClose(ActionEvent actionEvent) {
        Stage stage = (Stage) ((javafx.scene.control.Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    private void loadGenres() {
        try {
            categoryModel.loadCategories();
            cmbGenres.setItems(categoryModel.getObservableCategories());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

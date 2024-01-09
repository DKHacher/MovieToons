package Words.GUI.Controller;

import Words.BE.Category;
import Words.BE.Movie;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.nio.file.StandardCopyOption;


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
    private void handleSave(ActionEvent actionEvent) {
        try {
            String movieTitle = txtMovieTitle.getText();
            String filePath = txtFilePath.getText();
            int ratingIMDB = Integer.parseInt(txtRatingIMDB.getText());
            int ratingPersonal = Integer.parseInt(txtRatingPersonal.getText());
            ArrayList<Category> selectedGenresList = new ArrayList<>(selectedGenres);

            Movie newMovie = new Movie(0, movieTitle, ratingIMDB, ratingPersonal, filePath, null, selectedGenresList);

            movieModel.createNewMovie(newMovie);

            Stage stage = (Stage) ((javafx.scene.control.Button) actionEvent.getSource()).getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
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
    private void handleChooseFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP4 files (*.mp4)", "*.mp4"));

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String destinationPath = "Resources/movies/" + selectedFile.getName();
            File destinationFile = new File(destinationPath);

            try {
                // Copy the selected file to the destination path
                java.nio.file.Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
            txtFilePath.setText(destinationPath);
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

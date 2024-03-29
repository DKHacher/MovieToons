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
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class NewMovieController implements Initializable {

    private MovieModel movieModel;
    private CategoryModel categoryModel;

    @FXML
    private TextField txtMovieTitle, txtFilePath;
    @FXML
    private ChoiceBox<Category> choiceGenres;
    @FXML
    private ListView<Category> lvSelectedGenres;
    @FXML
    private ChoiceBox<Integer> choiceRatingIMDB, choiceRatingPersonal;

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
        selectedGenres = FXCollections.observableArrayList();
        lvSelectedGenres.setItems(selectedGenres);
        choiceRatingIMDB.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        choiceRatingPersonal.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        loadGenres();
    }


    @FXML
    private void handleSave(ActionEvent actionEvent) {
        try {
            String movieTitle = txtMovieTitle.getText();
            String originalFilePath = txtFilePath.getText();
            int ratingIMDB = choiceRatingIMDB.getValue();
            int ratingPersonal = choiceRatingPersonal.getValue();
            ArrayList<Category> selectedGenresList = new ArrayList<>(selectedGenres);

            if (isValidMediaFile(originalFilePath)) {
                //Copy the file to the data folder
                File originalFile = new File(originalFilePath);
                File dataFolder = new File("Resources/Movies/");
                File copiedFile = new File(dataFolder, originalFile.getName());

                //Perform the copy
                if (!copiedFile.exists()) {
                    Files.copy(originalFile.toPath(), copiedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }

                //Update the file path with the new path in the data folder
                String filePath = "Resources/Movies/" + copiedFile.getName();

                Movie newMovie = new Movie(-1, movieTitle, ratingIMDB, ratingPersonal, filePath, null, selectedGenresList);
                movieModel.createNewMovie(newMovie);

                //Close the window
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                stage.close();
            } else {
                alertFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddGenre(ActionEvent actionEvent) {
        Category selectedGenre = choiceGenres.getValue();
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
        //Filters for the file chooser
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP4 Files", "*.mp4"),
                new FileChooser.ExtensionFilter("MPEG4 Files", "*.MPEG4"),
                new FileChooser.ExtensionFilter("All Files", "*.*")

        );

        File selectedFile = fileChooser.showOpenDialog(((Button) actionEvent.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            // Set the chosen file path to the txtFile TextField
            txtFilePath.setText(selectedFile.getAbsolutePath());

            boolean isValidFile = isValidMediaFile(selectedFile.getAbsolutePath());
            System.out.println("Is Valid File: " + isValidFile);
        }
    }

    private boolean isValidMediaFile(String filePath) {
        File file = new File(filePath);

        System.out.println("Selected File: " + filePath);
        System.out.println("File Size: " + file.length() + " bytes");

        boolean isValidExtension = filePath.toLowerCase().endsWith(".mp4") || filePath.toLowerCase().endsWith(".MPEG4");
        System.out.println("Is Valid Extension: " + isValidExtension);

        return isValidExtension;
    }

    @FXML
    private void handleClose(ActionEvent actionEvent) {
        Stage stage = (Stage) ((javafx.scene.control.Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    private void loadGenres() {
        try {
            categoryModel.loadCategories();
            choiceGenres.setItems(categoryModel.getObservableCategories());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void alertFile() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Unsupported file format. Please select a valid media file.");
        alert.showAndWait();

        System.out.println("Unsupported file format. Please select a valid media file.");
    }


}

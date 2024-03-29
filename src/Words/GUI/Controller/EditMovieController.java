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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EditMovieController implements Initializable {

    private MovieModel movieModel;
    private CategoryModel categoryModel;
    private Movie selectedMovie;
    @FXML
    private TextField txtNewMovieTitle, txtNewFilePath;
    @FXML
    private ChoiceBox<Category> choiceNewGenres;
    @FXML
    private ChoiceBox<Integer> choiceNewPersonalRating, choiceNewIMDBRating;
    @FXML
    private ListView<Category> lvSelectedNewGenres;

    private ObservableList<Category> selectedNewGenres;


    public EditMovieController(){
        try {
            movieModel = new MovieModel();
            categoryModel = new CategoryModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setManager(MovieModel movieModel) {
        this.movieModel = movieModel;
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadGenres();

        selectedNewGenres = FXCollections.observableArrayList();
        lvSelectedNewGenres.setItems(selectedNewGenres);

        choiceNewPersonalRating.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        choiceNewIMDBRating.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    }

    @FXML
    private void handleClose(ActionEvent actionEvent) {
        Stage stage = (Stage) ((javafx.scene.control.Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleSave(ActionEvent actionEvent) {
        String newMovieTitle = txtNewMovieTitle.getText();
        int newRatingIMDB = choiceNewIMDBRating.getValue();
        int newRatingPersonal = choiceNewPersonalRating.getValue();
        String newFilePath = txtNewFilePath.getText();
        ArrayList<Category> categoryList = new ArrayList<>(selectedNewGenres);

        if (movieModel != null && selectedMovie != null) {
            try {
                selectedMovie.setMovieTitle(newMovieTitle);
                selectedMovie.setRatingIMDB(newRatingIMDB);
                selectedMovie.setRatingPersonal(newRatingPersonal);
                selectedMovie.setFilePath(newFilePath);
                selectedMovie.setCategories(categoryList);
                movieModel.updateMovie(selectedMovie);

                Stage stage = (Stage) ((javafx.scene.control.Button) actionEvent.getSource()).getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setSelectedMovie(Movie selectedMovie) {
        this.selectedMovie = selectedMovie;
        if (selectedMovie != null) {
            txtNewMovieTitle.setText(selectedMovie.getMovieTitle());
            choiceNewPersonalRating.setValue(selectedMovie.getRatingPersonal());
            choiceNewIMDBRating.setValue(selectedMovie.getRatingIMDB());
            txtNewFilePath.setText(selectedMovie.getFilePath());
            selectedNewGenres.addAll(selectedMovie.getCategories());
        }
    }



    private void loadGenres() {
        try {
            categoryModel.loadCategories();
            choiceNewGenres.setItems(categoryModel.getObservableCategories());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddGenre() {
        Category selectedGenre = choiceNewGenres.getSelectionModel().getSelectedItem();
        if (selectedGenre != null && !selectedNewGenres.contains(selectedGenre)) {
            selectedNewGenres.add(selectedGenre);
        }
    }

    @FXML
    private void handleRemoveGenre() {
        Category selectedGenre = lvSelectedNewGenres.getSelectionModel().getSelectedItem();
        if (selectedGenre != null) {
            selectedNewGenres.remove(selectedGenre);
        }
    }


}

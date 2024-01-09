package Words.GUI.Controller;

import Words.BE.Category;
import Words.BE.Movie;
import Words.GUI.Model.CategoryModel;
import Words.GUI.Model.MovieModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TableView<Movie> allMoviesTbl;
    @FXML
    private TableView<Category> categoriesTbl;
    @FXML
    private TableView<Movie> moviesTbl;
    @FXML
    private TableColumn <Movie, String> colTitleAll, colGenreAll, colTitle, colGenre;
    @FXML
    private TableColumn <Movie, Integer> colRatingIMDBAll, colRatingPersonalAll, colRatingIMDB, colRatingPersonal;
    @FXML
    private TableColumn <Category, String> colCatType;
    @FXML
    private TextField txtMovieSearch;

    private MovieModel movieModel;
    private CategoryModel categoryModel;

    public MainController() {
        try {
            movieModel = new MovieModel();
            categoryModel = new CategoryModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Initialize Methods
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableColumns();
        initializeModels();
        initializeSearchListener();
    }

    private void initializeTableColumns() {
        //Movies
        colTitle.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));
        colRatingIMDB.setCellValueFactory(new PropertyValueFactory<>("ratingIMDB"));
        colRatingPersonal.setCellValueFactory(new PropertyValueFactory<>("ratingPersonal"));

        //All movies
        colTitleAll.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));
        colRatingIMDBAll.setCellValueFactory(new PropertyValueFactory<>("ratingIMDB"));
        colRatingPersonalAll.setCellValueFactory(new PropertyValueFactory<>("ratingPersonal"));

        //categories
        colCatType.setCellValueFactory(new PropertyValueFactory<>("catType"));
    }

    private void initializeModels() {
        movieModel.loadMovies();
        allMoviesTbl.setItems(movieModel.getObservableMovies());

        categoryModel.loadCategories();
        categoriesTbl.setItems(categoryModel.getObservableCategories());
    }

    private void initializeSearchListener() {
        txtMovieSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
           try {
                movieModel.searchMovie(newValue);
           } catch (Exception e) {
               e.printStackTrace();
           }
        });
    }


    //FXML
    @FXML
    private void handleCreateMovie() {
        openMovieNew();
    }

    @FXML
    private void handleEditAllMovies() {
        openMovieEdit(allMoviesTbl);
    }

    @FXML
    private void handleEditMovie() {
        openMovieEdit(moviesTbl);
    }

    @FXML
    private void handleCreateCategory() {
        openCategoryNew();
    }

    @FXML
    private void handleUpdateCategory() {
        openCategoryEdit();
    }

    @FXML
    private void handleDeleteCategory() {
        deleteCategory();
    }


    //Methods
    private void deleteCategory() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you wish to delete this category?");

        //Buttons
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Category selectedCategory = categoriesTbl.getSelectionModel().getSelectedItem();
            if (selectedCategory != null) {
                try {
                    categoryModel.deleteCategory(selectedCategory);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Methods to open windows
    private void openCategoryEdit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditCategory.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Category");

            CategoryEditController categoryEditController = loader.getController();

            Category selectedCategory = categoriesTbl.getSelectionModel().getSelectedItem();
            categoryEditController.setSelectedCategory(selectedCategory);

            categoryEditController.setManager(categoryModel);

            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.setResizable(false);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openCategoryNew() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/NewCategory.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("New Category");

            CategoryNewController categoryNewController = loader.getController();
            categoryNewController.setManager(categoryModel);

            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.setResizable(false);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openMovieNew() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/NewMovie.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("New Movie");

            NewMovieController newMovieController = loader.getController();
            newMovieController.setManager(movieModel);

            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.setResizable(false);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openMovieEdit(TableView<Movie> tableView) {
        Movie selectedMovie = tableView.getSelectionModel().getSelectedItem();

        if (selectedMovie != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditMovie.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Edit Movie");

                EditMovieController editMovieController = loader.getController();
                editMovieController.setSelectedMovie(selectedMovie);
                editMovieController.setManager(movieModel);

                Scene scene = new Scene(root);
                stage.setScene(scene);

                stage.setResizable(false);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            noMovieAlert();
        }
    }

    private void noMovieAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Movie Selected");
        alert.setHeaderText("No movie selected!");
        alert.setContentText("Please select a movie.");

        alert.showAndWait();
    }
}

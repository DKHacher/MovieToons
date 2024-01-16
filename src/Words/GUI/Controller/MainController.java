package Words.GUI.Controller;

import Words.BE.Category;
import Words.BE.Movie;
import Words.GUI.Model.CategoryModel;
import Words.GUI.Model.MovieModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TableView<Movie> allMoviesTbl;
    @FXML
    private TableView<Category> categoriesTbl;
    @FXML
    private TableColumn <Movie, String> colTitleAll, colGenreAll;
    @FXML
    private TableColumn <Movie, Integer> colRatingIMDBAll, colRatingPersonalAll;
    @FXML
    private TableColumn <Movie, Timestamp> colLastViewAll;
    @FXML
    private TableColumn <Category, String> colCatType;
    @FXML
    private TextField txtMovieSearch;

    private MovieModel movieModel;
    private CategoryModel categoryModel;
    private boolean isCategorySelected = false;
    private boolean isMovieSelected = false;

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
        initializeModels();
        initializeTableColumns();
        initializeSearchListener();
        initializeDoubleClickCheck();
    }

    private void initializeTableColumns() {

        //All movies
        colTitleAll.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));
        colGenreAll.setCellValueFactory((new PropertyValueFactory<>("CategoriesAsString")));
        colRatingIMDBAll.setCellValueFactory(new PropertyValueFactory<>("ratingIMDB"));
        colRatingPersonalAll.setCellValueFactory(new PropertyValueFactory<>("ratingPersonal"));
        colLastViewAll.setCellValueFactory(new PropertyValueFactory<>("lastView"));

        //categories
        colCatType.setCellValueFactory(new PropertyValueFactory<>("catType"));
    }

    private void initializeModels() {
        try {
            movieModel.loadMovies();
            allMoviesTbl.setItems(movieModel.getObservableMovies());

            categoryModel.loadCategories();
            categoriesTbl.setItems(categoryModel.getObservableCategories());

            loadMoviesBySelectedCategory();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void initializeDoubleClickCheck() {
        allMoviesTbl.setOnMouseClicked(this::handleMovieDoubleClick);
    }

    //FXML
    @FXML
    private void handleCreateMovie() {
        openMovieNew();
        allMoviesTbl.refresh();
    }

    @FXML
    private void handleEditAllMovies() {
        Movie selectedMovie = allMoviesTbl.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            openMovieEdit();
            allMoviesTbl.refresh();
        } else {
            noMovieAlert();
        }
    }

    @FXML
    private void handleDeleteMovie() {
        deleteMovie();
        allMoviesTbl.refresh();
    }

    @FXML
    private void handleCreateCategory() {
        openCategoryNew();
    }

    @FXML
    private void handleUpdateCategory() {
        Category selectedCategory = categoriesTbl.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            openCategoryEdit();
        } else {
            noCategoryAlert();
        }
    }

    @FXML
    private void handleDeleteCategory() {
        deleteCategory();
    }


    @FXML
    private void handleCategoryClick(javafx.scene.input.MouseEvent mouseEvent) {
        toggleCategorySelection();
        loadMoviesBySelectedCategory();
    }

    @FXML
    private void handleMovieDoubleClick(javafx.scene.input.MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            toggleMovieSelection();
        }
        else if (mouseEvent.getClickCount() == 2) {
            openMovieWindow();
        }
    }

    @FXML
    private void handleResetSearch() {
        txtMovieSearch.clear();
        categoriesTbl.getSelectionModel().clearSelection();
        loadAllMovies(); // Load all movies instead of filtering by category
    }




    //Methods
    private void toggleCategorySelection() {
        Category selectedCategory = categoriesTbl.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            categoriesTbl.getSelectionModel().select(selectedCategory);
        }
        categoriesTbl.refresh();
    }

    private void loadAllMovies() {
        try {
            movieModel.loadMovies(); // Load all movies
            allMoviesTbl.setItems(movieModel.getObservableMovies());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadMoviesBySelectedCategory() {
        Category selectedCategory = categoriesTbl.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            try {
                List<Movie> moviesByCategory = movieModel.getMoviesByCategory(selectedCategory);
                allMoviesTbl.setItems(FXCollections.observableArrayList(moviesByCategory));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            loadAllMovies();
        }
    }


    private void toggleMovieSelection() {
        Movie selectedMovie = allMoviesTbl.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            allMoviesTbl.getSelectionModel().select(selectedMovie);
        }
        allMoviesTbl.refresh();
    }


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

    private void deleteMovie() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you wish to delete this movie?");

        //Buttons
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Movie selectedMovie = allMoviesTbl.getSelectionModel().getSelectedItem();
            if (selectedMovie != null) {
                try {
                    movieModel.deleteMovie(selectedMovie);
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

            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.setResizable(false);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openMovieEdit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditMovie.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Movie");

            EditMovieController editMovieController = loader.getController();

            Movie selectedMovie = allMoviesTbl.getSelectionModel().getSelectedItem();
            editMovieController.setSelectedMovie(selectedMovie);

            editMovieController.setManager(movieModel);

            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void openMovieWindow() {
        Movie selectedMovie = allMoviesTbl.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/MovieWindow.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Movie Details");

                MovieWindowController movieWindowController = loader.getController();
                movieWindowController.setMovieDetails(selectedMovie.getMovieTitle(), selectedMovie.getCategoriesAsString(), selectedMovie);

                Scene scene = new Scene(root);
                stage.setScene(scene);

                stage.setResizable(false);
                stage.showAndWait();
                movieModel.updateMovie(selectedMovie);
                allMoviesTbl.refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //Alerts

    private void noMovieAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Movie Selected");
        alert.setHeaderText("No movie selected!");
        alert.setContentText("Please select a movie.");

        alert.showAndWait();
    }

    private void noCategoryAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Category Selected");
        alert.setHeaderText("No category selected!");
        alert.setContentText("Please select a category.");

        alert.showAndWait();
    }

    @FXML
    private void HandleBackgroundClick(MouseEvent mouseEvent) {
        allMoviesTbl.getSelectionModel().clearSelection();
        categoriesTbl.getSelectionModel().clearSelection();
    }
}

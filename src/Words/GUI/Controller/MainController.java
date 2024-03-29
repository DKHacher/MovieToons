package Words.GUI.Controller;

import Words.BE.Category;
import Words.BE.Movie;
import Words.GUI.Model.CategoryModel;
import Words.GUI.Model.MovieModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
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
    private ChoiceBox ratingNumberChoiceBox, minChoiceBox, maxChoiceBox;
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
    @FXML
    private ChoiceBox<String> ratingTypeChoiceBox;
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
        initializeChoiceBox();

        //must be loaded last
        moviesToDeleteInitialize();
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

    private void initializeChoiceBox() {
        ratingTypeChoiceBox.setItems(FXCollections.observableArrayList("Personal", "IMDB"));
        ratingNumberChoiceBox.setItems(FXCollections.observableArrayList("0", "1","2","3","4","5","6","7","8","9","10"));
        minChoiceBox.setItems(FXCollections.observableArrayList("0", "1","2","3","4","5","6","7","8","9","10"));
        maxChoiceBox.setItems(FXCollections.observableArrayList("0", "1","2","3","4","5","6","7","8","9","10"));
    }

    private void moviesToDeleteInitialize() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        timestamp = Timestamp.valueOf(timestamp.toLocalDateTime().minusYears(2));
        String moviesToDelete = "Movies to Delete: ";
        for (Movie movie : allMoviesTbl.getItems()) {
            if (movie.getRatingPersonal() < 6 && movie.getLastView() != null && movie.getLastView().before(timestamp)) {
                moviesToDelete += movie.getMovieTitle() + ", ";
            }
        }
        if (moviesToDelete.equals("Movies to Delete: ")) {
            moviesToDelete += "None";
        } else {
            moviesToDelete = moviesToDelete.substring(0, moviesToDelete.length() - 2);
        }
        moviesToDeleteAlert(moviesToDelete);
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
    private void handleOpenMovie() {
        Movie selectedMovie = allMoviesTbl.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            openMovieWindow();
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
        ratingTypeChoiceBox.getSelectionModel().clearSelection();
        ratingNumberChoiceBox.getSelectionModel().clearSelection();
        minChoiceBox.getSelectionModel().clearSelection();
        maxChoiceBox.getSelectionModel().clearSelection();
    }







    //Methods
    private void toggleCategorySelection() {
        Category selectedCategory = categoriesTbl.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            categoriesTbl.getSelectionModel().select(selectedCategory);
        }
        categoriesTbl.refresh();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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

    private void moviesToDeleteAlert(String moviesToDelete) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Movies To Delete");
        alert.setHeaderText("Movies To Delete");
        alert.setContentText(moviesToDelete);

        alert.showAndWait();
    }

    @FXML
    private void HandleBackgroundClick(MouseEvent mouseEvent) {
        allMoviesTbl.getSelectionModel().clearSelection();
        categoriesTbl.getSelectionModel().clearSelection();
        loadAllMovies();
    }

    public void filterMoviesOnRating(ActionEvent actionEvent) throws Exception {
        if (ratingNumberChoiceBox.getValue() != null && !ratingNumberChoiceBox.getValue().equals("") && ratingTypeChoiceBox.getValue() != null && !ratingTypeChoiceBox.getValue().equals("")) {
            List<Movie> moviesByRating = movieModel.getMoviesByRating(ratingTypeChoiceBox.getValue(), Integer.parseInt((String) ratingNumberChoiceBox.getValue()));
            allMoviesTbl.setItems(FXCollections.observableArrayList(moviesByRating));
        }
    }

    @FXML
    private void filterMoviesByRating(ActionEvent actionEvent) {
        try {
            String ratingType = ratingTypeChoiceBox.getValue();
            Integer minRating = (minChoiceBox.getValue() != null) ? Integer.parseInt((String) minChoiceBox.getValue()) : null;
            Integer maxRating = (maxChoiceBox.getValue() != null) ? Integer.parseInt((String) maxChoiceBox.getValue()) : null;

            if (minRating != null && maxRating != null && ratingType != null) {
                List<Movie> filteredMovies = movieModel.getMoviesByRatingRange(ratingType, minRating, maxRating);
                allMoviesTbl.setItems(FXCollections.observableArrayList(filteredMovies));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

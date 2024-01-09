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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    private TableColumn <Movie, String> colTitle, colGenre;
    @FXML
    private TableColumn <Movie, Integer> colRatingIMDB;
    @FXML
    private TableColumn <Category, String> colCatType;

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

    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableColumns();
        initializeModels();
        initializeSearchListener();
    }

    private void initializeTableColumns() {
        //movies
        colTitle.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));

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

}

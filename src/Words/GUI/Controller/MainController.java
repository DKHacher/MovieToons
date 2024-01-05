package Words.GUI.Controller;

import Words.BE.Category;
import Words.GUI.Model.CategoryModel;
import Words.GUI.Model.MovieModel;
import Words.BE.Movie;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
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
}

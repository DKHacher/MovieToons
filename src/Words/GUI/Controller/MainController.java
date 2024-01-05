package Words.GUI.Controller;

import Words.GUI.Model.MovieModel;
import Words.BE.Movie;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController {

    @FXML
    private TableView<Movie> allMoviesTbl;
    @FXML
    private TableColumn <Movie, String> colTitle, colGenre;
    @FXML
    private TableColumn <Movie, Integer> colRatingIMDB;


    private MovieModel movieModel;

    public MainController() {
        try {
            movieModel = new MovieModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableColumns();
        initializeModels();
    }

    private void initializeTableColumns() {
        colTitle.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));
    }

    private void initializeModels() {
        movieModel.loadMovies();
        allMoviesTbl.setItems(movieModel.getObservableMovies());

    }
}

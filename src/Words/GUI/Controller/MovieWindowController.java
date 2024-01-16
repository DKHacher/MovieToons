package Words.GUI.Controller;

import Words.BE.Movie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ResourceBundle;

public class MovieWindowController implements Initializable {

    @FXML
    private Label movieLabel;
    @FXML
    private Label genresLabel;
    @FXML
    private MediaView mediaView;
    @FXML
    private ImageView playPauseIMG, volumeImage;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Label volumeLabel;

    private Movie movie;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private Image playImage, pauseImage, mutedImage, smallVolImage, mediumVolImage, highVolImage;
    private double storedVolumePercent;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initializeThumbnail();
      initializeImages();
      initializeVolumeSlider();
      initializePlayPauseButton();

    }

    private void initializeThumbnail() {
        if (movie != null) {
            loadThumbnail();
        }
    }

    private void initializeImages() {
        playImage = new Image(getClass().getResourceAsStream("/Buttons/play.png"));
        pauseImage = new Image(getClass().getResourceAsStream("/Buttons/pause.png"));
        playPauseIMG.setImage(playImage);

        mutedImage = new Image(getClass().getResourceAsStream("/Buttons/Muted.png"));
        smallVolImage = new Image(getClass().getResourceAsStream("/Buttons/1-25.png"));
        mediumVolImage = new Image(getClass().getResourceAsStream("/Buttons/25-75.png"));
        highVolImage = new Image(getClass().getResourceAsStream("/Buttons/75-100.png"));
    }

    private void initializeVolumeSlider(){
        volumeHandler();// Calls the volumeHandler method so the Slider works
        volumeSlider.setValue(50); // Sets the start value of the slider to 50% when the program opens up.
    }

    public void setMovieDetails(String name, String genres, Movie movie) {
        movieLabel.setText(name);
        genresLabel.setText("Genres: " + genres);
        this.movie = movie;

        // Load thumbnail only if the controller has been initialized
        if (mediaView != null) {
            loadThumbnail();
        }
    }

    private void initializePlayPauseButton(){
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @FXML
    private void watchMovie(ActionEvent actionEvent) {
        Timestamp ts = Timestamp.from(Instant.now());
        movie.setLastView(ts);

        // Toggle between play and pause
        if (isPlaying) {
            mediaPlayer.pause();
        } else {
            // Create Media and MediaPlayer
            Media media = new Media(new File(movie.getFilePath()).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            // Bind MediaPlayer to MediaView
            mediaView.setMediaPlayer(mediaPlayer);

            // Set aspect ratio
            mediaView.setFitWidth(480);
            mediaView.setFitHeight(270);

            // Play the video
            mediaPlayer.play();

            // Close
            Stage stage = (Stage) mediaView.getScene().getWindow();
            stage.setOnCloseRequest(windowEvent -> stopMediaPlayer());
        }

        // Update the play/pause status
        isPlaying = !isPlaying;

        // Call playPause() to update the play/pause button image
        playPause();
    }

    private void loadThumbnail() {
        // Create Media and MediaPlayer for loading the thumbnail
        Media media = new Media(new File(movie.getFilePath()).toURI().toString());
        MediaPlayer thumbnailPlayer = new MediaPlayer(media);

        // Seek to the desired time for the thumbnail
        Duration thumbnailTime = Duration.seconds(0.1); // Adjust as needed
        thumbnailPlayer.seek(thumbnailTime);

        // Bind MediaPlayer to MediaView
        mediaView.setMediaPlayer(thumbnailPlayer);
    }

    private void stopMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    private void updatePlayPauseImage() {
        if (mediaPlayer != null) {
            MediaPlayer.Status status = mediaPlayer.getStatus();

            if (status == MediaPlayer.Status.PLAYING) {
                playPauseIMG.setImage(playImage);
            } else if (status == MediaPlayer.Status.PAUSED || status == MediaPlayer.Status.READY) {
                playPauseIMG.setImage(pauseImage);
            } else if (status == MediaPlayer.Status.STOPPED) {
                playPauseIMG.setImage(playImage);
            }
        }
    }

    private void playPause() {
        if (mediaPlayer != null) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.play();
            }

            updatePlayPauseImage();
        }
    }


    public void volumeHandler(){

        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (mediaPlayer != null)
                mediaPlayer.setVolume(newVal.doubleValue() / 100.0);
            updateVolumeLabel(newVal.doubleValue());

        });
        //setting the initial volume
        updateVolumeLabel(volumeSlider.getValue());
    }

    private void updateVolumeLabel(double volumePercentage) {
        storedVolumePercent = volumePercentage;

        volumeLabel.setText(String.format("%.0f%%", volumePercentage));

        if (volumePercentage == 0) {
            volumeImage.setImage(mutedImage);
        } else if (volumePercentage > 0 && volumePercentage <= 25 ) {
            volumeImage.setImage(smallVolImage);
        } else if (volumePercentage > 25 && volumePercentage <= 75) {
            volumeImage.setImage(mediumVolImage);
        } else if (volumePercentage > 75) {
            volumeImage.setImage(highVolImage);
        }
    }
}
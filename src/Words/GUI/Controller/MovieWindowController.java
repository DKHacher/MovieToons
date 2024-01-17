package Words.GUI.Controller;

import Words.BE.Movie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    @FXML
    private ProgressBar progressBar;

    private Movie movie;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private Image playImage, pauseImage, mutedImage, smallVolImage, mediumVolImage, highVolImage;
    private double storedVolumePercent;
    private boolean isSeeking = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initializeThumbnail();
      initializeImages();
      initializeVolumeSlider();
      initializePlayPauseButton();
      initializeProgressBar();

    }

    private void initializeThumbnail() {
        if (movie != null) {
            loadThumbnail();
        }
    }

    private void initializeProgressBar() {
        if (mediaPlayer != null) {
            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                double progress = newValue.toSeconds() / mediaPlayer.getTotalDuration().toSeconds();
                progressBar.setProgress(progress);
            });
            progressBar.setOnMouseClicked(event -> handleProgressBarClick(event));
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
        volumeLabel.setText("50%");
    }

    @FXML
    private void handleProgressBarPressed() {
        isSeeking = true;
    }

    @FXML
    private void handleProgressBarReleased() {
        isSeeking = false;
    }

    @FXML
    private void watchMovie(ActionEvent actionEvent) {
        Timestamp ts = Timestamp.from(Instant.now());
        movie.setLastView(ts);

        // Toggle between play and pause
        if (mediaPlayer != null) {
            MediaPlayer.Status status = mediaPlayer.getStatus();
            if (status == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
            } else if (status == MediaPlayer.Status.PAUSED || status == MediaPlayer.Status.READY) {
                mediaPlayer.play();
            }
        } else {
            // Media player is not initialized, prepare and play
            prepareMediaPlayer();
            mediaPlayer.play();

            // Close
            Stage stage = (Stage) mediaView.getScene().getWindow();
            stage.setOnCloseRequest(windowEvent -> stopMediaPlayer());
        }

        // Update the play/pause status
        isPlaying = mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;

        // Call playPause() to update the play/pause button image
        playPause();
    }



    private void handleProgressBarClick(MouseEvent event) {
        if (mediaPlayer != null) {
            double mouseX = event.getX();
            double progressBarWidth = progressBar.getWidth();
            double seekTime = (mouseX / progressBarWidth) * mediaPlayer.getTotalDuration().toSeconds();

            // Check if the video is playing or paused
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.seek(Duration.seconds(seekTime));
            } else {
                // If the video is paused, seek to the desired time and keep it paused
                mediaPlayer.pause();
                mediaPlayer.seek(Duration.seconds(seekTime));
            }
        }
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



    private void prepareMediaPlayer() {
        // Create Media and MediaPlayer
        Media media = new Media(new File(movie.getFilePath()).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        // Bind MediaPlayer to MediaView
        mediaView.setMediaPlayer(mediaPlayer);

        // Set aspect ratio
        mediaView.setFitWidth(480);
        mediaView.setFitHeight(270);

        // Initialize the progress bar listener
        initializeProgressBar();
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

    private void playPause() {
        if (mediaPlayer != null) {
            MediaPlayer.Status status = mediaPlayer.getStatus();

            if (status == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.play();
            }

            updatePlayPauseImage();
        }
    }

    private void updatePlayPauseImage() {
        MediaPlayer.Status status = mediaPlayer.getStatus();

        if (status == MediaPlayer.Status.PLAYING) {
            playPauseIMG.setImage(playImage);
        } else {
            playPauseIMG.setImage(pauseImage);
        }
    }

    public void volumeHandler() {
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (mediaPlayer != null) {
                double scaledVolume = Math.pow(newVal.doubleValue() / 100.0, 2);
                mediaPlayer.setVolume(scaledVolume);
                updateVolumeLabel(newVal.doubleValue());
            }
        });
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
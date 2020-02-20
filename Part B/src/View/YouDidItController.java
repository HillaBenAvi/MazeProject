package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class YouDidItController {

    @FXML
    private javafx.scene.control.Button close_btn;
    private Stage youDidItStage;

    public void openYouDidItStage(){ //call it from MyViewController
        youDidItStage = new Stage();
        youDidItStage.setTitle("You Did It!");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("YouDidIt.fxml").openStream());
            Scene scene = new Scene(root, 670, 450);
            scene.getStylesheets().add(getClass().getResource("YouDidIt.css").toExternalForm());
            youDidItStage.setScene(scene);
            youDidItStage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            youDidItStage.show();
            MyViewController.mediaPlayer.pause();
            MyViewController.startMusic("resources/Music/WeDidIt.mp3");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void closeYouDidItStage(ActionEvent actionEvent){
            MyViewController.mediaPlayer.pause();
            MyViewController.startMusic("resources/Music/DoraOpen.mp3");
            Stage stage = (Stage) close_btn.getScene().getWindow();
            stage.close();

    }

}

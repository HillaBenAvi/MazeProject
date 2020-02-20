package View;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AboutController {

    @FXML
    private javafx.scene.control.Button close_btn;
    private Stage aboutStage;

    public Canvas Us;

    public void openAboutStage() { //call it from MyViewController
        aboutStage = new Stage();
        aboutStage.setTitle("About");

        try {
            Stage stage = new Stage();
            stage.setTitle("About");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("About.fxml").openStream());
            Scene scene = new Scene(root, 700, 500);
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("ViewStyle2.css").toExternalForm());
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeAboutStage(ActionEvent actionEvent) {
        Stage stage = (Stage) close_btn.getScene().getWindow();
        stage.close();
    }
}

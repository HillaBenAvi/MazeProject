package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HelpController {

    @FXML
    private javafx.scene.control.Button close_btn;
    private Stage helpStage;

    public void openHelpStage(){ //call it from MyViewController
        helpStage = new Stage();
        helpStage.setTitle("Help");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Help.fxml").openStream());
            Scene scene = new Scene(root, 670, 450);
            scene.getStylesheets().add(getClass().getResource("ViewStyle2.css").toExternalForm());
            helpStage.setScene(scene);
            helpStage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            helpStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void closeHelpStage(ActionEvent actionEvent){
        Stage stage = (Stage) close_btn.getScene().getWindow();
        stage.close();
    }

}

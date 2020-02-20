package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesController {
    @FXML
    private javafx.scene.control.Button close_btn;
    public javafx.scene.control.Label generateAlgo_lbl;
    public javafx.scene.control.Label searchingAlgo_lb;

    private Stage propStage;

    StringProperty mazeSearchingProp;
    StringProperty mazeGeneratorProp;

    public void openPropertiesStage(){ //call it from MyViewController
        propStage = new Stage();
        propStage.setTitle("Properties");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Properties.fxml").openStream());
            Scene scene = new Scene(root, 670, 450);
            scene.getStylesheets().add(getClass().getResource("ViewStyle2.css").toExternalForm());
            propStage.setScene(scene);
            getProperties();
            propStage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            propStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getProperties (){
        InputStream input = PropertiesController.class.getClassLoader().getResourceAsStream("resources/config.properties");
        Properties prop = new Properties();
        try{

            prop.load(input);
            String mazeGenerator = prop.getProperty("MazeGenerator");
            String mazeSearching = prop.getProperty("SearchingAlgorithm");
            mazeGeneratorProp = new SimpleStringProperty(mazeGenerator);
            mazeSearchingProp = new SimpleStringProperty(mazeSearching);
            generateAlgo_lbl.textProperty().bind(mazeGeneratorProp);
            searchingAlgo_lb.textProperty().bind(mazeSearchingProp);
        }
        catch (Exception e){

        }

    }

    public void closePropertiesStage(ActionEvent actionEvent){
        Stage stage = (Stage) close_btn.getScene().getWindow();
        stage.close();
    }
}

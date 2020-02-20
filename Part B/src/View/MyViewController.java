package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileInputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

public class MyViewController implements IView, Observer {


    public static enum Up {
        Move_character, Generate_maze, Solve_maze ;
    }

    private MyViewModel viewModel;
    private Scene mainScene;
    private Stage mainStage;
    public static MediaPlayer mediaPlayer;

    private HelpController helpController;
    private PropertiesController propertiesController;
    private AboutController aboutController;
    private YouDidItController youDidItController;


    @FXML
    public MazeDisplayer mazeDisplayer;
    public javafx.scene.control.TextField rows_textField;
    public javafx.scene.control.TextField col_textField;
    public javafx.scene.control.Label positionRow_lbl;
    public javafx.scene.control.Label positionCol_lbl;
    public javafx.scene.control.Button GenerateMaze_btn;
    public javafx.scene.control.Button SolveMaze_btn;
    public javafx.scene.control.MenuItem save_menu;
    public javafx.scene.control.Button saveMazeName_btn;
    public javafx.scene.control.TextField mazeName_txtfld;
    public javafx.scene.control.Button music_btn;

    private boolean isGenerate = false;
    private boolean isPlay=true;

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        bindProperties();
    }

    public void initialize(MyViewModel viewModel, Stage stage, Scene scene) {
        this.viewModel = viewModel;
        this.mainScene = scene;
        this.mainStage = stage;
        startMusic("resources/Music/DoraOpen.mp3");
        bindProperties();
        //setResizeEvent(mainScene);
        SolveMaze_btn.setDisable(true);
        save_menu.setDisable(true);
    }

    private void bindProperties() {
        positionRow_lbl.textProperty().bind(viewModel.characterPositionRow);
        positionCol_lbl.textProperty().bind(viewModel.characterPositionCol);
    }

    public void KeyPressed(KeyEvent keyEvent) {
        viewModel.moveCharacter(keyEvent.getCode());
        keyEvent.consume();

    }

    @Override
    public void displayMaze(int[][] maze) {
        mazeDisplayer.setMaze(maze);
        mazeDisplayer.setGoalPosition(viewModel.getGoalPositionRow(), viewModel.getGoalPositionColumn());
        int characterPositionRow = viewModel.getCharacterPositionRow();
        int characterPositionColumn = viewModel.getCharacterPositioncol();
        mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (viewModel == o) {
            if (arg == MyViewModel.Up.Generate_maze || arg == MyViewModel.Up.Solve_maze){
                resetZoomEvent();
                displayMaze(viewModel.getMaze());
                GenerateMaze_btn.setDisable(false);
            }
            else {
                displayMaze(viewModel.getMaze());
                GenerateMaze_btn.setDisable(false);
            }

            if (isGenerate) {
                SolveMaze_btn.setDisable(false);
                save_menu.setDisable(false);
            }
            if (viewModel.finish()) {
                mediaPlayer.pause();
                youDidItController= new YouDidItController();
                youDidItController.openYouDidItStage();
            }
        }

    }

    // complete- resize the maze when we resize the window
    public void setResizeEvent(Scene scene) {
        long width = 0;
        long height = 0;
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                System.out.println("Width: " + newSceneWidth);
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                System.out.println("Height: " + newSceneHeight);
            }
        });
    }

    public void About(ActionEvent actionEvent) {
        aboutController = new AboutController();
        aboutController.openAboutStage();
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        this.mazeDisplayer.requestFocus();
    }

    public void generateMaze() {
        String rows = rows_textField.getText();
        String cols = col_textField.getText();
        if (rows.matches("[0-9]+") && cols.matches("[0-9]+")){
            int heigth = Integer.valueOf(rows);
            int width = Integer.valueOf(cols);
            GenerateMaze_btn.setDisable(true);
            SolveMaze_btn.setDisable(true);
            isGenerate = true;
            viewModel.generateMaze(width, heigth);

        }
        else{
            showAlert("Please enter a valid number");
        }

    }

    public void solveMaze(ActionEvent actionEvent) {
        showAlert("Don't worry Dora! boots will help you to find Swiper!");
        GenerateMaze_btn.setDisable(true);
        SolveMaze_btn.setDisable(true);
        viewModel.solveMaze();
    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void save(ActionEvent actionEvent) {
        if(viewModel.saveMaze()){
            showAlert("Your maze successfully saved!");
        }
        else{
            showAlert("File save cancelled");
        }
    }

    public void loadFile(ActionEvent actionEvent){
       if(viewModel.loadFile()){
           showAlert("Let's play!");
       }
       else{
           showAlert("Something is Wrong!");
       }

    }

    public static void startMusic(String musicFile) {
        javafx.scene.media.Media sound = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });
    }

    public void stopMusic(ActionEvent actionEvent) {
        if(isPlay){
            isPlay=false;
            mediaPlayer.pause();
        }
        else{
            isPlay=true;
            mediaPlayer.play();
        }
    }

    public void help (ActionEvent actionEvent){
        helpController = new HelpController();
        helpController.openHelpStage();
    }

    public void properties (ActionEvent actionEvent){
        propertiesController = new PropertiesController();
        propertiesController.openPropertiesStage();
    }

    public void scrollZoomEvent(ScrollEvent scrollEvent){
        if(scrollEvent.isControlDown()){
            double zoomSize = 1.05;
            double scrollMovement = scrollEvent.getDeltaY();
            if(scrollEvent.getDeltaY()<0){
                zoomSize = 2.0 - zoomSize;
            }
            mazeDisplayer.setScaleX(mazeDisplayer.getScaleX()*zoomSize);
            mazeDisplayer.setScaleY(mazeDisplayer.getScaleY()*zoomSize);

        }
    }

    public void resetZoomEvent(){
        mazeDisplayer.setTranslateX(mazeDisplayer.getParent().getTranslateX());
        mazeDisplayer.setTranslateY(mazeDisplayer.getParent().getTranslateY());
        mazeDisplayer.setScaleX(mazeDisplayer.getParent().getScaleX());
        mazeDisplayer.setScaleY(mazeDisplayer.getParent().getScaleY());
    }

    int prevMazeCordX, prevMazeCordY, prevMouseCordX, prevMouseCordY, diffX, diffY;

    public void mazeDragMousePressed(MouseEvent m)
    {
        prevMazeCordX= (int) mazeDisplayer.getTranslateX();
        prevMazeCordY= (int) mazeDisplayer.getTranslateY();
        prevMouseCordX= (int) m.getSceneX();
        prevMouseCordY= (int) m.getSceneY();
    }

    // set this method on Mouse Drag event for lblDrag

    public void mazeDragMouseDragged(MouseEvent m)
    {
        diffX= (int) (m.getSceneX()- prevMouseCordX);
        diffY= (int) (m.getSceneY()-prevMouseCordY );
        int x = diffX + prevMazeCordX;
        int y = diffY + prevMazeCordY;
        mazeDisplayer.setTranslateX(x);
        mazeDisplayer.setTranslateY(y);
    }

    public void exitApplication (ActionEvent actionEvent){
        showAlert("Thank you for helping Dora to find Swiper! ");
        viewModel.exitApplication();
        mainStage.close();
    }

    public void getFocus(MouseEvent mouseEvent){
        mazeDisplayer.requestFocus();
    }




}



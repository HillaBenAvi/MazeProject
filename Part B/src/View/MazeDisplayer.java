package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.SimpleStyleableStringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MazeDisplayer extends Canvas {

    private int [][] maze;
    private int characterPositionRow =1;
    private int characterPositionColumn = 1;

    private int goalPositionRow;
    private int goalPositionColumn;

    //region Properties
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();
    private StringProperty ImageFileNameSolution = new SimpleStringProperty();
    private StringProperty ImageFileNameMazeBack = new SimpleStringProperty();
    private StringProperty ImageFileNameGoal = new SimpleStringProperty();

    public void setMaze(int[][] maze) {
        this.maze = maze;
        redraw();
    }

    public void setCharacterPosition(int row, int column) {
        characterPositionRow = row;
        characterPositionColumn = column;
        redraw();
    }

    public void setGoalPosition(int row, int column) {
        goalPositionRow = row;
        goalPositionColumn = column;
        redraw();
    }

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    public double minHeight (double Height){
        return 64;
    }

    public double minWidth (double width){
        return 64;
    }

    public double maxHeight (double Height){
        return 1000;
    }

    public double maxWidth (double width){
        return 1000;
    }

    public double prefHeight (double Height){
        return minHeight(Height);
    }

    public double prefWidth (double width){
        return minWidth(width);
    }

    public boolean isResizable (){
        return true;
    }

    public void resize (double width, double height){
        super.setWidth(width);
        super.setHeight(height);
        redraw();
    }

    public void redraw() {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.length;
            double cellWidth = canvasWidth / maze[0].length;

            try {
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
                Image solutionImage = new Image(new FileInputStream(ImageFileNameSolution.get()));
                Image mazeBackImage = new Image(new FileInputStream(ImageFileNameMazeBack.get()));
                Image goalImage = new Image(new FileInputStream(ImageFileNameGoal.get()));


                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());

                //Draw Maze
                for (int i = 0; i < maze.length; i++) {
                    for (int j = 0; j < maze[i].length; j++) {
                        if (maze[i][j] == 1) {
                            gc.fillRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                            gc.drawImage(wallImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                        }
                        else if (maze[i][j] == 2) {
                            gc.fillRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                            gc.drawImage(solutionImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                        }
                        else{
                            gc.fillRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                            gc.drawImage(mazeBackImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                        }

                    }
                }
                 gc.drawImage(characterImage, characterPositionColumn * cellWidth, characterPositionRow * cellHeight, cellWidth, cellHeight);
                 gc.drawImage(goalImage, goalPositionRow * cellWidth, goalPositionColumn* cellHeight, cellWidth, cellHeight);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }

    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);
    }

    public String getImageFileNameSolution() {
        return ImageFileNameSolution.get();
    }

    public void setImageFileNameSolution(String imageFileNameSolution) {
        this.ImageFileNameSolution.set(imageFileNameSolution);
    }

    public String getImageFileNameMazeBack() {
        return ImageFileNameMazeBack.get();
    }

    public void setImageFileNameMazeBack(String imageFileNameMazeBack) {
        this.ImageFileNameMazeBack.set(imageFileNameMazeBack);
    }

    public String getImageFileNameGoal() {
        return ImageFileNameGoal.get();
    }

    public void setImageFileNameGoal(String imageFileNameGoal) {
        this.ImageFileNameGoal.set(imageFileNameGoal);
    }

}



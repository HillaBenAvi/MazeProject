package Model;

import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyModel extends Observable implements IModel {

    public static enum Up {
        Move_character, Generate_maze, Solve_maze ;
    }

    private Server generateServer;
    private Server solveMazeServer;

    private Maze objMaze;
    private int[][] maze;

    private int characterPositionRow;
    private int characterPositionCol;

    private int goalPositionRow;
    private int goalPositionCol;

    public MyModel() {
        generateServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveMazeServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
    }

    @Override
    public void generateMaze(int width, int height) {
        maze = new int[width][height];
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{width, height};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = ((byte[]) fromServer.readObject());
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[compressedMaze.length * 8 + 1];
                        is.read(decompressedMaze);
                        objMaze = new Maze(decompressedMaze);
                        for (int i = 0; i < width; i++) {
                            for (int j = 0; j < height; j++) {
                                maze[i][j] = objMaze.getCellValue(i, j);
                            }
                        }
                        characterPositionCol = objMaze.getStartPosition().getColumnIndex();
                        characterPositionRow = objMaze.getStartPosition().getRowIndex();
                        goalPositionRow = maze[0].length-1;
                        goalPositionCol = maze.length-1;

                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }

                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }

        setChanged();
        notifyObservers( Up.Generate_maze );
    }

    public void solveMaze() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(objMaze);
                        toServer.flush();
                        Solution mazeSolution = (Solution) fromServer.readObject();
                        ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();

                        for (int i = 0; i < mazeSolutionSteps.size(); ++i) {
                            Position pos = (Position) (mazeSolutionSteps.get(i)).getData();
                            maze[pos.getRowIndex()][pos.getColumnIndex()] = 2;
                        }
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }


                }
            });
            client.communicateWithServer();

        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }
        setChanged();
        notifyObservers(Up.Solve_maze);
    }

    @Override
    public void moveCharacter(KeyCode movement) {
        if(objMaze==null){
            return;
        }
        else {
            switch (movement) {
                case UP:
                    if (characterPositionRow - 1 >= 0 && maze[characterPositionRow - 1][characterPositionCol] != 1)
                        characterPositionRow--;
                    break;
                case DOWN:
                    if (characterPositionRow + 1 < maze.length && maze[characterPositionRow + 1][characterPositionCol] != 1)
                        characterPositionRow++;
                    break;
                case RIGHT:
                    if (characterPositionCol + 1 < maze[0].length && maze[characterPositionRow][characterPositionCol + 1] != 1)
                        characterPositionCol++;
                    break;
                case LEFT:
                    if (characterPositionCol - 1 >= 0 && maze[characterPositionRow][characterPositionCol - 1] != 1)
                        characterPositionCol--;
                    break;
                case HOME:
                    characterPositionRow = objMaze.getStartPosition().getRowIndex();
                    characterPositionCol = objMaze.getStartPosition().getColumnIndex();
                    break;
                case NUMPAD2:
                    if (characterPositionRow + 1 < maze.length && maze[characterPositionRow + 1][characterPositionCol] != 1)
                        characterPositionRow++;
                    break;
                case NUMPAD4:
                    if (characterPositionCol - 1 >= 0 && maze[characterPositionRow][characterPositionCol - 1] != 1)
                        characterPositionCol--;
                    break;
                case NUMPAD6:
                    if (characterPositionCol + 1 < maze[0].length && maze[characterPositionRow][characterPositionCol + 1] != 1)
                        characterPositionCol++;
                    break;
                case NUMPAD8:
                    if (characterPositionRow - 1 >= 0 && maze[characterPositionRow - 1][characterPositionCol] != 1)
                        characterPositionRow--;
                    break;
                case NUMPAD1:
                    if (characterPositionRow + 1 < maze.length && maze[characterPositionRow + 1][characterPositionCol - 1] != 1 && characterPositionCol - 1 >= 0) {
                        characterPositionRow++;
                        characterPositionCol--;
                    }
                    break;
                case NUMPAD3:
                    if (characterPositionRow + 1 < maze.length && maze[characterPositionRow + 1][characterPositionCol + 1] != 1 && characterPositionCol + 1 < maze[0].length) {
                        characterPositionRow++;
                        characterPositionCol++;
                    }
                    break;
                case NUMPAD7:
                    if (characterPositionRow - 1 >= 0 && maze[characterPositionRow - 1][characterPositionCol - 1] != 1 && characterPositionCol - 1 >= 0) {
                        characterPositionRow--;
                        characterPositionCol--;
                    }
                    break;
                case NUMPAD9:
                    if (characterPositionRow - 1 >= 0 && maze[characterPositionRow - 1][characterPositionCol + 1] != 1 && characterPositionCol + 1 < maze[0].length) {
                        characterPositionRow--;
                        characterPositionCol++;
                    }
                    break;

                case A:
                    if (characterPositionRow + 1 < maze.length && maze[characterPositionRow + 1][characterPositionCol - 1] != 1 && characterPositionCol - 1 >= 0) {
                        characterPositionRow++;
                        characterPositionCol--;
                    }
                    break;
                case S:
                    if (characterPositionRow + 1 < maze.length && maze[characterPositionRow + 1][characterPositionCol + 1] != 1 && characterPositionCol + 1 < maze[0].length) {
                        characterPositionRow++;
                        characterPositionCol++;
                    }
                    break;
                case Q:
                    if (characterPositionRow - 1 >= 0 && maze[characterPositionRow - 1][characterPositionCol - 1] != 1 && characterPositionCol - 1 >= 0) {
                        characterPositionRow--;
                        characterPositionCol--;
                    }
                    break;
                case W:
                    if (characterPositionRow - 1 >= 0 && maze[characterPositionRow - 1][characterPositionCol + 1] != 1 && characterPositionCol + 1 < maze[0].length) {
                        characterPositionRow--;
                        characterPositionCol++;
                    }
                    break;
                default:
                    try {
                        break;
                    } catch (Exception e) {
                        break;
                    }
            }
            setChanged();
            notifyObservers(Up.Move_character);
        }
    }

    @Override
    public int[][] getMaze() {
        return maze;
    }

    @Override
    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    @Override
    public int getCharacterPositionColumn() {
        return characterPositionCol;
    }

    public int getGoalPositionColumn() {
        return goalPositionCol;
    }

    public int getGoalPositionRow() {
        return goalPositionRow;
    }

    public void startServers() {
        generateServer.start();
        solveMazeServer.start();
    }

    public void stopServers() {
        generateServer.stop();
        solveMazeServer.stop();
        //Server.pool.awaitTermination(2, TimeUnit.SECONDS);
    }

    public boolean finish() {
        if (characterPositionRow == maze.length - 1 && characterPositionCol == maze[0].length - 1) {
            return true;
        }
        return false;
    }

    public void exitApplication() {
        stopServers();

    }

    public boolean saveMaze() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        fileChooser.setInitialFileName("MyMaze.txt");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt Files", "*.txt"));
        Stage savedStage = new Stage();
        File savedFile = fileChooser.showSaveDialog(savedStage);
        boolean save = false;
        System.out.println(savedFile==null);
        if (savedFile!=null) {
            if (!savedFile.exists()) {
                try {
                    saveFileRoutine(savedFile);
                    save = true;
                } catch (Exception e) {
                    save = false;
                }
            } else { //file exists
                save = false;
            }
        }
        return save;
    }

    private void saveFileRoutine(File file){
        try{
            // Creates a new file and writes the txtArea contents into it
            file.createNewFile();
            FileOutputStream fileOutput = new FileOutputStream(file);
            ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
            objectOutput.writeObject(objMaze);
            objectOutput.writeObject(characterPositionRow);
            objectOutput.writeObject(characterPositionCol);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean loadFile(){
        FileChooser fileChooser = new FileChooser();
        Stage loadStage = new Stage();
        File selectedFile = fileChooser.showOpenDialog(loadStage);
        if (selectedFile != null) {
            try {
                FileInputStream fileInput = new FileInputStream(selectedFile);
                ObjectInputStream objectInput = new ObjectInputStream(fileInput);
                objMaze = (Maze) objectInput.readObject();
                characterPositionRow = (int)objectInput.readObject();
                characterPositionCol = (int)objectInput.readObject();
                maze = new int [objMaze.getRows()][objMaze.getColumns()];
                for (int i = 0; i < objMaze.getRows(); i++) {
                    for (int j = 0; j < objMaze.getColumns(); j++) {
                        maze[i][j] = objMaze.getCellValue(i, j);
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }
        else {
            return false;

        }
        setChanged();
        notifyObservers(Up.Generate_maze);
        return true;
    }
}



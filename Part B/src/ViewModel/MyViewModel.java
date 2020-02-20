package ViewModel;

import Model.IModel;
import Model.MyModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    public static enum Up {
        Move_character, Generate_maze, Solve_maze ;
    }

    private IModel model;

    private int characterPositionRowIndex;
    private int characterPositionColumnIndex;

    public StringProperty characterPositionRow = new SimpleStringProperty("1");
    public StringProperty characterPositionCol = new SimpleStringProperty("1");

    public MyViewModel(IModel model) {
        this.model = model;
    }

    @Override
    public void update(Observable o, Object arg) {
        if ( o == model){
            characterPositionRowIndex = model.getCharacterPositionRow();
            characterPositionRow.set(characterPositionRowIndex+"");
            characterPositionColumnIndex = model.getCharacterPositionColumn();
            characterPositionCol.set(characterPositionColumnIndex+"");
            if (arg == MyModel.Up.Generate_maze){
                setChanged();
                notifyObservers(Up.Generate_maze);
            }
            else if (arg == MyModel.Up.Solve_maze){
                setChanged();
                notifyObservers(Up.Solve_maze);
            }
            else if (arg == MyModel.Up.Move_character){
                setChanged();
                notifyObservers(Up.Move_character);
            }

        }
    }

    public void generateMaze(int width, int height){
        model.generateMaze(width,height);
    }

    public void solveMaze(){
        model.solveMaze();
    }

    public void moveCharacter(KeyCode movement){
        model.moveCharacter(movement);
    }

    public int[][] getMaze(){
        return model.getMaze();
    }

    public int getGoalPositionRow (){
        return model.getGoalPositionRow();
    }

    public int getGoalPositionColumn (){
        return model.getGoalPositionColumn();
    }

    public int getCharacterPositioncol (){
        return characterPositionColumnIndex;
    }
    public int getCharacterPositionRow (){
        return characterPositionRowIndex;
    }

    public boolean finish () {return model.finish();}

    public void exitApplication(){
        model.exitApplication();
    }

    public boolean saveMaze (){
        return model.saveMaze();
    }

    public boolean loadFile(){
        return model.loadFile();
    }

}

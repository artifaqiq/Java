package srun;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SRunner extends Application implements GameListener {
  private Pane mRoot;

  @Override
  public void start(Stage theStage) {
    mRoot = new Pane();
    Scene scene = new Scene(mRoot);
    showMenu();
    theStage.setScene(scene);
    theStage.setTitle("S-Runner");
    theStage.setResizable(false);
    theStage.show();
  }

  @Override
  public void startGame(int countArea, boolean autoMode, boolean mySelfMode) {
    mRoot.getChildren().clear();
    if (mySelfMode && autoMode) {
      mySelfMode = false;
    }
    GameLauncher launcher = new GameLauncher(countArea, autoMode, mySelfMode);
    launcher.setListener(this);
    mRoot.getChildren().add(launcher);
  }

  @Override
  public void showMenu() {
    mRoot.getChildren().clear();
    Menu menu = new Menu(this);
    mRoot.getChildren().add(menu);
  }

  @Override
  public void replayLastGame() {
    mRoot.getChildren().clear();
    GameLauncher launcher = new GameLauncher(
        ReplayIO.getReplayDir() + File.separator + "game" + (ReplayIO.countGames() - 1));
    launcher.setListener(this);
    mRoot.getChildren().add(launcher);
  }

  @Override
  public void replayGame(int number) {
    mRoot.getChildren().clear();
    GameLauncher launcher =
        new GameLauncher(ReplayIO.getReplayDir() + File.separator + "game" + number);
    launcher.setListener(this);
    mRoot.getChildren().add(launcher);
  }

  public static void main(String[] args) {
    launch(args);
    System.out.println(Statistics.getStat());
    System.out.println(ReplayIO.countGames());
  }

}



import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SRunner extends Application implements GameListener {
  private Pane mRoot;
  private static final int WINDOW_WIDTH= 1600, WINDOW_HEIGHT = 800;

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
  public void startGame(int countArea, boolean autoMode) {
    mRoot.getChildren().clear();
    GameLauncher launcher = new GameLauncher(WINDOW_WIDTH, WINDOW_HEIGHT, countArea, autoMode);
    launcher.setListener(this);
    mRoot.getChildren().add(launcher);
  }

  @Override
  public void showMenu() {
    mRoot.getChildren().clear();
    Menu menu = new Menu();
    menu.setListener(this);
    mRoot.getChildren().add(menu);
  }

  public static void main(String[] args) {
    launch(args);
  }
}

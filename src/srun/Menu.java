package srun;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class Menu extends BorderPane {
  protected static final long UPDATE_RUNNER_NANO_FREQ = 30000000;
  protected static final int RUNNER_HEIGHT = 200, RUNNER_WIDTH = 50;
  protected static final int BUTTON_WIDTH = 145, BUTTON_HEIGHT = 67;
  static final int CANVAS_WIDTH = 400, CANVAS_HEIGHT = 710;
  protected static final int BUTTON_SPACING = 5;
  private static final int COUNT_IMAGES = 10;
  private static final int RUNNER_POS_X = 100, RUNNER_POS_Y = 45;

  private Canvas mRunnerCanvas;
  private GameListener mListener;

  private int mItButtons;

  private ArrayList<Button> mButtonsRun = new ArrayList<>();

  public Menu(GameListener listener) {
    mListener = listener;
    getStylesheets().add(Menu.class.getResource("application.css").toExternalForm());

    HBox buttonsControlHbox = new HBox();
    VBox buttonsRunVbox = new VBox();

    setId("background");

    CheckBox chBoxAutoMode = new CheckBox("AUTO MODE");
    chBoxAutoMode.setId("check");
    chBoxAutoMode.setPrefWidth(BUTTON_WIDTH * 2);
    chBoxAutoMode.setAlignment(Pos.CENTER);

    CheckBox chMySelfMode = new CheckBox("DEATHMATCH");
    chMySelfMode.setId("check2");
    chMySelfMode.setPrefWidth(BUTTON_WIDTH * 2);
    chMySelfMode.setAlignment(Pos.CENTER);

    mButtonsRun.add(new Button("       EASY\nhigh score: " + ScoreIO.findHighScore(false, 1)));
    mButtonsRun.get(0).setId("easy");
    mButtonsRun.add(new Button("    NORMAL\nhigh score: " + ScoreIO.findHighScore(false, 2)));
    mButtonsRun.get(1).setId("normal");
    mButtonsRun.add(new Button("NIGHTMARE\nhigh score: " + ScoreIO.findHighScore(false, 3)));
    mButtonsRun.get(2).setId("nightmare");
    mButtonsRun.add(new Button("       HELL\nhigh score: " + ScoreIO.findHighScore(false, 4)));
    mButtonsRun.get(3).setId("hell");
    mButtonsRun
        .forEach(mButtonsRun -> mButtonsRun.setPrefSize(BUTTON_WIDTH * 2, BUTTON_HEIGHT * 2));
    mButtonsRun.forEach(mButtonsRun -> mButtonsRun.setAlignment(Pos.BASELINE_CENTER));

    buttonsRunVbox.getChildren().addAll(mButtonsRun);
    buttonsRunVbox.getChildren().addAll(chBoxAutoMode, chMySelfMode);
    buttonsRunVbox.setSpacing(BUTTON_SPACING);

    Button buttonExit = new Button("EXIT");
    buttonExit.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
    buttonExit.setId("dark-blue");

    Button buttonReplay = new Button("REPLAY LAST GAME");
    buttonReplay.setPrefSize(BUTTON_WIDTH * 2, BUTTON_HEIGHT);
    buttonReplay.setId("dark-blue");

    Button buttonStat = new Button("STATISTIC");
    buttonStat.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
    buttonStat.setId("dark-blue");

    buttonsControlHbox.setSpacing(BUTTON_SPACING);
    buttonsControlHbox.getChildren().addAll(buttonExit, buttonReplay);

    setBottom(buttonsControlHbox);

    setLeft(buttonsRunVbox);
    setPadding(new Insets(BUTTON_SPACING, BUTTON_SPACING, BUTTON_SPACING, BUTTON_SPACING));

    ArrayList<ImageView> runnerImages = new ArrayList<>();
    for (int i = 0; i < COUNT_IMAGES; i++) {
      runnerImages.add(new ImageView(new Image("/res/Idle__00" + i + ".png")));
      runnerImages.get(i).setFitWidth(RUNNER_WIDTH);
      runnerImages.get(i).setFitHeight(RUNNER_HEIGHT);
    }

    mRunnerCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
    setCenter(mRunnerCanvas);
   
    AnimationTimer animationTimer = new AnimationTimer() {
      private int mIterImages = 0;
      private long mLastNanoTime = System.nanoTime();

      @Override
      public void handle(long now) {
        if (now - mLastNanoTime > UPDATE_RUNNER_NANO_FREQ) {
          mIterImages++;
          if (mIterImages == COUNT_IMAGES) {
            mIterImages = 0;
          }
          mRunnerCanvas.getGraphicsContext2D().clearRect(0, 0, mRunnerCanvas.getWidth(),
              mRunnerCanvas.getHeight());
          mRunnerCanvas.getGraphicsContext2D().drawImage(runnerImages.get(mIterImages).getImage(),
              RUNNER_POS_X, RUNNER_POS_Y);
          mLastNanoTime = System.nanoTime();
        }
      }
    };

    animationTimer.start();
    buttonExit.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        System.exit(0);
      }
    });

    for (mItButtons = 0; mItButtons < mButtonsRun.size(); mItButtons++) {
      mButtonsRun.get(mItButtons).setOnAction(new EventHandler<ActionEvent>() {
        private int countArea = Menu.this.mItButtons + 1;

        @Override
        public void handle(ActionEvent event) {
          animationTimer.stop();
          mListener.startGame(countArea, chBoxAutoMode.isSelected(), chMySelfMode.isSelected());
        }
      });
    }

    buttonReplay.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        animationTimer.stop();
        mListener.replayLastGame();
      }
    });
    //
    chBoxAutoMode.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        Menu.this.mButtonsRun.get(0).setText(
            "  	EASY\nhigh score: " + ScoreIO.findHighScore(chBoxAutoMode.isSelected(), 1));
        Menu.this.mButtonsRun.get(1).setText(
            "    NORMAL\nhigh score: " + ScoreIO.findHighScore(chBoxAutoMode.isSelected(), 2));
        Menu.this.mButtonsRun.get(2).setText(
            "NIGHTMARE\nhigh score: " + ScoreIO.findHighScore(chBoxAutoMode.isSelected(), 3));
        Menu.this.mButtonsRun.get(3).setText(
            "       HELL\nhigh score: " + ScoreIO.findHighScore(chBoxAutoMode.isSelected(), 4));
      }
    });

    setRight(new Statistic(listener));
  }
}


class Statistic extends VBox {
  private static final int WIDTH = 900;
  private static final int COUNT_RPOPERTYS = 5;
  private static final int WIDTH_LABEL = 125, HEIGHT_LABEL = 100;
  private static final double FONT_SIZE = 35f;

  private GameInfo[] mInfo = new GameInfo[ReplayIO.countGames()];
  private GameListener mListener;
  private boolean[] mSortFlags = new boolean[COUNT_RPOPERTYS];

  public Statistic(GameListener listener) {
    mListener = listener;
    for (int i = 0; i < mInfo.length; i++) {
      mInfo[i] = new GameInfo(i);
    }

    generatePropertysView();
    generateTable();
    generateTotalStat();

  }

  private void generatePropertysView() {
    HBox propertys = new HBox();
    Label[] labels = new Label[COUNT_RPOPERTYS];
    labels[0] = new Label("№");
    labels[1] = new Label("Score");
    labels[2] = new Label("Level");
    labels[3] = new Label("Mode");
    labels[4] = new Label("Jumps");

    labels[0].setOnMouseClicked((event) -> {
      mSortFlags[0] = mSortFlags[0] == true ? false : true;
      sortByNumbers(mSortFlags[0]);
    });

    labels[1].setOnMouseClicked((event) -> {
      mSortFlags[1] = !mSortFlags[1];
      sortByScore(mSortFlags[1]);
    });

    labels[2].setOnMouseClicked((event) -> {
      mSortFlags[2] = !mSortFlags[2];
      sortByLevel(mSortFlags[2]);
    });

    labels[3].setOnMouseClicked((event) -> {
      mSortFlags[3] = !mSortFlags[3];
      sortByAuto(mSortFlags[3]);
    });

    labels[4].setOnMouseClicked((event) -> {
      mSortFlags[4] = !mSortFlags[4];
      sortByJumps(mSortFlags[4]);
    });

    for (Label temp : labels) {
      temp.setFont(new Font(FONT_SIZE));
      temp.setAlignment(Pos.CENTER);
      temp.setPrefSize(WIDTH_LABEL, HEIGHT_LABEL);
    }
    propertys.getChildren().addAll(labels);

    super.getChildren().add(propertys);
  }

  private void generateTable() {

    VBox vbox = new VBox();
    vbox.setId("table");
    for (int i = 0; i < ReplayIO.countGames(); i++) {
      vbox.getChildren().add(new GameInfoView(mInfo[i], mListener));
    }
    vbox.setAlignment(Pos.CENTER);
    vbox.setSpacing(10);
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setContent(vbox);
    scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
    scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
    
    setPrefSize(WIDTH, Menu.CANVAS_HEIGHT);
    super.getChildren().clear();
    generatePropertysView();
    super.getChildren().add(scrollPane);

  }

  private void generateTotalStat() {
    Game gamesStat = Statistics.getStat();
    HBox hbox = new HBox();
    Label[] labels = new Label[COUNT_RPOPERTYS];
    labels[0] = new Label(new Integer(ReplayIO.countGames()).toString());
    labels[1] = new Label(new Integer(gamesStat.points()).toString());
    labels[2] = new Label();
    labels[3] = new Label();
    labels[4] = new Label(new Integer(gamesStat.countJump()).toString());
    for (Label temp : labels) {
      temp.setFont(new Font((int)(FONT_SIZE + 10)));
      temp.setAlignment(Pos.CENTER);
      temp.setPrefSize(WIDTH_LABEL, HEIGHT_LABEL);
    }
    hbox.getChildren().addAll(labels);
    super.getChildren().addAll(hbox);

  }

  private void sortByNumbers(boolean normalOrder) {
    long time0 = System.nanoTime();
    Arrays.sort(mInfo.clone(), new GameInfo.NumberComp());
    System.err.println("array[" + mInfo.length + "] :");
    System.out.println("Java: Arrays.sort()    ::: " + (System.nanoTime() - time0) + "  nsec");
    time0 = System.nanoTime();
    SortGames.qSortCstyle(mInfo, new SortGames.NumberComp());
    System.out.println("Scala:                 ::: " + (System.nanoTime() - time0) + "  nsec");
    if (normalOrder == false) {
      reverseArray(mInfo);
    }
    generateTable();
    generateTotalStat();

  }

  private void sortByScore(boolean normalOrder) {
    long time0 = System.nanoTime();
    Arrays.sort(mInfo.clone(), new GameInfo.ScoreComp());
    System.err.println("array[" + mInfo.length + "] :");
    System.out.println("Java: Arrays.sort()    ::: " + (System.nanoTime() - time0) + "  nsec");
    time0 = System.nanoTime();
    SortGames.qSortCstyle(mInfo, new SortGames.ScoreComp());
    System.out.println("Scala:                 ::: " + (System.nanoTime() - time0) + "  nsec");
    if (normalOrder == false) {
      reverseArray(mInfo);
    }
    generateTable();
    generateTotalStat();

  }

  private void sortByAuto(boolean normalOrder) {
    long time0 = System.nanoTime();
    Arrays.sort(mInfo.clone(), new GameInfo.AutoComp());
    System.err.println("array[" + mInfo.length + "] :");
    System.out.println("Java: Arrays.sort()    ::: " + (System.nanoTime() - time0) + "  nsec");
    time0 = System.nanoTime();
    SortGames.qSortCstyle(mInfo, new SortGames.ModeComp());
    System.out.println("Scala:                 ::: " + (System.nanoTime() - time0) + "  nsec");
    if (normalOrder == false) {
      reverseArray(mInfo);
    }
    generateTable();
    generateTotalStat();

  }

  private void sortByJumps(boolean normalOrder) {
    System.err.println("array[" + mInfo.length + "] :");
    Arrays.sort(mInfo.clone(), new GameInfo.JumpComp());
    long time0 = System.nanoTime();
    System.out.println("Java: Arrays.sort()    ::: " + (System.nanoTime() - time0) + "  nsec");
    time0 = System.nanoTime();
    SortGames.qSortCstyle(mInfo, new SortGames.JumpComp());
    System.out.println("Scala:                 ::: " + (System.nanoTime() - time0) + "  nsec");
    if (normalOrder == false) {
      reverseArray(mInfo);
    }
    generateTable();
    generateTotalStat();

  }

  private void sortByLevel(boolean normalOrder) {
    long time0 = System.nanoTime();
    Arrays.sort(mInfo.clone(), new GameInfo.AreaComp());
    System.err.println("\narray[" + mInfo.length + "] :");
    System.out.println("Java: Arrays.sort()    ::: " + (System.nanoTime() - time0) + "  nsec");
    time0 = System.nanoTime();
    SortGames.qSortCstyle(mInfo, new SortGames.LevelComp());
    System.out.println("Scala:                 ::: " + (System.nanoTime() - time0) + "  nsec");
    if (normalOrder == false) {
      reverseArray(mInfo);
    }
    generateTable();
    generateTotalStat();

  }

  private void reverseArray(GameInfo[] array) {
    for (int start = 0, end = array.length - 1; start <= end; start++, end--) {
      GameInfo aux = array[start];
      array[start] = array[end];
      array[end] = aux;
    }
  }

}


class GameInfoView extends HBox {
  private static final int WIDTH_LABEL = 125, HEIGHT_LABEL = 100;
  private static final double FONT_SIZE = 35f;

  public GameInfoView(GameInfo info, GameListener listener) {
    getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());

    ArrayList<Labeled> labels = new ArrayList<>();
    labels.add(new Label(new Integer(info.getNumberGame() + 1).toString()));
    labels.add(new Label(new Integer(info.getScore()).toString()));
    labels.add(new Label(levelToString(info.getCountArea())));
    labels.add(new Label(info.isAutoMode() == true ? "AUTO" : "PLAYER"));
    labels.add(new Label(new Integer(info.getCountJump()).toString()));
    labels.add(new Button("Replay"));

    super.getChildren().addAll(labels);

    for (Labeled temp : labels) {
      temp.setFont(new Font(FONT_SIZE));
      temp.setAlignment(Pos.CENTER);
      temp.setPrefSize(WIDTH_LABEL, HEIGHT_LABEL);
    }
    switch (info.getCountArea()) {
      case 1:
        labels.get(5).setId("easy");
        break;
      case 2:
        labels.get(5).setId("normal");
        break;
      case 3:
        labels.get(5).setId("nightmare");
        break;
      default:
        labels.get(5).setId("hell");
        break;
    }
    labels.get(labels.size() - 1).setPrefSize(WIDTH_LABEL * 2, HEIGHT_LABEL);
    labels.get(2).setFont(new Font(FONT_SIZE / 1.5));
    labels.get(3).setFont(new Font(FONT_SIZE / 1.5));
    Button run = (Button) labels.get(labels.size() - 1);
    run.setPadding(new Insets(40, 40, 40, 40));
    run.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        listener.replayGame(info.getNumberGame());
      }
    });
  }

  private String levelToString(int countArea) {
    switch (countArea) {
      case 1:
        return "EASY";
      case 2:
        return "NORMAL";
      case 3:
        return "NIGHTMARE";
      case 4:
        return "HELL";
      default:
        return null;
    }
  }

}

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Menu extends BorderPane {
  protected static final long UPDATE_RUNNER_NANO_FREQ = 30000000;
  protected static final int RUNNER_HEIGHT = 200, RUNNER_WIDTH = 50;
  protected static final int BUTTON_WIDTH = 145, BUTTON_HEIGHT = 50;
  protected static final int CANVAS_WIDTH = 1270, CANVAS_HEIGHT = 730;
  protected static final int BUTTON_SPACING = 5;
  private static final int COUNT_IMAGES = 10;
  private static final int RUNNER_POS_X = 100, RUNNER_POS_Y = 45;
  
  private Canvas mRunnerCanvas;
  private Color backgroundColor = Color.AZURE;
  private GameListener mListener;

  private int mItButtons;
  
  public Menu() {
    getStylesheets().add(Menu.class.getResource("application.css").toExternalForm());

    HBox buttonsControlHbox = new HBox();
    VBox buttonsRunVbox = new VBox();

    setBackground(
        new Background(new BackgroundFill(backgroundColor, new CornerRadii(0.0), new Insets(0.0))));

    CheckBox autoMode = new CheckBox("AUTO MODE");
    autoMode.setId("check");

    ArrayList<Button> buttonsRun = new ArrayList<>();
    buttonsRun.add(new Button("EASY"));
    buttonsRun.get(0).setId("easy");
    buttonsRun.add(new Button("NORMAL"));
    buttonsRun.get(1).setId("normal");
    buttonsRun.add(new Button("NIGHTMARE"));
    buttonsRun.get(2).setId("nightmare");
    buttonsRun.add(new Button("HELL"));
    buttonsRun.get(3).setId("hell");
    buttonsRun.forEach(mButtonsRun -> mButtonsRun.setPrefSize(BUTTON_WIDTH * 2, BUTTON_HEIGHT * 2));
    buttonsRunVbox.getChildren().add(autoMode);
    buttonsRunVbox.getChildren().addAll(buttonsRun);
    buttonsRunVbox.setSpacing(BUTTON_SPACING);

    Button buttonExit = new Button("EXIT");
    buttonExit.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
    buttonExit.setId("dark-blue");

    Button buttonViewStatistic = new Button("STATISTIC");
    buttonViewStatistic.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
    buttonViewStatistic.setId("dark-blue");

    buttonsControlHbox.setSpacing(BUTTON_SPACING);
    buttonsControlHbox.getChildren().addAll(buttonExit);

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
    setRight(mRunnerCanvas);
    
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
    
    for(mItButtons = 0; mItButtons < buttonsRun.size(); mItButtons++) {
      buttonsRun.get(mItButtons).setOnAction(new EventHandler<ActionEvent>() {
        private int countArea = Menu.this.mItButtons + 1;
      
        @Override
        public void handle(ActionEvent event) {
          animationTimer.stop();
          mListener.startGame(countArea, autoMode.isSelected());
        }
      });
    }
  }

  public void setListener(GameListener listener) {
    mListener = listener;
  }
}

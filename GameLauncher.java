import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GameLauncher extends Canvas implements GameAreaListener {
  private Integer mScore = new Integer(0);
  private int mWidth, mHeight;
  private int mCountArea;
  private ArrayList<GameArea> mAreas;
  private GameListener mListener;
  private boolean mPause = false;
  private boolean mAuto;

  public GameLauncher(int width, int height, int countArea, boolean auto) {
    super(width, height);
    mWidth = width > 0 ? width : 1600;
    mAuto = auto;
    mHeight = height > 0 ? height : 800;
    mCountArea = countArea >= 1 ? countArea : 1;
    mAreas = new ArrayList<>(mCountArea);
    for (int i = 0; i < this.mCountArea; i++) {
      mAreas.add(new GameArea((Canvas) this, 0, mHeight * i / this.mCountArea, mWidth,
          mHeight / this.mCountArea, countArea, mAuto));
      mAreas.get(i).setBackgroundFill(new Color(new Random().nextDouble(),
          new Random().nextDouble(), new Random().nextDouble(), new Random().nextDouble()));
      mAreas.get(i).setListener(this);
    }
    mAreas.get(0).setScore(0);
    getGraphicsContext2D().setFont(new Font(45));

    Executor exec = Executors.newFixedThreadPool(mCountArea);
    for (int i = 0; i < mCountArea; i++) {
      exec.execute(new Thread(mAreas.get(i)));
    }

    mAreas.get(0).setScore(mScore);
    Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

    this.setFocusTraversable(true);
    this.setOnKeyPressed(new EventHandler<KeyEvent>() {
      public void handle(KeyEvent e) {
        if (e.getCode() == KeyCode.DIGIT1 && mCountArea >= 1 && mAuto == false) {
          System.out.println("jump");
          mAreas.get(0).jump();
        }
        if (e.getCode() == KeyCode.DIGIT2 && mCountArea >= 2 && mAuto == false) {
          mAreas.get(1).jump();
        }
        if (e.getCode() == KeyCode.DIGIT3 && mCountArea >= 3 && mAuto == false) {
          mAreas.get(2).jump();
        }
        if (e.getCode() == KeyCode.DIGIT4 && mCountArea >= 4 && mAuto == false) {
          mAreas.get(3).jump();
        }
        if (e.getCode() == KeyCode.ESCAPE) {
          if (mPause == false) {
            for (GameArea temp : mAreas) {
              temp.stopAnimation();
            }
            mPause = true;
          } else if (mPause == true) {
            for (GameArea temp : mAreas) {
              temp.startAnimation();
            }
            mPause = false;
          }
        }

      }
    });

  }

  @Override
  public void dead() {
    System.out.println("GameLauncher.dead()");
    for (GameArea temp : mAreas) {
      temp.stopAnimation();
    }
    new AnimationTimer() {
      private long startNanoTime = System.nanoTime();

      @Override
      public void handle(long now) {
        getGraphicsContext2D().setFill(Color.RED);
        getGraphicsContext2D().fillText("You are LOOOOSER. Score = " + mScore, 400, 400);
        if (now - startNanoTime >= 1_500_000_000l) {
          this.stop();
          mListener.showMenu();
        }
      }
    }.start();
  }

  @Override
  public void incScore() {
    mAreas.get(0).setScore(++mScore);
    for (GameArea temp : mAreas) {
      temp.incSpeed();
    }
  }

  public void setListener(GameListener listener) {
    mListener = listener;
  }

}

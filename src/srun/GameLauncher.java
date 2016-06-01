package srun;

import java.io.File;
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
import scala.reflect.internal.util.Statistics;

public class GameLauncher extends Canvas implements GameAreaListener {
  private Integer mScore = 0;
  private int mCountArea;
  private ArrayList<GameArea> mAreas;
  private transient GameListener mListener;
  private boolean mAuto;
  private boolean mReplay = false;
  private boolean mMySelf = false;

  private static final long LOSE_NANO_SLEEP = 1_500_000_000l;

  private static final int STANDART_WIDTH = 1600, STANDART_HEIGHT = 800;

  public GameLauncher(int countArea, boolean auto, boolean mySelf) {
    super(STANDART_WIDTH, STANDART_WIDTH);
    mAuto = auto;
    mMySelf = mySelf;
    mCountArea = countArea >= 1 ? countArea : 1;
    mAreas = new ArrayList<>(mCountArea);
    getGraphicsContext2D().setFont(new Font(45));

    String replayDirPath = null;
    if (mySelf == false) {
      replayDirPath = ReplayIO.createFiles(countArea);
    }
    for (int i = 0; i < this.mCountArea; i++) {
      mAreas.add(new GameArea((Canvas) this, 0, STANDART_HEIGHT * i / this.mCountArea,
          STANDART_WIDTH, STANDART_HEIGHT / this.mCountArea, countArea, mAuto,
          replayDirPath + File.separator + "replay" + i + ".txt"));
      mAreas.get(i).setBackgroundFill(new Color(new Random().nextDouble(),
          new Random().nextDouble(), new Random().nextDouble(), new Random().nextDouble()));
      mAreas.get(i).setListener(this);
      mAreas.get(i).setMySelf(mMySelf);
      if (mMySelf == true) {
        mAreas.get(i).setScore(mScore);
      }
    }

    Executor exec = Executors.newFixedThreadPool(mCountArea);
    for (int i = 0; i < mCountArea; i++) {
      Thread thread = new Thread(mAreas.get(i));
      exec.execute(thread);
    }

    mAreas.get(0).setScore(mScore);
    Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

    this.setFocusTraversable(true);
    this.setOnKeyPressed(new EventHandler<KeyEvent>() {
      public void handle(KeyEvent e) {
        if (e.getCode() == KeyCode.DIGIT1 && mCountArea >= 1 && mAuto == false) {
          mAreas.get(0).jump();
        }
        if (e.getCode() == KeyCode.DIGIT4 && mCountArea >= 2 && mAuto == false) {
          mAreas.get(1).jump();
        }
        if (e.getCode() == KeyCode.V && mCountArea >= 3 && mAuto == false) {
          mAreas.get(2).jump();
        }
        if (e.getCode() == KeyCode.M && mCountArea >= 4 && mAuto == false) {
          mAreas.get(3).jump();
        }
      }
    });
  }


  public GameLauncher(String replayFilePath) {
    super(STANDART_WIDTH, STANDART_WIDTH);
    mReplay = true;
    mAreas = new ArrayList<>(mCountArea);
    getGraphicsContext2D().setFont(new Font(45));

    mCountArea = 0;
    File saveDir = new File(replayFilePath);
    System.out.println(replayFilePath);
    File[] saveFiles = saveDir.listFiles();
    for (File temp : saveFiles) {
      if (temp.getName().contains("replay")) {
        mCountArea++;
      }
    }

    for (int i = 0; i < this.mCountArea; i++) {
      mAreas.add(new GameArea((Canvas) this, 0, STANDART_HEIGHT * i / this.mCountArea,
          STANDART_WIDTH, STANDART_HEIGHT / this.mCountArea, mCountArea,
          replayFilePath + File.separator + "replay" + i + ".txt"));
      mAreas.get(i).setBackgroundFill(new Color(new Random().nextDouble(),
          new Random().nextDouble(), new Random().nextDouble(), new Random().nextDouble()));
      mAreas.get(i).setListener(this);
    }
    Executor exec = Executors.newFixedThreadPool(mCountArea);
    for (int i = 0; i < mCountArea; i++) {
      Thread thread = new Thread(mAreas.get(i));
      thread.setPriority(Thread.MAX_PRIORITY);
      exec.execute(thread);
    }

    mAreas.get(0).setScore(mScore);
    Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
  }

  private int mCountAreaDead = 0;

  @Override
  public void dead() {
    if (++mCountAreaDead == mCountArea || mMySelf == false) {
      for (GameArea temp : mAreas) {
        temp.endGame();
        new AnimationTimer() {
          private long startNanoTime = System.nanoTime();
          private int messagePosY;
          private static final int MESSAGE_OFFSET_Y = 100;

          {
            messagePosY =
                (int) (GameLauncher.this.mCountArea % 2 == 1 ? GameLauncher.STANDART_HEIGHT / 2
                    : GameLauncher.this.mAreas.get(0).mAreaPosY + MESSAGE_OFFSET_Y);
          }

          @Override
          public void handle(long now) {
            getGraphicsContext2D().setFill(Color.DARKRED);
            getGraphicsContext2D().setFont(new Font(96));
            if (mMySelf == false) {
              getGraphicsContext2D().fillText("You lose. Score: " + mScore,
                  GameLauncher.this.getWidth() / 4, messagePosY, GameLauncher.this.getWidth() / 2);
            }
            if (now - startNanoTime >= LOSE_NANO_SLEEP) {
              this.stop();
              mListener.showMenu();
            }
          }
        }.start();
        if (mReplay == false) {
          ScoreIO.write(mCountArea, mAuto, mScore);
        }
      }
    }

  }

  @Override
  synchronized public void incScore(boolean incPoints) {

    if (incPoints) {
      mScore++;
      if (mScore <= ScoreIO.getLastScore() && mReplay == true) {
        mAreas.get(0).setScore(mScore);
      } else {
        mAreas.get(0).setScore(mScore);
      }
      for (int i = 1; i < mCountArea; i++) {
        mAreas.get(i).setScore(-mScore);
      }
    }
    for (GameArea temp : mAreas) {
      temp.incSpeed();
    }

  }

  public void setListener(GameListener listener) {
    mListener = listener;
  }
}

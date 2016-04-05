import java.util.ArrayList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * The area contains a runner and a few walls. \ Implements interface Runnable, so to start the game
 * you need to start a thread
 */
public class GameArea implements Runnable {
  /** Listener interface used for communication with the flow of the other classes of */
  protected GameAreaListener mAreaListener;
  protected static final int GROUND_HEIGHT = 10;
  protected static final int START_SPEED = 500;
  protected static final int WALLS_COUNT = 3;
  protected static final int MIN_WIDTH_WALL = 40, MAX_WIDTH_WALL = 80;
  protected static final int MIN_DISTANCE_BETWEEN_WALLS = 800, MAX_DISTANCE_BETWEEN_WALLS = 1600;
  
  private static final int AUTO_MODE_JUMP_POS = 130;
  private static final float SPEED_UP_BY_SCORE = 0.05f;
  private static final int SCORE_TEXT_WIDHT = 200;
  private static final int SCORE_TEXT_HEIGHT = 50;
  private static final int RUNNER_POS_X = 40;
  private static final float RUNNER_SIZE_SCALE = 1.58f;

  protected Runner mRunner;
  protected ArrayList<Wall> mWalls = new ArrayList<Wall>(GameArea.WALLS_COUNT);

  protected Canvas mCanvas;
  private Color mBackgroundFill = Color.WHITE;

  private int mScore = -1;
  private boolean mAuto;

  protected int mMinHeightWall, mMaxHeightWall;
  protected int mAreaPosX, mAreaPosY;
  protected int mAreaWidth, mAreaHeight;
  protected int mSpeed = GameArea.START_SPEED;

  protected int mFirstWall = 0;
  private long mLastNanoTime;

  private AnimationTimer mGameLoop = new AnimationTimer() {
    @Override
    public void handle(long now) {
      double deltaTime = (double) (now - mLastNanoTime) / Math.pow(10.0f, 9.0f);
      GameArea.this.update(deltaTime);
      GameArea.this.render();
      mLastNanoTime = System.nanoTime();
    }
  };

  public GameArea(Canvas canvas, int posX, int posY, int width, int heigth, int countArea,
      boolean auto) {
    mAreaPosX = posX;
    mAreaPosY = posY;
    mAreaWidth = width;
    mAreaHeight = heigth;
    mCanvas = canvas;
    mAuto = auto;
    mMinHeightWall = mAreaHeight / 8;
    mMaxHeightWall = mAreaHeight / 5;

    generateRunner(countArea);
    generateRandomWalls();
  }

  public void update(double deltaTime) {
    mRunner.update(deltaTime);
    for (int i = 0; i < GameArea.WALLS_COUNT; i++) {
      mWalls.get(i).update(deltaTime);
    }
    if (mWalls.get(mFirstWall).intersects(mRunner)) {
      mAreaListener.dead();
    }
    if (mWalls.get(mFirstWall).getPositionX() + mWalls.get(mFirstWall).getWidth() <= 0.0f) {
      mAreaListener.incScore();
      int lastWall = mFirstWall == 0 ? GameArea.WALLS_COUNT - 1 : mFirstWall - 1;
      mWalls.set(mFirstWall, generateRandomWall(mWalls.get(lastWall).getPositionX()));
      mFirstWall = mFirstWall == GameArea.WALLS_COUNT - 1 ? 0 : mFirstWall + 1;
    }
    if (mAuto == true && mWalls.get(mFirstWall).getPositionX() < AUTO_MODE_JUMP_POS) {
      jump();
    }
  }

  public void render() {
    GraphicsContext gc = mCanvas.getGraphicsContext2D();

    gc.setFill(Color.BLACK);
    gc.fillRect(0, mAreaPosY + mAreaHeight - GROUND_HEIGHT, mAreaWidth, GROUND_HEIGHT);

    gc.clearRect(mAreaPosX, mAreaPosY, mAreaWidth, mAreaHeight - GameArea.GROUND_HEIGHT);
    gc.setFill(mBackgroundFill);
    gc.fillRect(mAreaPosX, mAreaPosY - 1, mAreaWidth, mAreaHeight - GameArea.GROUND_HEIGHT + 1);

    mRunner.render(gc);

    for (int i = 0; i < GameArea.WALLS_COUNT; i++) {
      mWalls.get(i).render(gc);
    }
    if (mScore != -1) {
      gc.setStroke(Color.BLACK);
      gc.strokeText("Score: " + mScore, mAreaWidth - SCORE_TEXT_WIDHT, SCORE_TEXT_HEIGHT);
    }
  }

  public void incSpeed() {
    mRunner.incSpeed(SPEED_UP_BY_SCORE / 5);
    for (Wall temp : mWalls) {
      temp.setSpeed((int) (temp.getSpeed() + SPEED_UP_BY_SCORE * temp.getSpeed()));
    }
    mSpeed = (int) (mSpeed + SPEED_UP_BY_SCORE * mSpeed);

  }

  public void setListener(GameAreaListener listener) {
    mAreaListener = listener;
  }

  public void setScore(int score) {
    mScore = score;
  }

  public void run() {
    Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
    mLastNanoTime = System.nanoTime();
    mGameLoop.start();
    return;
  }

  public void stopAnimation() {
    mGameLoop.stop();
  }

  public void startAnimation() {
    mLastNanoTime = System.nanoTime();
    mGameLoop.start();
  }

  public void jump() {
    mRunner.jump();
  }

  public void setBackgroundFill(Color backgroundFill) {
    this.mBackgroundFill = backgroundFill;
  }

  protected void generateRandomWalls() {
    int posLastWall = mAreaWidth;
    for (int i = 0; i < GameArea.WALLS_COUNT; i++) {
      int randomDistanceBetweenWalls = new Random().nextInt(MIN_DISTANCE_BETWEEN_WALLS)
          + MAX_DISTANCE_BETWEEN_WALLS - MIN_DISTANCE_BETWEEN_WALLS;
      int randomWidthWall = new Random().nextInt(MIN_WIDTH_WALL)
          + MAX_WIDTH_WALL - MIN_WIDTH_WALL;
      int randomHeightWall = new Random().nextInt(mMinHeightWall) + mMaxHeightWall - mMinHeightWall;

      mWalls.add(new Wall(mAreaPosX + posLastWall + randomDistanceBetweenWalls,
          mAreaPosY + mAreaHeight - randomHeightWall - GROUND_HEIGHT, mSpeed));
      mWalls.get(i).setSize(randomWidthWall, randomHeightWall);
      posLastWall = mWalls.get(i).getPositionX();
    }
  }

  protected void generateRunner(int countArea) {
    int runnerHeight = mAreaHeight / 4;
    mRunner = new Runner(mAreaPosX + RUNNER_POS_X,
        mAreaPosY + mAreaHeight - runnerHeight - GROUND_HEIGHT + 5);
    mRunner.setSize((int) (runnerHeight / RUNNER_SIZE_SCALE), runnerHeight);
    int startSpeedJump;
    switch (countArea) {
      case 1:
        startSpeedJump = 2400;
        break;
      case 2:
        startSpeedJump = 1700;
        break;
      case 3:
        startSpeedJump = 1300;
        break;
      default:
        startSpeedJump = 1100;
        break;
    }
    mRunner.setSpeedStartJump(startSpeedJump);
  }

  protected Wall generateRandomWall(int lastWallPos) {
    int randomDistanceBetweenWalls = new Random().nextInt(MIN_DISTANCE_BETWEEN_WALLS)
        + MAX_DISTANCE_BETWEEN_WALLS - MIN_DISTANCE_BETWEEN_WALLS;
    int randomWidthWall = new Random().nextInt(MIN_WIDTH_WALL)
        + MAX_WIDTH_WALL - MIN_WIDTH_WALL;
    int randomHeightWall = new Random().nextInt(mMinHeightWall) + mMaxHeightWall - mMinHeightWall;

    Wall wall = new Wall(mAreaPosX + randomDistanceBetweenWalls + lastWallPos,
        mAreaPosY + mAreaHeight - randomHeightWall - GROUND_HEIGHT, mSpeed);
    wall.setSize(randomWidthWall, randomHeightWall);
    return wall;
  }
}

package srun;

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
  /**
   * Listener interface used for communication with the flow of the other classes of
   */
  protected GameAreaListener mAreaListener;

  protected static final int GROUND_HEIGHT = 10;
  protected static final int START_SPEED = 500;
  protected static final int WALLS_COUNT = 3;
  protected static final int MIN_WIDTH_WALL = 20, MAX_WIDTH_WALL = 40;
  protected static final int MIN_DISTANCE_BETWEEN_WALLS = 800, MAX_DISTANCE_BETWEEN_WALLS = 1600;
  private static final float SPEED_UP_BY_SCORE = 0.05f;
  private static final int SCORE_TEXT_WIDHT = 220;
  private static final int SCORE_TEXT_HEIGHT = 50;
  private static final int RUNNER_POS_X = 40;
  private static final float RUNNER_SIZE_SCALE = 1.58f;
  private int mAutoModeJumpPos;
  private boolean mMySelfMode = false;

  protected Runner mRunner;
  protected ArrayList<Wall> mWalls = new ArrayList<Wall>(GameArea.WALLS_COUNT);

  protected Canvas mCanvas;
  private Color mBackgroundFill = Color.WHITE;

  private int mScore = -1;
  private int mCountArea;

  protected int mMinHeightWall, mMaxHeightWall;
  protected int mAreaPosX, mAreaPosY;
  protected int mAreaWidth, mAreaHeight;
  protected int mSpeed = GameArea.START_SPEED;

  protected int mFirstWall = 0;
  protected int mLastWall = -1;
  private long mLastNanoTime;
  private long mStartNanoTime = 0L;

  private ReplayWriter mReplayWriter = null;
  private ReplayReader mReplayReader = null;

  private boolean mIsAutoMode = false;
  private boolean mIsReplayMode = false;
  private long mJumpReplayNanoTime = 0L;
  private int mJumpDistanceForFirstWall = 0;

  class GameLoop extends AnimationTimer {

    private int mLastWall = 0;

    @Override
    public void handle(long now) {
      double deltaTime = (double) (now - mLastNanoTime) / Math.pow(10.0f, 9.0f);
      mLastNanoTime = System.nanoTime();
      checkReplayJump();
      GameArea.this.update(deltaTime);
      GameArea.this.render();
    }

    public void checkReplayJump() {
      if (mIsReplayMode == true && mJumpDistanceForFirstWall != 0
          && mWalls.get(mFirstWall).getPositionX()
              - mRunner.getPositionX() <= mJumpDistanceForFirstWall) {
        if (mReplayReader.hasNextWall()) {
          if (mLastWall != mFirstWall) {
            GameArea.this.jump();
            initReplayJumpTime();
            mLastWall = mFirstWall;
          }
        } else {
          GameArea.this.jump();
          initReplayJumpTime();
          mLastWall = mFirstWall;
        }
      }
    }

    public void initReplayJumpTime() {
      mJumpDistanceForFirstWall = mReplayReader.getJumpPos();
    }
  }

  GameLoop mGameLoop;

  public GameArea(Canvas canvas, int posX, int posY, int width, int height, int countArea,
      boolean isAutoMode, String replayWriteFilePath) {
    mAreaPosX = posX;
    mAreaPosY = posY;
    mAreaWidth = width;
    mAreaHeight = height;
    mCanvas = canvas;
    mIsAutoMode = isAutoMode;
    mMinHeightWall = mAreaHeight / 8;
    mMaxHeightWall = mAreaHeight / 5;
    mCountArea = countArea;
    mReplayWriter = new ReplayWriter(replayWriteFilePath);

    generateRunner();

    if (mIsAutoMode) {
      mAutoModeJumpPos = mRunner.getWidth() + 150;
    }
  }

  public GameArea(Canvas canvas, int posX, int posY, int width, int height, int countArea,
      String replayReadPath) {
    mAreaPosX = posX;
    mAreaPosY = posY;
    mAreaWidth = width;
    mAreaHeight = height;
    mCanvas = canvas;
    mMinHeightWall = mAreaHeight / 8;
    mMaxHeightWall = mAreaHeight / 5;
    mCountArea = countArea;
    mIsReplayMode = true;

    mReplayReader = new ReplayReader(replayReadPath);

    generateRunner();

    mAutoModeJumpPos = mRunner.getWidth() + 150;
  }

  @Override
  public void run() {

    generateRunner();
    generateRandomWalls();

    if (mIsReplayMode == false) {
      if (mMySelfMode == false) {
        mReplayWriter.writeAutoMode(mIsAutoMode);
        mReplayWriter.writeColor(mBackgroundFill);
      }
    } else {
      mBackgroundFill = mReplayReader.getColor();
    }

    mLastNanoTime = System.nanoTime();
    mGameLoop = new GameLoop();
    if (mIsReplayMode) {
      mGameLoop.initReplayJumpTime();
    }
    synchronized (mCanvas.getGraphicsContext2D()) {
      mGameLoop.start();
    }
    mStartNanoTime = System.nanoTime();
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

  public void jump() {
    if (mIsReplayMode == false && mRunner.getJump() == false) {
      if (mMySelfMode == false) {
        mReplayWriter.writeJumpPos(mWalls.get(mFirstWall).getPositionX() - mRunner.getPositionX());
      }
    }
    mRunner.jump();
  }

  public void setMySelf(boolean mySelf) {
    mMySelfMode = mySelf;
  }

  public void setBackgroundFill(Color backgroundFill) {
    this.mBackgroundFill = backgroundFill;
  }

  public void endGame() {
    stopAnimation();

  }

  public void stopAnimation() {
    mGameLoop.stop();
    if (mMySelfMode == true) {
      synchronized (mCanvas.getGraphicsContext2D()) {
        mCanvas.getGraphicsContext2D().fillText("u looooose", mAreaWidth / 4,
            mAreaPosY + SCORE_TEXT_HEIGHT);
      }
    }
  }

  public void startAnimation() {
    mLastNanoTime = System.nanoTime();
    mGameLoop.start();
  }

  private void update(double deltaTime) {
    mRunner.update(deltaTime);
    for (int i = 0; i < GameArea.WALLS_COUNT; i++) {
      mWalls.get(i).update(deltaTime);
    }

    if (mWalls.get(mFirstWall).getPositionX() + mWalls.get(mFirstWall).getWidth() < mRunner
        .getPositionX()) {
      int lastWall = mFirstWall == 0 ? GameArea.WALLS_COUNT - 1 : mFirstWall - 1;
      Wall wall = generateRandomWall(mWalls.get(lastWall).getPositionX());
      if (wall != null) {
        mWalls.set(mFirstWall, wall);
        mFirstWall = mFirstWall == GameArea.WALLS_COUNT - 1 ? 0 : mFirstWall + 1;
      }
      if (mMySelfMode == true) {
        mAreaListener.incScore(false);
        mScore++;
      } else {
        mAreaListener.incScore(true);
      }
    }
    if (mIsAutoMode == true && mWalls.get(mFirstWall).getPositionX() < mAutoModeJumpPos
        && mRunner.getJump() == false) {
      jump();
    }
    if (mIsReplayMode == false) {
      if (mWalls.get(mFirstWall).intersects(mRunner)) {
        if (mIsReplayMode == false) {
          if (mMySelfMode == false) {
            mReplayWriter
                .writeDeadPos(mWalls.get(mFirstWall).getPositionX() - mRunner.getPositionX());
          }
        }
        mAreaListener.dead();
        endGame();
      }
    } else {
      if (mReplayReader.getDeadPos() != 0 && mReplayReader
          .getDeadPos() >= mWalls.get(mFirstWall).getPositionX() - mRunner.getPositionX()) {
        mAreaListener.dead();
      }
    }
  }

  private void render() {
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
    if (mScore >= 0) {
      gc.setStroke(Color.BLACK);
      gc.strokeText("Score: " + mScore, mAreaWidth - SCORE_TEXT_WIDHT,
          mAreaPosY + SCORE_TEXT_HEIGHT);
    }
  }

  protected Wall generateRandomWall(int lastWallPos) {
    int randomDistanceBetweenWalls;
    int randomWidthWall;
    int randomHeightWall;

    if (mIsReplayMode == false) {
      randomDistanceBetweenWalls = new Random().nextInt(MIN_DISTANCE_BETWEEN_WALLS)
          + MAX_DISTANCE_BETWEEN_WALLS - MIN_DISTANCE_BETWEEN_WALLS;
      randomWidthWall = new Random().nextInt(MIN_WIDTH_WALL) + MAX_WIDTH_WALL - MIN_WIDTH_WALL;
      randomHeightWall = new Random().nextInt(mMinHeightWall) + mMaxHeightWall - mMinHeightWall;
      if (mMySelfMode == false) {
        mReplayWriter.writeWallCoord(
            new WallCoord(randomDistanceBetweenWalls, randomWidthWall, randomHeightWall));
      }
    } else {
      if (mIsReplayMode && System.nanoTime() - mStartNanoTime >= mJumpReplayNanoTime
          && mJumpReplayNanoTime != 0L) {
        GameArea.this.jump();
        // mGameLoop.initReplayJumpTime();
      }
      WallCoord wall = mReplayReader.readNextWall();
      if (wall == null) {
        randomDistanceBetweenWalls = new Random().nextInt(MIN_DISTANCE_BETWEEN_WALLS)
            + MAX_DISTANCE_BETWEEN_WALLS - MIN_DISTANCE_BETWEEN_WALLS;
        randomWidthWall = new Random().nextInt(MIN_WIDTH_WALL) + MAX_WIDTH_WALL - MIN_WIDTH_WALL;
        randomHeightWall = new Random().nextInt(mMinHeightWall) + mMaxHeightWall - mMinHeightWall;
      } else {
        randomDistanceBetweenWalls = wall.getDistance();
        randomWidthWall = wall.getWidth();
        randomHeightWall = wall.getHeight();
      }
    }
    if (mIsReplayMode && System.nanoTime() - mStartNanoTime >= mJumpReplayNanoTime
        && mJumpReplayNanoTime != 0L) {
      GameArea.this.jump();
      // mGameLoop.initReplayJumpTime();
    }
    Wall wall = new Wall(mAreaPosX + randomDistanceBetweenWalls + lastWallPos,
        mAreaPosY + mAreaHeight - randomHeightWall - GROUND_HEIGHT, mSpeed);
    wall.setSize(randomWidthWall, randomHeightWall);
    return wall;
  }

  protected void generateRandomWalls() {
    int posLastWall = mAreaWidth;

    int randomDistanceBetweenWalls;
    int randomWidthWall;
    int randomHeightWall;

    for (int i = 0; i < GameArea.WALLS_COUNT; i++) {
      if (mIsReplayMode == false) {
        randomDistanceBetweenWalls = new Random().nextInt(MIN_DISTANCE_BETWEEN_WALLS)
            + MAX_DISTANCE_BETWEEN_WALLS - MIN_DISTANCE_BETWEEN_WALLS;
        randomWidthWall = new Random().nextInt(MIN_WIDTH_WALL) + MAX_WIDTH_WALL - MIN_WIDTH_WALL;
        randomHeightWall = new Random().nextInt(mMinHeightWall) + mMaxHeightWall - mMinHeightWall;
        if (mMySelfMode == false) {
          mReplayWriter.writeWallCoord(
              new WallCoord(randomDistanceBetweenWalls, randomWidthWall, randomHeightWall));
        }
      } else {
        WallCoord wall = mReplayReader.readNextWall();
        randomDistanceBetweenWalls = wall.getDistance();
        randomWidthWall = wall.getWidth();
        randomHeightWall = wall.getHeight();
      }
      mWalls.add(new Wall(mAreaPosX + posLastWall + randomDistanceBetweenWalls,
          mAreaPosY + mAreaHeight - randomHeightWall - GROUND_HEIGHT, mSpeed));
      mWalls.get(i).setSize(randomWidthWall, randomHeightWall);
      posLastWall = mWalls.get(i).getPositionX();
    }
  }

  protected void generateRunner() {
    int runnerHeight = mAreaHeight / 4;
    mRunner = new Runner(mAreaPosX + RUNNER_POS_X,
        mAreaPosY + mAreaHeight - runnerHeight - GROUND_HEIGHT + 5);
    mRunner.setSize((int) (runnerHeight / RUNNER_SIZE_SCALE), runnerHeight);
    int startSpeedJump;
    switch (mCountArea) {
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

}

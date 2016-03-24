package application;

import java.io.IOException;
import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameArea implements Runnable, ActionConstants {
	private static final int GROUND_HIGHT = 20;
	private static final int START_SPEED = 300;
	private static final int WALLS_COUNT = 3;
	private int mMinDistanceBetweenWalls = 800, mMaxDistanceBetweenWalls = 1600;
	private int mMinHeightWall = 20, mMaxHeightWall = 40;
	private int mMinWidthWall = 40, mMaxWidthWall = 80;
	private Color mBackgroundFill = Color.WHITE;
	private int mPosXarea, mPosYarea;
	private int mWidthArea, mHeightArea;
	private int mSpeed = GameArea.START_SPEED;
	Object sync;
	/**
	 * Итератор по коллекции, содержащей стены, соответствующий самой левой
	 * стенке
	 */
	protected int mFirstWall = 0;
	private Runner mRunner;
	private Integer mScore;
	protected ArrayList<Wall> mWalls = new ArrayList<Wall>(GameArea.WALLS_COUNT);
	volatile private Canvas mCanvas;
	private long mLastNanoTime;
	private PipedWriter mPipeWriterForLauncher;
	private AnimationTimer mGameLoop = new AnimationTimer() {

		@Override
		public void handle(long now) {
			synchronized (sync) {
				double deltaTime = (double) (now - mLastNanoTime) / Math.pow(10.0f, 9.0f);
				GameArea.this.update(deltaTime);
				GameArea.this.render();
				mLastNanoTime = System.nanoTime();
			}

		}
	};

	public GameArea(Canvas canvas, int posX, int posY, int width, int heigth) {
		mRunner = new Runner(posX + 40, posY + heigth - 130);
		mRunner.setSize(70, 110);
		this.mPosXarea = posX;
		this.mPosYarea = posY;
		this.mWidthArea = width;
		this.mHeightArea = heigth;
		this.mCanvas = canvas;

		for (int i = 0; i < GameArea.WALLS_COUNT; i++) {
			int randomDistanceBetweenWalls = new Random().nextInt(mMinDistanceBetweenWalls) + mMaxDistanceBetweenWalls
					- mMinDistanceBetweenWalls;
			int randomWidthWall = new Random().nextInt(mMinWidthWall) + mMaxWidthWall - mMinWidthWall;
			int randomHeightWall = new Random().nextInt(mMinHeightWall) + mMaxHeightWall - mMinHeightWall;
			mWalls.add(new Wall(mPosXarea + mWidthArea + randomDistanceBetweenWalls * (i + 1),
					mPosYarea + mHeightArea - randomHeightWall - GROUND_HIGHT, mSpeed));
			mWalls.get(i).setSize(randomWidthWall, randomHeightWall);

		}
		GraphicsContext gc = mCanvas.getGraphicsContext2D();
		synchronized (gc) {
			gc.setFill(Color.BLACK);
			gc.fillRect(0, posY + mHeightArea - GROUND_HIGHT, width, GROUND_HIGHT);
		}
	}

	void setSyncObject(Object sync) {
		this.sync = sync;
	}

	public void update(double deltaTime) {
		mRunner.update(deltaTime);
		for (int i = 0; i < GameArea.WALLS_COUNT; i++) {
			mWalls.get(i).update(deltaTime);
		}
		// если runner врезался в стенку
		if (mWalls.get(mFirstWall).intersects(mRunner)) {
			System.out.println("stop");
			try {
				mPipeWriterForLauncher.write((int) ActionConstants.IS_DEAD);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit((int) ActionConstants.IS_DEAD);
			}
		}
		// если runner перепрыгнул стенку
		if (mWalls.get(mFirstWall).getPositionX() + mWalls.get(mFirstWall).getWidth() <= 0.0f) {
			int randomDistanceBetweenWalls = new Random().nextInt(mMinDistanceBetweenWalls) + mMaxDistanceBetweenWalls
					- mMinDistanceBetweenWalls;
			int randomWidthWall = new Random().nextInt(mMinWidthWall) + mMaxWidthWall - mMinWidthWall;
			int randomHeightWall = new Random().nextInt(mMinHeightWall) + mMaxHeightWall - mMinHeightWall;
			mWalls.get(mFirstWall).setPosition(mPosXarea + mWidthArea + randomDistanceBetweenWalls,
					mPosYarea + mHeightArea - randomHeightWall - GameArea.GROUND_HIGHT);
			mWalls.get(mFirstWall).setSize(randomWidthWall, randomHeightWall);
			mFirstWall = mFirstWall == GameArea.WALLS_COUNT - 1 ? 0 : mFirstWall + 1;
			for (Wall tempWall : mWalls) {
				tempWall.setSpeed(mSpeed);
			}
			try {
				mPipeWriterForLauncher.write((int) ActionConstants.INC_SCORE);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void render() {
		GraphicsContext gc = mCanvas.getGraphicsContext2D();
		gc.clearRect(mPosXarea, mPosYarea, mWidthArea, mHeightArea - GameArea.GROUND_HIGHT);
		gc.setFill(mBackgroundFill);
		gc.fillRect(mPosXarea, mPosYarea - 1, mWidthArea, mHeightArea - GameArea.GROUND_HIGHT + 1);
		if (mScore != null) {
			gc.setStroke(mBackgroundFill.BLACK);
			gc.strokeText("Score: " + mScore, mWidthArea - 200, 50);
		}
		mRunner.render(gc);
		for (int i = 0; i < GameArea.WALLS_COUNT; i++) {
			mWalls.get(i).render(gc);
		}

	}

	public void run() {
		mLastNanoTime = System.nanoTime();
		mGameLoop.start();
	}

	public void jump() {
		mRunner.setJump(true);
	}

	public Color getBackgroundFill() {
		return mBackgroundFill;
	}

	public void setBackgroundFill(Color backgroundFill) {
		this.mBackgroundFill = backgroundFill;
	}

	public int getSpeed() {
		return mSpeed;
	}

	public PipedWriter getPipeWriterForLauncher() {
		return mPipeWriterForLauncher;
	}

	public void setPipeWriterForLauncher(PipedWriter pipeWriterForLauncher) {
		this.mPipeWriterForLauncher = pipeWriterForLauncher;
	}

	public void setScore(Integer score) {
		mScore = score;
	}

	public void setSpeed(int speed) {
		mSpeed = speed;
	}
}

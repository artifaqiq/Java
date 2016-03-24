package application;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameLauncher implements Runnable {
	private Integer mScore = new Integer(0);
	private int mWidth, mHeight;
	private Canvas mCanvas;
	private long mLastNanoTime;
	private int mCountArea;
	private ArrayList<GameArea> mAreas;
	Object sync = new Object();

	public GameLauncher(Group root, int width, int height, int countArea) {
		System.out.println("Constr Game Launcher");
		mWidth = width > 0 ? width : 1600;
		mHeight = height > 0 ? height : 800;
		mCountArea = countArea >= 1 ? countArea : 1;
		mAreas = new ArrayList<>(mCountArea);
		mCanvas = new Canvas(width, height);
		root.getChildren().add(mCanvas);
		mCanvas.resize(mWidth, mHeight);
		for (int i = 0; i < this.mCountArea; i++) {
			mAreas.add(new GameAreaAuto(mCanvas, 0, 800 * i / this.mCountArea, 1600, 800 / this.mCountArea));
			mAreas.get(i).setBackgroundFill(new Color(new Random().nextDouble(), new Random().nextDouble(),
					new Random().nextDouble(), new Random().nextDouble()));
		}
		mCanvas.setFocusTraversable(true);
		mCanvas.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.ENTER) {
					mAreas.get(0).jump();
				}
				if (e.getCode() == KeyCode.SPACE) {
					mAreas.get(1).jump();
				}
			}
		});
		mCanvas.getGraphicsContext2D().setFont(new Font(45));

	}
	
	public void run() {
		mLastNanoTime = System.nanoTime();
		PipedWriter pipeWriter = new PipedWriter();
		Executor exec = Executors.newFixedThreadPool(mCountArea);
		for (int i = 0; i < mCountArea; i++) {
			mAreas.get(i).setPipeWriterForLauncher(pipeWriter);
			mAreas.get(i).setSyncObject(sync);
			exec.execute(new Thread(mAreas.get(i)));

		}
		mAreas.get(0).setScore(mScore);
		// ожидание поступления данных в канал
		try {
			int action;
			PipedReader pipeReader = new PipedReader(pipeWriter);
			while (true) {
				action = pipeReader.read();
				if (action == ActionConstants.INC_SCORE) {
					System.out.println("inc score");
					mScore++;
					mAreas.get(0).setScore(mScore);
				} else if (action == ActionConstants.IS_DEAD) {
					System.out.println("is dead");
				}
			}
		} catch (IOException e) {
			System.out.println("Execption");
			e.printStackTrace();
		}
	}

	

	public int getWidth() {
		return mWidth;
	}

	public int getHeight() {
		return mHeight;
	}

	public int getScore() {
		return mScore;
	}

	public void incScore() {
		mScore++;
	}

}

package application;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Menu {
	protected static final long UPDATE_RUNNER_NANO_FREQ = 30000000;
	private Stage mMainStage;
	private Scene mMainScene;
	private Group mRoot;
	private GridPane mMainGrid = new GridPane();
	Button mButtonRun = new Button("Run");
	Button mButtonViewStatistic = new Button("Statistics");
	Button mButtonExit = new Button("Exit");
	Button mButtonSetting = new Button("Setting");
	ArrayList<ImageView> mRunnerImages = new ArrayList<>();
	private int mIterImages = 0;
	private long mLastNanoTime;
	int mWidhtRunner = 50, mHeightRunner = 200;

	public Menu(Stage theStage) {
		mRoot = new Group();
		mMainScene = new Scene(mRoot);
		this.mMainStage = theStage;
		for (int i = 0; i < 10; i++) {
			mRunnerImages.add(new ImageView(new Image("/res/Idle__00" + i + ".png")));
			mRunnerImages.get(i).setFitWidth(mWidhtRunner);
			mRunnerImages.get(i).setFitHeight(mHeightRunner);
		}
		VBox buttonsVbox = new VBox();
		buttonsVbox.setSpacing(10.0f);
		buttonsVbox.setPadding(new Insets(20, 20, 20, 20));

		mButtonRun.setPrefSize(300, 145);
		mButtonExit.setPrefSize(300, 145);
		mButtonViewStatistic.setPrefSize(300, 145);
		mButtonSetting.setPrefSize(300, 145);
		buttonsVbox.getChildren().addAll(mButtonRun, mButtonSetting,mButtonViewStatistic, mButtonExit);
		buttonsVbox.setSpacing(20.0f);
		mMainGrid.add(buttonsVbox, 0, 0);
		mMainGrid.setGridLinesVisible(true);

	}

	public void show() {
		mLastNanoTime = System.nanoTime();
		Canvas runnerCanvas = new Canvas(400, 500);
		mMainGrid.add(runnerCanvas, 1, 0);
		new AnimationTimer() {

			@Override
			public void handle(long now) {
				if (now - mLastNanoTime > UPDATE_RUNNER_NANO_FREQ) {
					mIterImages++;
					if (mIterImages == 10) {
						mIterImages = 0;
					}
					runnerCanvas.getGraphicsContext2D().clearRect(20, 20, 380, 480);
					runnerCanvas.getGraphicsContext2D().drawImage(mRunnerImages.get(mIterImages).getImage(), 70, 20);
					mLastNanoTime = System.nanoTime();
				}

			}
		}.start();
		mRoot.getChildren().add(mMainGrid);

		mButtonExit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.exit(0);
			}
		});
		mMainStage.sizeToScene();
		mMainStage.setScene(mMainScene);
		mMainStage.setTitle("SRunner: menu");
		mMainStage.show();
	}
}

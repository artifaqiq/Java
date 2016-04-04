

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
	private Canvas mRunnerCanvas;
	private Color backgroundColor = Color.AZURE;
	private GameListener mListener;
	
	public Menu() {

		getStylesheets().add(Menu.class.getResource("application.css").toExternalForm());

		HBox buttonsControlHbox = new HBox();
		VBox buttonsRunVbox = new VBox();

		setBackground(new Background(new BackgroundFill(backgroundColor, new CornerRadii(0.0), new Insets(0.0))));

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
		buttonsRun.forEach(mButtonsRun -> mButtonsRun.setPrefSize(300, 100));
		buttonsRunVbox.getChildren().add(autoMode);
		buttonsRunVbox.getChildren().addAll(buttonsRun);
		buttonsRunVbox.setSpacing(15);
		
		Button buttonExit = new Button("EXIT");
		buttonExit.setPrefSize(145, 50);
		buttonExit.setId("dark-blue");

		Button buttonViewStatistic = new Button("STATISTIC");
		buttonViewStatistic.setPrefSize(145, 50);
		buttonViewStatistic.setId("dark-blue");
		Button buttonSetting = new Button("SETTING");
		buttonSetting.setPrefSize(145, 50);
		buttonSetting.setId("dark-blue");
		buttonsControlHbox.getChildren().addAll(buttonSetting, buttonViewStatistic);

		buttonsControlHbox.setSpacing(10);
		buttonsControlHbox.getChildren().addAll(buttonExit);
		
		setBottom(buttonsControlHbox);

		setLeft(buttonsRunVbox);
		setPadding(new Insets(10, 10, 10, 10));

		ArrayList<ImageView> runnerImages = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			runnerImages.add(new ImageView(new Image("/res/Idle__00" + i + ".png")));
			runnerImages.get(i).setFitWidth(RUNNER_WIDTH);
			runnerImages.get(i).setFitHeight(RUNNER_HEIGHT);
		}
		
		
		mRunnerCanvas = new Canvas(1270, 730);
		setRight(mRunnerCanvas);
		AnimationTimer animationTimer = new AnimationTimer() {
			private int mIterImages = 0;
			private long mLastNanoTime = System.nanoTime();

			@Override
			public void handle(long now) {
				if (now - mLastNanoTime > UPDATE_RUNNER_NANO_FREQ) {
					mIterImages++;
					if (mIterImages == 10) {
						mIterImages = 0;
					}
					mRunnerCanvas.getGraphicsContext2D().clearRect(20, 20, 380, 480);
					mRunnerCanvas.getGraphicsContext2D().drawImage(runnerImages.get(mIterImages).getImage(), 70, 20);
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
		buttonsRun.get(0).setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				animationTimer.stop();
				mListener.startGame(1, autoMode.isSelected());
			}
		});
		buttonsRun.get(1).setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				animationTimer.stop();
				mListener.startGame(2, autoMode.isSelected());
			}
		});
		buttonsRun.get(2).setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				animationTimer.stop();
				mListener.startGame(3, autoMode.isSelected());
			}
		});
		buttonsRun.get(3).setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				animationTimer.stop();
				mListener.startGame(4, autoMode.isSelected());
			}
		});
	}
	
	public void setListener(GameListener listener) {
		mListener = listener;
	}
	
}

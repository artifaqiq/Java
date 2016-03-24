package application;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SRunner extends Application {

	@Override
	public void start(Stage theStage) {
		Group root = new Group();
//		Scene scene = new Scene(root);
		//GameLauncher launcher = new GameLauncher(root, 1600, 800, 2);
		//Thread gameThread=new Thread(launcher);
		//gameThread.start();
		Menu menu=new Menu(theStage);
		menu.show();
//		theStage.setScene(scene);
	//	theStage.setTitle("S-Runner");
	//	theStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}

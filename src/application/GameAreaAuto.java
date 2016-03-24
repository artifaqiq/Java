package application;

import javafx.scene.canvas.Canvas;

public class GameAreaAuto extends GameArea{

	public GameAreaAuto(Canvas canvas, int posX, int posY, int width, int heigth) {
		super(canvas, posX, posY, width, heigth);
	}
	
	public void update(double deltaTime){
		super.update(deltaTime);
		if(mWalls.get(mFirstWall).getPositionX() <130){
			jump();
		}
	}
	
}

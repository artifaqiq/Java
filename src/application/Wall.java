package application;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Wall {
	double mStartPosX;
	double mStartPosY;
	double mPosX;
	double mPosY;
	double mWidth;
	double mHeight;
	int mSpeed;

	public Wall(double startPositionX, double startPositionY, int speed) {
		mPosX = startPositionX;
		mPosY = startPositionY;
		this.mStartPosX = startPositionX;
		this.mStartPosY = startPositionY;
		this.mSpeed = speed;
	}

	public double getPositionX() {
		return mPosX;
	}

	public double getWidth() {
		return mWidth;
	}

	public void setSpeed(int speed) {
		this.mSpeed = speed;
	}

	public double getPositionY() {
		return mPosY;
	}
	
	public void setSize(double width, double height) {
		this.mWidth = width;
		this.mHeight = height;
	}

	public void setPosition(double x, double y) {
		mPosX = x;
		mPosY = y;
	}

	public int getSpeed() {
		return mSpeed;
	}
	/**
	 * Предположительно, метод вызывается из игрового бесконечного цикла 
	 * и обновляет положение и другие характеристики объекта
	 * 
	 * @param time  приращение времени между обновлениями кадров
	 */
	public void update(double time) {
		mPosX = mPosX - mSpeed * time;
	}

	public void render(GraphicsContext gc) {
		gc.setFill(Color.BLACK);
		gc.fillRect(mPosX, mPosY, mWidth, mHeight);
	}

	/**
	 * @return прямогульник, расположение которого соответствует расположению стенки*/
	public Rectangle2D getBoundary() {
		return new Rectangle2D(mPosX, mPosY, mWidth, mHeight);
	}

	/**
	 * Ищет пересечение с обеъектом Sprite
	 * */
	public boolean intersects(Sprite s) {
		return s.getBoundary().intersects(this.getBoundary());
	}

}

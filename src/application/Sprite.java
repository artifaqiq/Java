package application;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

/**@author Артур Ломако 450504
 * Класс, использующийся как базовый для реализации некоторого анимационного объекта
 */
abstract public class Sprite {
	/**
	 * Координаты верхней левой точки вымышленного прямоугольника,
	 * ограничевающего изображение объекта
	 */
	protected double mPosX, mPosY;
	/**
	 * Высота и ширина вымышленного прямоугольника
	 */
	protected double mWidth = 0.0f, mHeight = 0.0f;
	
	abstract public void update(double time);

	abstract public void render(GraphicsContext gc);

	public Sprite() {
		mPosX = 0;
		mPosY = 0;
	}

	public void setPosition(double x, double y) {
		mPosX = x;
		mPosY = y;
	}

	public void setSize(double width, double height) {
		this.mWidth = width;
		this.mHeight = height;
	}

	/**
	 * @return вымышленный прямоугольник
	 */
	public Rectangle2D getBoundary() {
		return new Rectangle2D(mPosX, mPosY, mWidth, mHeight);
	}

	public double getWidth() {
		return mWidth;
	}

	public double getHeight() {
		return mHeight;
	}
	/**
	 *Ищет пересечения с другим объектом данного класса 
	 */
	public boolean intersects(Sprite s) {
		return s.getBoundary().intersects(this.getBoundary());
	}

}
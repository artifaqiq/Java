package application;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

/**@author ����� ������ 450504
 * �����, �������������� ��� ������� ��� ���������� ���������� ������������� �������
 */
abstract public class Sprite {
	/**
	 * ���������� ������� ����� ����� ������������ ��������������,
	 * ��������������� ����������� �������
	 */
	protected double mPosX, mPosY;
	/**
	 * ������ � ������ ������������ ��������������
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
	 * @return ����������� �������������
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
	 *���� ����������� � ������ �������� ������� ������ 
	 */
	public boolean intersects(Sprite s) {
		return s.getBoundary().intersects(this.getBoundary());
	}

}
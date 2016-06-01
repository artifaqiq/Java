package srun;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

/** The base class for graphical objects */
abstract public class Sprite {
  protected int mPosX, mPosY;
  protected int mWidth = 0, mHeight = 0;

  /** alculates the position of the object over time */
  abstract public void update(double time);

  /** Draws this object */
  abstract public void render(GraphicsContext gc);

  public Sprite() {
    mPosX = 0;
    mPosY = 0;
  }

  public void setPosition(int x, int y) {
    mPosX = x;
    mPosY = y;
  }

  public int getPositionX() {
    return mPosX;
  }

  public int getPositionY() {
    return mPosY;
  }

  public void setSize(int width, int height) {
    this.mWidth = width;
    this.mHeight = height;
  }

  /** @return figure 2D, according to the object */
  public Rectangle2D getBoundary() {
    return new Rectangle2D(mPosX, mPosY, mWidth, mHeight);
  }

  public int getWidth() {
    return mWidth;
  }

  public int getHeight() {
    return mHeight;
  }

  /** @return true, if the object overlaps with other related object */
  public boolean intersects(Sprite s) {
    return s.getBoundary().intersects(this.getBoundary());
  }

  @Override
  public String toString() {
    return (mPosX + " " + mPosY + " " + mWidth + " " + mHeight + " ");
  }
}

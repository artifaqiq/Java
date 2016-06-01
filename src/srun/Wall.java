package srun;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/** Wall is a moving object */
public class Wall extends Sprite {
  /** Speed the Wall */
  private int mSpeed;
  /** Color the Wall */
  transient private Paint mColorWall = Color.BLACK;

  public Wall(int startPosX, int startPosY, int speed) {
    mPosX = startPosX;
    mPosY = startPosY;
    mSpeed = speed;
  }

  public void setSpeed(int speed) {
    mSpeed = speed;
  }

  public int getSpeed() {
    return mSpeed;
  }

  public void update(double time) {
    mPosX = (int) (mPosX - mSpeed * time);
  }

  public void render(GraphicsContext gc) {
    gc.setFill(Color.BLACK);
    gc.fillRect(mPosX, mPosY, mWidth, mHeight);
  }

  public Paint getColorWall() {
    return mColorWall;
  }

  public void setColorWall(Paint mColorWalls) {
    this.mColorWall = mColorWalls;
  }

  @Override
  public String toString() {
    return super.toString() + " " + mSpeed + " ";
  }
}

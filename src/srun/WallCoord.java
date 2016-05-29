package srun;

public class WallCoord {
  int mDistance;
  int mHeight;
  int mWidth;

  public WallCoord() {}

  public WallCoord(int distance, int width, int height) {
    mDistance = distance;
    mHeight = height;
    mWidth = width;
  }

  public int getDistance() {
    return mDistance;
  }

  public void setDistance(int distance) {
    mDistance = distance;
  }

  public int getHeight() {
    return mHeight;
  }

  public void setHeight(int height) {
    mHeight = height;
  }

  public int getWidth() {
    return mWidth;
  }

  public void setWidth(int width) {
    mWidth = width;
  }

  @Override
  public String toString() {
    return "distace=" + mDistance + " width=" + mWidth + " height=" + mHeight;
  }
}

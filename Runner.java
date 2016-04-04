import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * The runner does not move back and forth. He only knows how to jump and create the illusion of
 * forward movement
 */
public class Runner extends Sprite {
  private int mStartPosY;

  /** gravitation */
  private int mGravitation = 280;
  /** start speed jump */
  private int mSpeedStartJump = 1100;
  /** added value, improving the kinematic properties */
  private int mAcerbity = 20;

  /** time change run pictures */
  private double mDeltaChangeRunImages = .019f;
  /** time change jump pictures */
  private double mDeltaChangeJumpImages = (double) mSpeedStartJump / (mGravitation * mAcerbity * 5);

  private ArrayList<ImageView> mRunImages = new ArrayList<>(10);
  private ArrayList<ImageView> mJumpImages = new ArrayList<>(10);

  public Runner(int startPositionX, int startPositionY) {
    mStartPosY = startPositionY;
    mPosX = startPositionX;
    mPosY = startPositionY;

    // heer load run pictures
    Image tempImage;
    for (int i = 0; i <= 9; i++) {
      tempImage = new Image(getClass().getResourceAsStream("/res/Run__00" + i + ".png"));
      mRunImages.add(new ImageView(tempImage));
      if (mWidth >= 0.0f || mHeight >= 0.0f) {
        mRunImages.get(i).setFitWidth(mWidth);
        mRunImages.get(i).setFitHeight(mHeight);
      }
    }

    // hear load jump pictures
    for (int i = 0; i <= 9; i++) {
      tempImage = new Image(getClass().getResourceAsStream("/res/Jump__00" + i + ".png"));
      mJumpImages.add(new ImageView(tempImage));
      if (mWidth >= 0.0f || mHeight >= 0.0f) {
        mJumpImages.get(i).setFitWidth(mWidth);
        mJumpImages.get(i).setFitHeight(mHeight);
      }
    }
  }

  /** true, if the Runner jumped */
  private boolean mIsJump = false;
  private int mSpeedY = 0;

  public void jump() {
    if (!mIsJump) {
      mSpeedY = mSpeedStartJump;
    }
    mIsJump = true;
  }

  public boolean getJump() {
    return mIsJump;
  }

  /** the time that the picture is not changed */
  private double mTimeJumpImage = 0.0f, mTimeRunImage = 0.0f;
  /** iterator over a collection with pictures */
  private int mIterRunImages = 0, mIterJumpImages = 0;

  public void update(double time) {
    // if runner jump
    if (mIsJump == true) {
      // kinematics formula to calculate the coordinates
      mPosY = (int) (mPosY - mSpeedY * time + mGravitation * time * time / 2);

      // if the runner intersected with land
      if (mPosY >= mStartPosY) {
        mIsJump = false;
        mPosY = mStartPosY;
        mSpeedY = 0;
        mIterJumpImages = 0;
        mTimeJumpImage = 0.0f;
        return;
      }
      // kinematics formula to calculate the speed v = v0 + a*t
      mSpeedY = (int) (mSpeedY - mGravitation * time * mAcerbity);
      mTimeJumpImage += time;

      // if it's time to change the image
      if (mTimeJumpImage >= mDeltaChangeJumpImages) {
        mIterJumpImages = mIterJumpImages == 9 ? 0 : mIterJumpImages + 1;
        mTimeJumpImage = 0.0f;
      }
    }
    // if the runner does not jump
    else {
      mTimeRunImage += time;
      if (mTimeRunImage >= mDeltaChangeRunImages) {
        mIterRunImages = mIterRunImages == 9 ? 0 : mIterRunImages + 1;
        mTimeRunImage = 0.0f;
      }
    }
  }

  public void render(GraphicsContext gc) {
    if (this.getJump()) {
      gc.drawImage(mJumpImages.get(mIterJumpImages).getImage(), mPosX, mPosY, mWidth, mHeight);
    } else {
      gc.drawImage(mRunImages.get(mIterRunImages).getImage(), mPosX, mPosY, mWidth, mHeight);
    }
  }

  /**increase the speed*/
  public void incSpeed(float percent) {
    mDeltaChangeRunImages -= mDeltaChangeRunImages * percent;
  }

  public double getGravitation() {
    return mGravitation;
  }

  public void setGravitation(int gravitation) {
    this.mGravitation = gravitation;
    mDeltaChangeJumpImages = mSpeedStartJump / (mGravitation * mAcerbity * 5);
  }

  public double getSpeedStartJump() {
    return mSpeedStartJump;
  }

  public void setSpeedStartJump(int speedStartJump) {
    this.mSpeedStartJump = speedStartJump;
    mDeltaChangeJumpImages = (double) mSpeedStartJump / (mGravitation * mAcerbity * 5);
  }

  public double getAcerbity() {
    return mAcerbity;
  }

  public void setAcerbity(int acerbity) {
    this.mAcerbity = acerbity;
    mDeltaChangeJumpImages = mSpeedStartJump / (mGravitation * mAcerbity * 5);
  }

}

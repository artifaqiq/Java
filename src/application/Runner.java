package application;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * По сути является классом бегущего человека
 */
public class Runner extends Sprite {
	private boolean mIsJump = false;
	private double mSpeedY = 0.0f;
	private double startPosY;
	private double mGravitation = 200.0f;
	private double mSpeedStartJump = 1500.0f;
	private double mDeltaChangeRunImages = .019f;
	private double mAcerbity = 20.0f;
	private double mDeltaChangeJumpImages = mSpeedStartJump / (mGravitation * mAcerbity * 5);
	/**
	 * Коллекциия, содержащая изображения бегещего человечка
	 */
	private ArrayList<ImageView> mRunImages = new ArrayList<>(10);
	/**
	 * Коллекциия, содержащая изображения прыгающего человечка
	 */
	private ArrayList<ImageView> mJumpImages = new ArrayList<>(10);

	public Runner(double startPositionX, double startPositionY) {
		this.startPosY = startPositionY;
		super.mPosX = startPositionX;
		super.mPosY = startPositionY;
		Image tempImage;
		for (int i = 0; i <= 9; i++) {
			tempImage = new Image(getClass().getResourceAsStream("/res/Run__00" + i + ".png"));
			mRunImages.add(new ImageView(tempImage));
			if (super.mWidth >= 0.0f || super.mHeight >= 0.0f) {
				mRunImages.get(i).setFitWidth(mWidth);
				mRunImages.get(i).setFitHeight(mHeight);
			}
		}
		for (int i = 0; i <= 9; i++) {
			tempImage = new Image(getClass().getResourceAsStream("/res/Jump__00" + i + ".png"));
			mJumpImages.add(new ImageView(tempImage));
			if (mWidth >= 0.0f || mHeight >= 0.0f) {
				mJumpImages.get(i).setFitWidth(mWidth);
				mJumpImages.get(i).setFitHeight(mHeight);
			}
		}
	}

	public void setJump(boolean isJump) {
		if (!this.mIsJump) {
			mSpeedY = mSpeedStartJump;
		}
		this.mIsJump = isJump;
	}

	public boolean getJump() {
		return mIsJump;
	}

	/**
	 * Время, прошедшее без смены картинки
	 */
	private double mTimeJumpImage = 0.0f, mTimeRunImage = 0.0f;
	/**
	 * Итераторы, по коллекции изображений человечка
	 */
	private int mIterRunImages = 0, mIterJumpImages = 0;

	/**
	 * Предположительно вызывается из бесконечного цикла и обновляет положение и
	 * другие характеристики объекта
	 * 
	 * @param time
	 *            приращение времени между обновлениями кадров
	 */
	public void update(double time) {
		// если в прыжке
		if (mIsJump == true) {
			// школьная формула кинематики x = x0 + v0t + at2/2
			mPosY = mPosY - mSpeedY * time + mGravitation * time * time / 2;
			// если приземление
			if (mPosY >= startPosY) {
				mIsJump = false;
				mPosY = startPosY;
				mSpeedY = 0.0f;
				mIterJumpImages = 0;
				mTimeJumpImage = 0.0f;
				return;
			}
			// школьная формула v = v0 + a*t
			// здесь переменная mAcerbity использована для улучшения
			// характеристик прыжка
			mSpeedY = mSpeedY - mGravitation * time * mAcerbity;
			mTimeJumpImage += time;
			// если пора менять изображение
			if (mTimeJumpImage >= mDeltaChangeJumpImages) {
				mIterJumpImages = mIterJumpImages == 9 ? 0 : mIterJumpImages + 1;
				mTimeJumpImage = 0.0f;
			}

		}
		// если бежит по земле
		else {
			mTimeRunImage += time;
			if (mTimeRunImage >= mDeltaChangeRunImages) {
				mIterRunImages = mIterRunImages == 9 ? 0 : mIterRunImages + 1;
				mTimeRunImage = 0.0f;
			}

		}
	}

	/**
	 * Отвечает за прорисовку человечка
	 */
	public void render(GraphicsContext gc) {
		if (this.getJump()) {
			gc.drawImage(mJumpImages.get(mIterJumpImages).getImage(), mPosX, mPosY, super.mWidth, super.mHeight);
		} else {
			gc.drawImage(mRunImages.get(mIterRunImages).getImage(), mPosX, mPosY, super.mWidth, super.mHeight);
		}
	}

	public double getGravitation() {
		return mGravitation;
	}

	public void setGravitation(double gravitation) {
		this.mGravitation = gravitation;
		mDeltaChangeJumpImages = mSpeedStartJump / (mGravitation * mAcerbity * 5);
	}

	public double getSpeedStartJump() {
		return mSpeedStartJump;
	}

	public void setSpeedStartJump(double speedStartJump) {
		this.mSpeedStartJump = speedStartJump;
		mDeltaChangeJumpImages = mSpeedStartJump / (mGravitation * mAcerbity * 5);
	}

	public double getAcerbity() {
		return mAcerbity;
	}

	public void setAcerbity(double acerbity) {
		this.mAcerbity = acerbity;
		mDeltaChangeJumpImages = mSpeedStartJump / (mGravitation * mAcerbity * 5);
	}

}

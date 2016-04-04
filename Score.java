
public class Score {
  private boolean mAutoMode;
  private int mScore;
  private int mCountArea;
  
  public Score(boolean autoMode, int score, int countArea) {
    mAutoMode = autoMode;
    mScore = score;
    mCountArea = countArea;
  }
  
  public boolean isAutoMode() {
    return mAutoMode;
  }
  
  public int getScore() {
    return mScore;
  }
  
  public int getCountArea() {
    return mCountArea;
  }
}

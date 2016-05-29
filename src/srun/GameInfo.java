package srun;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;

public class GameInfo {
  private int mScore;
  private int mCountJump;
  private boolean mAutoMode;
  private int mCountArea;
  private int mNumberGame;

  public GameInfo() {}

  public GameInfo(int numberGame, int score, boolean autoMode, int countArea, int countJump) {
    mScore = score;
    mCountJump = countJump;
    mCountArea = countArea;
    mAutoMode = autoMode;
    mNumberGame = numberGame;
  }

  public GameInfo(int numberGame) {
    mNumberGame = numberGame;
    File dir =
        new File(ReplayIO.getReplayDir() + File.separator + "game" + numberGame + File.separator);
    File[] files = dir.listFiles();
    
    boolean hasTranslatedFiles = false;
    for (File temp : files) {
      if (temp.getName().contains("повтор")) {
        hasTranslatedFiles = true;
        break;
      }
    }

    mCountArea = 0;
    for (int i = 0; i < files.length; i++) {
      if (files[i].getName().contains("replay") && !files[i].isDirectory()) {
        if (!hasTranslatedFiles) {
          NotationParser.parse(files[i].getAbsolutePath(),
              files[i].getParentFile().getAbsolutePath() + File.separator + "повтор" + i + ".txt");
        }
        mCountArea++;
      }
    }

    File replay = null;
    BufferedReader reader = null;
    String tempString;

    mCountJump = 0;
    mScore = 0;
    for (int i = 0; i < mCountArea; i++) {
      replay = new File(dir.getAbsolutePath() + File.separator + "replay" + i + ".txt");

      try {
        reader = new BufferedReader(new java.io.FileReader(replay));
        while (true) {
          tempString = reader.readLine();
          if (tempString == null) {
            break;
          } else if (tempString.contains("mode: auto=true")) {
            mAutoMode = true;
          } else if (tempString.contains("mode: auto=false")) {
            mAutoMode = false;
          } else if (tempString.contains("jump:")) {
            mCountJump++;
          } else if (tempString.contains("wall:")) {
            mScore++;
          }
        }
        mScore -= 3;

      } catch (IOException e2) {
        e2.printStackTrace();
      } finally {
        if (reader != null) {
          try {
            reader.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  public int getScore() {
    return mScore;
  }

  public void setScore(int score) {
    this.mScore = score;
  }

  public int getCountJump() {
    return mCountJump;
  }

  public void setCountJump(int countJump) {
    this.mCountJump = countJump;
  }

  public boolean isAutoMode() {
    return mAutoMode;
  }

  public void setAutoMode(boolean autoMode) {
    this.mAutoMode = autoMode;
  }

  public int getCountArea() {
    return mCountArea;
  }

  public void setCountArea(int countArea) {
    this.mCountArea = countArea;
  }

  public int getNumberGame() {
    return mNumberGame;
  }

  public void setNumberGame(int numberGame) {
    this.mNumberGame = numberGame;
  }

  @Override
  public String toString() {
    return "game#" + mNumberGame + ": countArea=" + mCountArea + " score=" + mScore + " auto="
        + mAutoMode + " jumps=" + mCountJump;
  }

  public static class ScoreComp implements Comparator<GameInfo> {
    @Override
    public int compare(GameInfo o1, GameInfo o2) {
      return new Integer(o1.getScore()).compareTo(o2.getScore());
    }

  }

  public static class JumpComp implements Comparator<GameInfo> {
    @Override
    public int compare(GameInfo o1, GameInfo o2) {
      return new Integer(o1.getCountJump()).compareTo(o2.getCountJump());
    }

  }

  public static class AutoComp implements Comparator<GameInfo> {
    @Override
    public int compare(GameInfo o1, GameInfo o2) {
      return new Boolean(o1.isAutoMode()).compareTo(o2.isAutoMode());
    }

  }

  public static class NumberComp implements Comparator<GameInfo> {
    @Override
    public int compare(GameInfo o1, GameInfo o2) {
      return new Integer(o1.getNumberGame()).compareTo(o2.getNumberGame());
    }

  }

  public static class AreaComp implements Comparator<GameInfo> {
    @Override
    public int compare(GameInfo o1, GameInfo o2) {
      return new Integer(o1.getCountArea()).compareTo(o2.getCountArea());
    }

  }
}

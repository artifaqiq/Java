package srun;

import java.io.File;
import java.io.IOException;

public class ReplayIO {

  private static final String REPLAY_DIR_PATH = "replay";

  /**
   * @return path to dir
   */
  public static String createFiles(int countArea) {
    File dir = new File(REPLAY_DIR_PATH + File.separator + "game" + (countGames()));
    dir.mkdir();
    for (int i = 0; i < countArea; i++) {
      File file = new File(dir.getAbsolutePath() + File.separator + "replay" + i + ".txt");
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return dir.getAbsolutePath();
  }

  public static int countGames() {
    int count = 0;
    File dir = new File(REPLAY_DIR_PATH);
    if (!dir.exists()) {
      dir.mkdir();
    }
    File[] files = dir.listFiles();
    for (File temp : files) {
      if (temp.getName().contains("game") && temp.isDirectory()) {
        count++;
      }
    }
    return count;
  }

  public static String getLastGamePath() {
    File file;
    String path = null;
    for (int i = 0;; i++) {
      file = new File(REPLAY_DIR_PATH + File.separator + "game" + i + File.separator);
      if (file.exists() && file.isDirectory()) {
        path = file.getAbsolutePath();
      } else {
        break;
      }
    }
    return path;
  }

  public static String getReplayDir() {
    return REPLAY_DIR_PATH;
  }

}


package srun;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScoreIO {
  private static String mPath = "save" + File.separator + "scores.txt";

  public ScoreIO() {};

  public ScoreIO(String path) {
    mPath = path;
  }

  public static void write(int countArea, boolean autoMode, int score) {
    BufferedWriter bw = null;

    File file = new File(mPath.substring(0, 4));
    if (!file.exists()) {
      file.mkdir();
    }
    try {
      bw = new BufferedWriter(new FileWriter(mPath, true));
      bw.write("score: count=" + countArea);
      bw.write(" auto=" + autoMode);
      bw.write(" score=" + score);
      bw.flush();
      bw.newLine();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } finally {
      if (bw != null) {
        try {
          bw.close();
        } catch (IOException ioe2) {
          System.err.println(ioe2.getMessage());
        }
      }
    }
  }

  public static int findHighScore(boolean isAutoMode, int countArea) {
    Scanner s = null;
    int highScore = 0;
    String tempString;
    Pattern pattern = Pattern.compile("([0-9]+)|true|false");

    try {
      s = new Scanner(new BufferedReader(new FileReader(mPath)));
      while (s.hasNextLine()) {
        tempString = s.nextLine();
        if (tempString == null) {
          break;
        }
        Matcher match = pattern.matcher(tempString);
        match.find();
        if (countArea != new Integer(match.group())) {
          continue;
        }
        match.find();
        if (isAutoMode != new Boolean(match.group())) {
          continue;
        }
        match.find();
        if (new Integer(match.group()) > highScore) {
          highScore = new Integer(match.group());
        }

      }
    } catch (IOException e) {
    } finally {
      if (s != null) {
        s.close();
      }
    }

    return highScore;

  }

  public static List<Integer> getScores(boolean isAutoMode, int countArea) {
    Scanner s = null;
    LinkedList<Integer> scores = new LinkedList<>();
    String tempString;
    Pattern pattern = Pattern.compile("([0-9]+)|true|false");

    try {
      s = new Scanner(new BufferedReader(new FileReader(mPath)));
      while (s.hasNextLine()) {
        tempString = s.nextLine();
        if (tempString == null) {
          break;
        }
        Matcher match = pattern.matcher(tempString);
        match.find();
        if (countArea != new Integer(match.group())) {
          continue;
        }
        match.find();
        if (isAutoMode != new Boolean(match.group())) {
          continue;
        }
        match.find();
        scores.add(new Integer(match.group()));

      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (s != null) {
        s.close();
      }
    }
    return scores;

  }

  public static int getLastScore() {
    Scanner s = null;
    int score = 0;
    String nextString;
    String tempString = null;
    try {
      s = new Scanner(new BufferedReader(new FileReader(mPath)));
      while (s.hasNextLine()) {
        nextString = s.nextLine();
        if (nextString == null) {
          break;
        }
        tempString = nextString;
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (s != null) {
        s.close();
      }
    }

    Pattern pattern = Pattern.compile("score=[0-9]+");
    Matcher match = pattern.matcher(tempString);
    match.find();
    score = new Integer(match.group().split("=")[1]);
    return score;
  }
}

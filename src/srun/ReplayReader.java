package srun;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.paint.Color;

public class ReplayReader {
  public String mPath;

  private int lastWallLine = 0;
  private int lastJumpLine = 3;

  public ReplayReader(String path) {
    mPath = path;
  }

  public WallCoord readNextWall() {
    BufferedReader reader = null;
    WallCoord wall = new WallCoord();

    try {
      reader = new BufferedReader(new java.io.FileReader(mPath));
      for (int i = 0; i < lastWallLine; i++) {
        reader.readLine();
      }
      String tempString;
      for (int i = 1;; i++) {
        tempString = reader.readLine();
        if (tempString == null) {
          return null;
        }
        if (tempString.contains("wall:")) {
          lastWallLine += i;
          break;
        }
      }
      Pattern intValuePattern = Pattern.compile("[0-9]+");
      Matcher matcher = intValuePattern.matcher(tempString);
      matcher.find();
      wall.setDistance(new Integer(matcher.group()));
      matcher.find();
      wall.setWidth(new Integer(matcher.group()));
      matcher.find();
      wall.setHeight(new Integer(matcher.group()));

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
    return wall;
  }

  public boolean hasNextJump() {
    BufferedReader reader = null;
    boolean result = false;

    try {
      reader = new BufferedReader(new java.io.FileReader(mPath));
      for (int i = 0; i < lastJumpLine; i++) {
        reader.readLine();
      }

      String tempString;
      while (true) {
        tempString = reader.readLine();
        if (tempString == null)
          result = false;
        else if (tempString.contains("jump:")) {
          result = true;
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
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
    return result;
  }

  public Color getColor() {
    BufferedReader reader = null;
    Color color = null;

    try {
      reader = new BufferedReader(new java.io.FileReader(mPath));
      String tempString;
      for (int i = 1;; i++) {
        tempString = reader.readLine();
        if (tempString == null) {
          return null;
        } else if (tempString.contains("background:")) {
          lastWallLine += i;
          break;
        }
      }
      Pattern intValuePattern = Pattern.compile("0[xX][0-9a-fA-F]+");
      Matcher matcher = intValuePattern.matcher(tempString);
      matcher.find();
      color = Color.valueOf(matcher.group());

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
    return color;
  }

  public int getJumpPos() {
    BufferedReader reader = null;
    int distanceToFirstWall = 0;

    try {
      reader = new BufferedReader(new java.io.FileReader(mPath));
      for (int i = 0; i < lastJumpLine; i++) {
        reader.readLine();
      }

      String tempString;
      for (int i = 1;; i++) {
        tempString = reader.readLine();
        if (tempString == null)
          return 0;
        if (tempString.contains("jump:")) {
          lastJumpLine += i;
          break;
        }
      }
      Pattern intValuePattern = Pattern.compile("[0-9]+");
      Matcher matcher = intValuePattern.matcher(tempString);
      matcher.find();
      distanceToFirstWall = new Integer(matcher.group());

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
    return distanceToFirstWall;
  }

  public boolean hasNextWall() {
    BufferedReader reader = null;
    boolean result = false;

    try {
      reader = new BufferedReader(new FileReader(mPath));
      for (int i = 0; i < lastJumpLine - 2; i++) {
        reader.readLine();
      }
      result = reader.readLine().contains("wall:");

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
    return result;
  }

  public boolean isAutoMode() {
    boolean result = false;
    Scanner s = null;
    String temp = null;
    Pattern pattern = Pattern.compile("true|false");
    s = new Scanner(mPath);
    for (;;) {
      temp = s.nextLine();
      if (temp.contains("mode:")) {
        break;
      }
    }
    Matcher match = pattern.matcher(temp);
    match.find();
    result = new Boolean(match.group());
    s.close();
    return result;
  }

  public int getDeadPos() {
    BufferedReader reader = null;
    int distanceToFirstWall = 0;

    try {
      reader = new BufferedReader(new java.io.FileReader(mPath));
      for (int i = 0; i < lastWallLine; i++) {
        reader.readLine();
      }

      String tempString = null;
      for (;;) {
        tempString = reader.readLine();
        if (tempString == null || tempString.contains("wall:"))
          return 0;
        if (tempString.contains("dead:")) {
          break;
        }
      }
      Pattern intValuePattern = Pattern.compile("-?[0-9]+");
      Matcher matcher = intValuePattern.matcher(tempString);
      matcher.find();
      distanceToFirstWall = new Integer(matcher.group());

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
    return distanceToFirstWall;
  }
}

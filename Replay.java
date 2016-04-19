class ReplayWriter {
  public String mPath;

  public void writeColor(Color color) {
    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new FileWriter(mPath, true));
      writer.write("background: color=" + color);
      writer.newLine();
      writer.flush();
    } catch (IOException e) {
      System.err.println("write wall coord exception");
      e.printStackTrace();
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException e) {
          System.err.println("close writer exception");
          e.printStackTrace();
        }
      }
    }
  }

  public ReplayWriter(String path) {
    mPath = path;
  }

  public void writeWallCoord(WallCoord wall) {
    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new FileWriter(mPath, true));
      writer.write("wall: distance=" + wall.getDistance() + " width=" + wall.getWidth() + " height="
          + wall.getHeight());
      writer.newLine();
      writer.flush();
    } catch (IOException e) {
      System.err.println("write wall coord exception");
      e.printStackTrace();
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException e) {
          System.err.println("close writer exception");
          e.printStackTrace();
        }
      }
    }
  }

  public void writeJumpTime(long nanoTime) {
    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new FileWriter(mPath, true));
      writer.write("jump: time=" + nanoTime);
      writer.newLine();
      writer.flush();
    } catch (IOException e) {
      System.err.println("write wall coord exception");
      e.printStackTrace();
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException e) {
          System.err.println("close writer exception");
          e.printStackTrace();
        }
      }
    }
  }

}


class ReplayReader {
  public String mPath;

  private int lastWallLine = 0;
  private int lastJumpLine = 0;

  ReplayReader(String path) {
    mPath = path;
  }

  WallCoord readNextWall() {
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

  boolean hasNextJump() {
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

  Color getColor() {
    BufferedReader reader = null;
    Color color = null;

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

  long getJumpTime() {
    BufferedReader reader = null;
    long jumpTime = 0;

    try {
      reader = new BufferedReader(new java.io.FileReader(mPath));
      for (int i = 0; i < lastJumpLine; i++) {
        reader.readLine();
      }

      String tempString;
      for (int i = 1;; i++) {
        tempString = reader.readLine();
        if (tempString == null)
          return 0L;
        if (tempString.contains("jump:")) {
          lastJumpLine += i;
          break;
        }
      }
      Pattern longValuePattern = Pattern.compile("[0-9]+");
      Matcher matcher = longValuePattern.matcher(tempString);
      matcher.find();
      jumpTime = new Long(matcher.group());

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
    return jumpTime;
  }
}


class WallCoord {
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

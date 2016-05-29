package srun;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javafx.scene.paint.Color;

public class ReplayWriter {
  public String mPath;

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
      e.printStackTrace();
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void writeJumpPos(int distanceToFirstWall) {
    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new FileWriter(mPath, true));
      writer.write("jump: distance_to_first_wall=" + distanceToFirstWall);
      writer.newLine();
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void writeColor(Color color) {
    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new FileWriter(mPath, true));
      writer.write("background: color=" + color);
      writer.newLine();
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void writeAutoMode(boolean isAutoMode) {
    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new FileWriter(mPath, true));
      writer.write("mode: auto=" + isAutoMode);
      writer.newLine();
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void writeDeadPos(int pos) {
    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new FileWriter(mPath, true));
      writer.write("dead: distance_to_first_wall=" + pos);
      writer.newLine();
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}

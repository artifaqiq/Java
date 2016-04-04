import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ScoreIO {
  private static final String PATH = "scores.txt";

  public ScoreIO() { };

  public static void write(int countArea, boolean autoMode, int score) {
    BufferedWriter bw = null;

    try {
      bw = new BufferedWriter(new FileWriter(PATH, true));
      bw.write("countArea=" + countArea);
      bw.newLine();
      bw.write("autoMode=" + autoMode);
      bw.newLine();
      bw.write("\nscore=" + score);
      bw.newLine();
      bw.newLine();
      bw.flush();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } finally { // always close the file
      if (bw != null)
        try {
          bw.close();
        } catch (IOException ioe2) {
          System.err.println(ioe2.getMessage());
        }
    }
  }
  
   public static Score read() {
     int countArea = 0;
     int score = 0;
     boolean isAutoMode = true;
     
     BufferedReader br = null;

     String tempString;
     try {
       br = new BufferedReader(new FileReader(PATH));
       tempString = br.readLine();
       System.out.println("tempString = " + tempString);
       countArea = findInt(tempString, 10);
       System.out.println(countArea);
       tempString = br.readLine();
       isAutoMode = findBoolean(tempString, 9);
       tempString = br.readLine();
       score = findInt(tempString, 6);
       
       
       System.out.println(countArea);
     } catch (IOException ioe) {
       ioe.printStackTrace();
     } finally { 
       if (br != null)
         try {
           br.close();
         } catch (IOException ioe2) {
           System.err.println(ioe2.getMessage());
         }
     }
     
     
     
     return new Score(isAutoMode, score, countArea);
     
   }
   
   private static int findInt(String string, int startPos) {
     string = string.substring(startPos);
     int value = Integer.parseInt(string);
     return value;
   }
   
   private static boolean findBoolean(String string, int startPos) {
     string = string.substring(startPos);
     boolean value = Boolean.parseBoolean(string);
     return value;
   }

}

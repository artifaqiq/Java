package srun;

public interface GameListener {
  public void startGame(int countArea, boolean auto, boolean mySelfMode);

  public void showMenu();

  public void replayLastGame();

  public void replayGame(int number);
}

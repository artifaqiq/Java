import scala.collection.immutable.List
import scala.io.Source

import java.io.File
import scala.annotation.tailrec

package srun {

  case class Game(countJump: Int, countWalls: Int, score: Int) {
    def +(s: Game) = {
      Game(countJump + s.countJump, countWalls + s.countWalls, score + s.score);
    }: Game;
  }

  object Statistics {
    def getStat: Game = {
      @tailrec
      def getStatIter(i: Int, accum: Game): Game = {
        if (i == ReplayIO.countGames) accum
        else getStatIter(i + 1, accum + getStatGame(ReplayIO.getReplayDir + 
            File.separator + "game" + i))

      };
      getStatIter(0, Game(0, 0, 0))
    };

    private def getStatGame(dirname: String): Game = {
      val dir: File = new File(dirname)
      val files: Array[File] = dir.listFiles

      @tailrec
      def getStatGameIter(i: Int, accum: Game): Game = {
        if (i == files.length) accum
        else if (files(i).getAbsolutePath.contains("������")) getStatGameIter(i + 1, accum)
        else getStatGameIter(i + 1, accum + getStatFile(files(i).getAbsolutePath))
      };

      getStatGameIter(0, Game(0, 0, 0))
    };

    private def getStatFile(fname: String) = {
      val lines: List[String] = Source.fromFile(fname).getLines().toList
      val walls = lines.filter { x => x.contains("wall:") }
      val jumps = lines.filter { _.contains("jump:") == true }
      Game(jumps.length, walls.length, walls.length - 3)
    }: Game;
  }
}

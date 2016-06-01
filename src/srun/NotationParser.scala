import scala.collection.immutable._
import scala.io._
import java.io.FileNotFoundException
import scala.collection.mutable.MutableList
import java.io.IOException
import java.io.BufferedWriter

package srun {

  sealed trait Action
  case class Walls(distance: Int, height: Int, width: Int) extends Action {
    override def toString = "когда расстояние до последней стены составляло  " + distance + ", высотой " +
    height + " и длиной " + width;
  }
  case class Jump(distance: Int) extends Action {
    override def toString = "когда расстояние до ближайшей стены составляло " + distance;
  }
  case class Dead(distance: Int) extends Action {
    override def toString = "когда расстояние до ближайшей стены составляло " + distance + ". :(";
  }
  case class Color(color: Int) extends Action {
    override def toString = java.lang.Integer.toHexString(color);
  }
  case class Mode(mode: Boolean) extends Action {
    override def toString = {
      if (mode == true) "компьютер"
      else "игрок"
    }
  }

  object NotationParser {

    def parse(inFname: String, outFname: String) {
      writeLine("В этой игре действия происходили так: ", outFname);

      getActions(inFname).reverse.foreach { action =>
        {
          action match {
            case mode: Mode => writeLine("Персонажем управлял " + mode + ";", outFname)
            case color: Color => writeLine("Случайным образом был сгенерирован цвет фона - 0x" + color + ";", outFname);
            case wall: Walls => writeLine("Была простроена стена, " + wall + ";", outFname);
            case jump: Jump => writeLine("Персонаж прыгнул, " + jump + ";", outFname);
            case dead: Dead => writeLine("Но все-таки персонаж умер, " + dead + ";", outFname)
            case _ => ()
          }
        }
      }
    }

    private def getActions(inFname: String): List[Action] = {
      val lines: Array[String] = Source.fromFile(inFname).getLines.toArray

      def iter(i: Int, accum: List[Action]): List[Action] = {
        if (i >= lines.length) accum
        else iter(i + 1, actionFromString(lines(i)) :: accum)
      };

      iter(0, Nil)

    }

    private def actionFromString(s: String): Action = {
      val tokens: Array[String] = s.split(" ")
      tokens(0) match {
        case "wall:" => new Walls(tokens(1).split("=")(1).toInt, tokens(2).split("=")(1).toInt, 
            tokens(3).split("=")(1).toInt)
        case "jump:" => new Jump(tokens(1).split("=")(1).toInt)
        case "dead:" => new Dead(tokens(1).split("=")(1).toInt)
        case "background:" => new Color(Integer.parseInt(
            (tokens(1).split("=")(1).split("x")(1).substring(0, 7)), 16))
        case "mode:" => new Mode(tokens(1).split("=")(1).toBoolean)
        case _ => new Walls(0, 0, 0)

      }
    }

    private def writeLine(s: String, path: String) {
      var writer: java.io.BufferedWriter = null;
      try {
        writer = new java.io.BufferedWriter(new java.io.FileWriter(path, true));
        writer.write(s);
        writer.newLine();
        writer.flush();
      } catch {
        case e: java.io.IOException => println(e)
      } finally {
        if (writer != null) {
          try {
            writer.close();
          } catch {
            case e: java.io.IOException => println(e)
          }
        }
      }
    }

  }

}
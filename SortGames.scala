package srun {

  object SortGames {

    sealed trait Comparator
    case class JumpComp() extends Comparator
    case class ScoreComp() extends Comparator
    case class ModeComp() extends Comparator
    case class LevelComp() extends Comparator
    case class NumberComp() extends Comparator

    def qSortCstyle(xs: Array[GameInfo], comparator: Comparator) = {
      qSort(xs, getComp(comparator))
    }

    private def qSort(xs: Array[GameInfo], comp: ((GameInfo, GameInfo) => Int)) = {
      def swap(i: Int, j: Int) {
        val t = xs(i);
        xs(i) = xs(j);
        xs(j) = t
      }
      def sort1(l: Int, r: Int) {
        val pivot = xs((l + r) / 2)
        var i = l;
        var j = r
        while (i <= j) {
          while (comp(xs(i), pivot) < 0) i += 1
          while (comp(xs(j), pivot) > 0) j -= 1
          if (i <= j) {
            swap(i, j)
            i += 1
            j -= 1
          }
        }
        if (l < j) sort1(l, j)
        if (j < r) sort1(i, r)
      }
      sort1(0, xs.length - 1)
    }

    def getComp(comparator: Comparator): ((GameInfo, GameInfo) => Int) = {
      comparator match {
        case JumpComp() => (o1: GameInfo, o2: GameInfo) => o1.getCountJump - o2.getCountJump
        case ScoreComp() => (o1: GameInfo, o2: GameInfo) => o1.getScore - o2.getScore
        case NumberComp() => (o1: GameInfo, o2: GameInfo) => o1.getNumberGame - o2.getNumberGame
        case LevelComp() => (o1: GameInfo, o2: GameInfo) => o1.getCountArea - o2.getCountArea
        case ModeComp() => (o1: GameInfo, o2: GameInfo) => {
          if (o1.isAutoMode() == o2.isAutoMode()) 0
          else if (o1.isAutoMode() && !o2.isAutoMode()) 1
          else -1
        }

      }
    };

  }
}

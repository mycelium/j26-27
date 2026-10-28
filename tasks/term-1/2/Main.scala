package recfun

object Main {
  def main(args: Array[String]): Unit = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row) print(pascal(col, row) + " ")
      println()
    }

    println(s"Balanced: ${balance("(a + b) * (c - d)".toList)}")
    println(s"Change for 4 with coins 1 and 2: ${countChange(4, List(1, 2))}")
    println("4 queens:")
    nQueens(4) match {
      case Some(queens) => printBoard(queens)
      case None => println("No solution")
    }
  }

  def pascal(c: Int, r: Int): Int = {
    if (c < 0 || c > r) 0
    else if (c == 0 || c == r) 1
    else pascal(c - 1, r - 1) + pascal(c, r - 1)
  }

  def balance(chars: List[Char]): Boolean = {
    def loop(rest: List[Char], opened: Int): Boolean = rest match {
      case Nil => opened == 0
      case '(' :: tail => loop(tail, opened + 1)
      case ')' :: tail => opened > 0 && loop(tail, opened - 1)
      case _ :: tail => loop(tail, opened)
    }

    loop(chars, 0)
  }

  def countChange(money: Int, coins: List[Int]): Int = {
    def ways(amount: Int, available: List[Int]): Int = {
      if (amount == 0) 1
      else if (amount < 0 || available.isEmpty) 0
      else ways(amount - available.head, available) + ways(amount, available.tail)
    }

    ways(money, coins.distinct.filter(_ > 0))
  }

  def nQueens(size: Int): Option[Array[Int]] = {
    def safe(column: Int, placed: List[Int], distance: Int): Boolean = placed match {
      case Nil => true
      case previous :: rest =>
        column != previous && math.abs(column - previous) != distance &&
          safe(column, rest, distance + 1)
    }

    def place(row: Int, placed: List[Int]): Option[List[Int]] = {
      if (row == size) Some(placed.reverse)
      else {
        // для этой строки пробуем колонки по очереди
        def tryColumn(column: Int): Option[List[Int]] = {
          if (column == size) None
          else if (safe(column, placed, 1))
            place(row + 1, column :: placed).orElse(tryColumn(column + 1))
          else tryColumn(column + 1)
        }

        tryColumn(0)
      }
    }

    if (size < 0) None else place(0, Nil).map(_.toArray)
  }

  def printBoard(queens: Array[Int]): Unit = {
    for (row <- queens.indices) {
      println(queens.indices.map(col => if (queens(row) == col) 1 else 0).mkString(" "))
    }
  }
}

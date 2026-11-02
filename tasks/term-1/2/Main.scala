package recfun

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }

    println("\nParentheses Balancing")
    println(s"(a + b) * (c - d): ${balance("(a + b) * (c - d)".toList)}")
    println(s"())(: ${balance("())(".toList)}")

    println("\nCounting Change")
    println(s"5 with coins 2 and 3: ${countChange(5, List(2, 3))}")

    println("\nN-Queens")
    nQueens(4) match {
      case Some(solution) =>
        println(solution.mkString("[", ", ", "]"))
        printBoard(solution)
      case None =>
        println("Решение не найдено")
    }
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    (c, r) match {
      case (column, row) if column < 0 || row < 0 || column > row =>
        0
      case (column, row) if column == 0 || column == row =>
        1
      case (column, row) =>
        pascal(column - 1, row - 1) + pascal(column, row - 1)
    }
  }

  /**
   * Exercise 2 Parentheses Balancing
   */
  def balance(chars: List[Char]): Boolean = {
    def loop(remaining: List[Char], opened: Int): Boolean = {
      remaining match {
        case Nil =>
          opened == 0
        case '(' :: tail =>
          loop(tail, opened + 1)
        case ')' :: _ if opened == 0 =>
          false
        case ')' :: tail =>
          loop(tail, opened - 1)
        case _ :: tail =>
          loop(tail, opened)
      }
    }

    loop(chars, 0)
  }

  /**
   * Exercise 3 Counting Change
   * Write a recursive function that counts how many different ways you can make
   * change for an amount, given a list of coin denominations. For example,
   * there is 1 way to give change for 5 if you have coins with denomiation
   * 2 and 3: 2+3.
   */
  def countChange(money: Int, coins: List[Int]): Int = {
    (money, coins) match {
      case (0, _) =>
        1
      case (amount, _) if amount < 0 =>
        0
      case (_, Nil) =>
        0
      case (amount, coin :: tail) if coin <= 0 =>
        countChange(amount, tail)
      case (amount, coin :: tail) =>
        countChange(amount, tail) + countChange(amount - coin, coin :: tail)
    }
  }
  
  /**
   * Excerice 4 N-Queens Problem
   * Write a function that provides a solution for n-queens problem if possible
   * Input parameter represents board size and number of queens
   * Output is an array: index represents row number and value represents column number.
   * Example: nQueens(4): [1,3,0,2] or
   * 0 1 0 0
   * 0 0 0 1
   * 1 0 0 0
   * 0 0 1 0
   */

  def nQueens(size: Int): Option[Array[Int]] = {
    def isSafe(column: Int, placedQueens: List[Int]): Boolean = {
      def check(queens: List[Int], rowDistance: Int): Boolean = {
        queens match {
          case Nil =>
            true
          case placedColumn :: _
              if column == placedColumn || Math.abs(column - placedColumn) == rowDistance =>
            false
          case _ :: tail =>
            check(tail, rowDistance + 1)
        }
      }

      check(placedQueens, 1)
    }

    def placeQueens(row: Int, placedQueens: List[Int]): Option[List[Int]] = {
      if (row == size) {
        Some(placedQueens.reverse)
      } else {
        def tryColumn(column: Int): Option[List[Int]] = {
          if (column == size) {
            None
          } else if (!isSafe(column, placedQueens)) {
            tryColumn(column + 1)
          } else {
            placeQueens(row + 1, column :: placedQueens) match {
              case solution @ Some(_) =>
                solution
              case None =>
                tryColumn(column + 1)
            }
          }
        }

        tryColumn(0)
      }
    }

    if (size < 0) {
      None
    } else {
      placeQueens(0, Nil).map(_.toArray)
    }
  }

  def printBoard(solution: Array[Int]): Unit = {
    val size = solution.length
    solution.foreach { queenColumn =>
      println((0 until size).map { column =>
        if (column == queenColumn) 1 else 0
      }.mkString(" "))
    }
  }

}

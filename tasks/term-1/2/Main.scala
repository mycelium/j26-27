package recfun
import common._

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }

    // Тесты для функций
    println("\nBalance: " + balance("(if (zero? x) max (/ 1 x))".toList))
    println("Balance: " + balance("I told him (that it's not (yet) done). (But he wasn't listening)".toList))
    println("Balance: " + balance(":-)".toList))
    println("Balance: " + balance("())(".toList))

    println("\nCountChange (money=300): " + countChange(300, List(5, 10, 20, 50, 100, 200, 500)))
    
    println("\nN-Queens (8x8):")
    nQueens(8).foreach(printBoard)
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if (c < 0 || c > r) 0
    else if (c == 0 || c == r) 1
    else pascal(c - 1, r - 1) + pascal(c, r - 1)
  }

  /**
   * Exercise 2 Parentheses Balancing
   */
  def balance(chars: List[Char]): Boolean = {
    def loop(chars: List[Char], count: Int): Boolean = {
      if (count < 0) false
      else chars match {
        case Nil => count == 0
        case '(' :: tail => loop(tail, count + 1)
        case ')' :: tail => loop(tail, count - 1)
        case _ :: tail => loop(tail, count)
      }
    }
    loop(chars, 0)
  }

  /**
   * Exercise 3 Counting Change
   */
  def countChange(money: Int, coins: List[Int]): Int = {
    if (money == 0) 1
    else if (money < 0 || coins.isEmpty) 0
    else countChange(money - coins.head, coins) + countChange(money, coins.tail)
  }
  
  /**
   * Excerice 4 N-Queens Problem
   */
  def nQueens(size: Int): Option[Array[Int]] = {
    // queens: столбцы уже поставленных ферзей для строк 0, 1, ..., row-1
    def isSafe(col: Int, row: Int, queens: List[Int]): Boolean =
      queens.zipWithIndex.forall { case (queenCol, queenRow) =>
        col != queenCol && math.abs(col - queenCol) != row - queenRow
      }

    def place(row: Int, queens: List[Int]): Option[List[Int]] = {
      if (row == size) {
        Some(queens)
      } else {
        def tryColumns(columns: List[Int]): Option[List[Int]] = columns match {
          case Nil => None
          case col :: tail =>
            if (isSafe(col, row, queens)) {
              place(row + 1, queens :+ col) match {
                case Some(solution) => Some(solution)
                case None           => tryColumns(tail)
              }
            } else {
              tryColumns(tail)
            }
        }

        tryColumns((0 until size).toList)
      }
    }

    place(0, Nil).map(_.toArray)
  }

  // Функция для печати доски
  def printBoard(board: Array[Int]): Unit = {
    val size = board.length
    for (row <- 0 until size) {
      for (col <- 0 until size) {
        if (board(row) == col) print("1 ") else print("0 ")
      }
      println()
    }
    println()
  }
}
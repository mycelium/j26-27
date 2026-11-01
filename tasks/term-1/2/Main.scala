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
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if (c == 0 || c == r) 1
    else pascal(c - 1, r - 1) + pascal(c, r - 1)
  }

  /**
   * Exercise 2 Parentheses Balancing
   */
  def balance(chars: List[Char]): Boolean = {
    def loop(chars: List[Char], open: Int): Boolean = {
      if (chars.isEmpty) open == 0
      else if (chars.head == '(') loop(chars.tail, open + 1)
      else if (chars.head == ')') open > 0 && loop(chars.tail, open - 1)
      else loop(chars.tail, open)
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
    if (money == 0) 1
    else if (money < 0 || coins.isEmpty) 0
    else countChange(money - coins.head, coins) + countChange(money, coins.tail)
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
    // queens - columns of already placed queens, the last placed row goes first
    def isSafe(queens: List[Int], col: Int): Boolean = {
      queens.zipWithIndex.forall { case (q, i) =>
        q != col && math.abs(q - col) != i + 1
      }
    }

    def place(row: Int, queens: List[Int]): Option[List[Int]] = {
      if (row == size) Some(queens.reverse)
      else tryColumn(row, 0, queens)
    }

    def tryColumn(row: Int, col: Int, queens: List[Int]): Option[List[Int]] = {
      if (col == size) None
      else if (isSafe(queens, col)) {
        place(row + 1, col :: queens) match {
          case Some(solution) => Some(solution)
          case None => tryColumn(row, col + 1, queens)
        }
      }
      else tryColumn(row, col + 1, queens)
    }

    if (size <= 0) None
    else place(0, Nil).map(_.toArray)
  }

  def printBoard(queens: Array[Int]): Unit = {
    for (row <- queens.indices) {
      val line = for (col <- queens.indices) yield if (queens(row) == col) 1 else 0
      println(line.mkString(" "))
    }
  }

}

package recfun
// import common._

object Main {
  def main(args: Array[String]): Unit = {
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
    if (c < 0 || r < 0 || c > r) 0
    else if (c == 0 || c == r) 1
    else pascal(c, r - 1) + pascal(c - 1, r - 1)
  }

  /**
   * Exercise 2 Parentheses Balancing
   */
  def balance(chars: List[Char]): Boolean = {
    def calculateDebt(chars: List[Char], debt: Int): Boolean = {
      if (debt < 0) false
      else chars match {
        case '(' :: tail  => calculateDebt(tail, debt + 1)
        case ')' :: tail  => calculateDebt(tail, debt - 1)
        case Nil          => debt == 0
        case _ :: tail    => calculateDebt(tail, debt)
      }
    }

    calculateDebt(chars, 0)
  }

  /**
   * Exercise 3 Counting Change
   * Write a recursive function that counts how many different ways you can make
   * change for an amount, given a list of coin denominations. For example,
   * there is 1 way to give change for 5 if you have coins with denomiation
   * 2 and 3: 2+3.
   */
  def countChange(money: Int, coins: List[Int]): Int = {
    def count(left: Int, available: List[Int]): Int = {
      if (left == 0) 1
      else if (left < 0) 0
      else available match {
        case coin :: tail => count(left - coin, available) + count(left, tail)
        case Nil => 0
      }
    }

    count(money, coins.filter(_ > 0).distinct)
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
    if (size < 0) None
    else {

      // true, if a queen can be placed in the next row at column col
      def isSafe(cols: List[Int], col: Int): Boolean = {
        def check(placed: List[Int], dist: Int): Boolean = {
          if (placed.isEmpty) true
          else {
            val c = placed.head
            if (c == col || math.abs(c - col) == dist) false
            else check(placed.tail, dist + 1)
          }
        }
        check(cols, 1)
      }

      // places queens row by row; cols stores columns of already placed
      // queens, newest row first
      def place(row: Int, cols: List[Int]): Option[List[Int]] = {
        if (row == size) Some(cols)
        else {
          def tryCol(col: Int): Option[List[Int]] = {
            if (col == size) None
            else if (isSafe(cols, col)) {
              val res = place(row + 1, col :: cols)
              if (res.isDefined) res else tryCol(col + 1)
            }
            else tryCol(col + 1)
          }
          tryCol(0)
        }
      }

      place(0, Nil).map(cols => cols.reverse.toArray)
    }
  }

  // prints the board: 1 - queen, 0 - empty cell
  def printBoard(cols: Array[Int]): Unit = {
    for (row <- 0 until cols.length) {
      for (col <- 0 until cols.length)
        if (cols(row) == col) print("1 ") else print("0 ")
      println()
    }
  }

}

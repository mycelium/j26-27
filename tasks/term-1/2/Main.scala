package recfun
import common._

object Main {
  def main(args: Array[String]) =   {
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
    else pascal(c-1,r-1) + pascal(c,r-1)
  }

  /**
   * Exercise 2 Parentheses Balancing
   */
    def balance(chars: List[Char]): Boolean = {

      def helper(remaining: List[Char], count:Int): Boolean = {

          if (count < 0) return false

          remaining match{
            case Nil => count == 0
            case '(':: tail => helper(tail,count + 1)
            case ')' :: tail => helper(tail,count - 1)
            case _ :: tail => helper(tail,count)
          }
      }

      helper(chars,0)
    }

  /**
   * Exercise 3 Counting Change
   * Write a recursive function that counts how many different ways you can make
   * change for an amount, given a list of coin denominations. For example,
   * there is 1 way to give change for 5 if you have coins with denomiation
   * 2 and 3: 2+3.
   */
  def countChange(money: Int, coins: List[Int]): Int = {

    if (money == 0) return 1
    if (money <  0) return 0

    coins match{
      case Nil => if (money == 0) 1 else 0
      case head :: tail => countChange(money - head, coins) + countChange(money, tail)
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


  def printSolution(): Unit = {

    val board = nQueens(4)

    board match {
      case None => println("No solution")
      case Some(queens) => printBoard(queens)
    }
  }

   def printBoard(queens: Array[Int]): Unit = {

    for (row <- 0.until(queens.length)) {
      for (col <- 0.until(queens.length)) {

        if (queens(row) == col) {
          print("1 ")
        } else {
          print("0 ")
        }
      }

      println()
    }
  }

  def nQueens(size: Int): Option[Array[Int]] = {

    val queens = Array.fill(size)(-1)

    def helper(row: Int): Boolean = {

      if (row == size) return true

      for (col <- 0 to (size - 1)) {
        if (isSafe(row, col, queens)) {
          queens(row) = col
          if (helper(row + 1)) return true
        }
      }

    false
    }

    if (helper(0)) Some(queens)
    else None

  }

    def isSafe(row: Int, col: Int, queens: Array[Int]): Boolean = {

    for (queen_row <- 0 to (row - 1)) {

      if (col == queens(queen_row)) return false
      if (math.abs(row - queen_row) == math.abs(col - queens(queen_row))) return false
    }

    true
  }

}








package recfun

object Main {
  def main(args: Array[String]): Unit = {
    println("Exercise 1. Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row) {
        print(pascal(col, row) + "\t")
      }
      println()
    }
    println()
  }

  /**
   * Exercise 1. Pascal's Triangle
   */
  def pascal(c: Int, r: Int): Int = {
    if (c == 0 || c == r) {
      1
    }
    else {
      pascal(c - 1, r - 1) + pascal(c, r - 1)
    }
  }

  /**
   * Exercise 2 Parentheses Balancing
   */
  def balance(chars: List[Char]): Boolean = {
    ???
  }

  /**
   * Exercise 3 Counting Change
   * Write a recursive function that counts how many different ways you can make
   * change for an amount, given a list of coin denominations. For example,
   * there is 1 way to give change for 5 if you have coins with denomiation
   * 2 and 3: 2+3.
   */
  def countChange(money: Int, coins: List[Int]): Int = {
    ???
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
    ???
  }

}
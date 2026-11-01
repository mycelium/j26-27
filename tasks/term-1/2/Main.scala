package recfun
import common._
import scala.collection.mutable.Stack
import scala.io.StdIn.readLine

object Main {
  def main(args: Array[String]) = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }

    println("\nParentheses Balancing")
    println(balance(List('(', '(', ')', ')')))
    println(balance(List('(', ')', ')')))

    println("\nCounting Change")
    println(countChange(5, List(2, 3)))

    println("\nN-Queens")
    println(nQueens(4).map(_.mkString("[", ", ", "]")))

  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if (c == 0 || c == r) 1
    else pascal (c - 1, r - 1) + pascal (c, r - 1)
  }

  /**
   * Exercise 2 Parentheses Balancing
   */
  def balance(chars: List[Char]): Boolean = {
    val stack = Stack[Char]()

    for (char <- chars) {
      if (char == '(') {
        stack.push(char)
      }
      else if (char == ')') {
        if (stack.nonEmpty) {
          stack.pop()
        }
        else {
          return false
        }
      }
    }

    stack.isEmpty
  }

  /**
   * Exercise 3 Counting Change
   * Write a recursive function that counts how many different ways you can make
   * change for an amount, given a list of coin denominations. For example,
   * there is 1 way to give change for 5 if you have coins with denomiation
   * 2 and 3: 2+3.
   */
  def countChange(money: Int, coins: List[Int]): Int = {
    if (money < 0) 0
    else if (money == 0) 1
    else if (coins.isEmpty) 0
    else {
       countChange(money, coins.tail)  + countChange(money - coins.head, coins)
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
    if (size <= 0) {
      None
    }
    else {
      val board = Array.fill(size, size)(0)

      def isSafe(row: Int, col: Int): Boolean = {
        (0 until col).forall { i =>
          val rowSafe = board(row)(i) == 0
          val upperDiagSafe =
            if (row - (col - i) >= 0)
              board(row - (col - i))(i) == 0
            else true

          val lowerDiagSafe =
            if (row + (col - i) < board.length)
              board(row + (col - i))(i) == 0
            else true

          rowSafe && upperDiagSafe && lowerDiagSafe
        }
      }

      def backtrack(col: Int): Boolean = {
        if (col >= size) return true

        for (row <- 0 until size) {
          if (isSafe(row, col)) {
            board(row)(col) = 1

            if (backtrack(col + 1)) return true

            board(row)(col) = 0
          }
        }
        false
      }

      if (!backtrack(0)) {
        None
      }
      else {
        val result = (0 until size)
          .map(row => board(row).indexOf(1))
          .toArray

        Some(result)
      }
    }
  }
}
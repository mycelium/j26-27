//> using scala 2.13
package recfun
//import common._

object Main {
  def main(args: Array[String]): Unit = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
    println("\nBalance")
    println(balance("(if (zero? x) max (/ 1 x))".toList))   // true
    println(balance(":-)".toList))                          // false
    println(balance("())(".toList))                         // false

    println("\nCounting Change")
    println(countChange(4, List(1, 2)))                                // 3
    println(countChange(300, List(5, 10, 20, 50, 100, 200, 500)))      // 1022

    for (size <- List(4, 8, 3)) {
      println(s"\nnQueens($size)")
      nQueens(size) match {
        case Some(board) =>
          println(board.mkString("[", ", ", "]"))
          printBoard(board)
        case None =>
          println("Решения нет")
      }
    }
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
    def balanceCount(chars: List[Char], open: Int): Boolean = {
      if (open < 0) false
      else if (chars.isEmpty) open == 0
      else if (chars.head == '(') balanceCount(chars.tail, open + 1)
      else if (chars.head == ')') balanceCount(chars.tail, open - 1)
      else balanceCount(chars.tail, open)
    }
    balanceCount(chars, 0)
    
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

    def isSafe(queens: List[Int], col: Int): Boolean = {
      val row = queens.length

      def check(r: Int): Boolean = {
        if (r == row) true
        else if (queens(r) == col || math.abs(queens(r) - col) == row - r) false
        else check(r + 1)
      }
      check(0)
    }

    def place(queens: List[Int]): Option[List[Int]] = {

      def tryCol(col: Int): Option[List[Int]] =
          if (col == size) None                                 
          else if (!isSafe(queens, col)) tryCol(col + 1)           
          else place(queens :+ col).orElse(tryCol(col + 1))  

      if (queens.length == size) Some(queens)                         
      else tryCol(0)                                       
    }

    place(List()).map(_.toArray)

  }

  def printBoard(board: Array[Int]): Unit = {
    val size = board.length

    def printCell(row: Int, col: Int): Unit =
      if (col < size) {
        print(if (board(row) == col) "1 " else "0 ")
        printCell(row, col + 1)              
      }

    def printRow(row: Int): Unit =
      if (row < size) {
        printCell(row, 0)              
        println()                       
        printRow(row + 1)                    
      }

    printRow(0)
  }
 
}

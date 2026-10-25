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
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if (c < 0 || r < 0 || c > r) 0
    else if (c == 0 || c == r) 1
    else pascal(c - 1, r - 1) + pascal(c, r - 1)
  }

  /**
   * Exercise 2 Parentheses Balancing
   */
  def balance(chars: List[Char]): Boolean = {
    def checkChar(chars: List[Char], openedParentheses: Int): Boolean = chars match {
      case Nil => openedParentheses == 0
      case '(' :: tail => checkChar(tail, openedParentheses + 1)
      case ')' :: tail => openedParentheses > 0 && checkChar(tail, openedParentheses - 1)
      case _ :: tail => checkChar(tail, openedParentheses)
    }
    checkChar(chars, 0)
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
    else coins match {
      case Nil => 0
      case coin :: tail => countChange(money - coin, coins) + countChange(money, tail)
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
    def isSafeToPlace(column: Int, queens: List[Int]): Boolean = {
      def loopFunc(qs: List[Int], dist: Int): Boolean = qs match {
        case Nil => true
        case q :: tail => q != column && math.abs(q - column) != dist && loopFunc(tail, dist + 1)
      }
      loopFunc(queens, 1)
    }
    def placeQueen(row: Int, queens: List[Int]): Option[List[Int]] =
      if (row == size) Some(queens.reverse)
      else {
        def tryColumns(columns: List[Int]): Option[List[Int]] = columns match {
          case Nil => None
          case column :: rest if !isSafeToPlace(column, queens) => tryColumns(rest)
          case column :: rest => 
            placeQueen(row + 1, column :: queens) match {
              case Some(sol) => Some(sol)
              case None => tryColumns(rest)
            }
        }
        tryColumns((0 until size).toList)
      }
    placeQueen(0, Nil).map(_.toArray)
  }

  def printBoard(optionQueens: Option[Array[Int]]): Unit = {
    optionQueens match {
      case Some(queens) => 
        val n = queens.length
        for (row <- 0 until n) {
          for (col <- 0 until n) {
            if (queens(row) == col) print("1 ")
            else print("0 ")
          }
          println()
        }
      case None => println()
    }
  }
}

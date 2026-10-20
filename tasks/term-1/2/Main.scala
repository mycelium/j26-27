package recfun

object Main {
  def main(args: Array[String]): Unit = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row).toString + " ")
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
    def check(rest: List[Char], open: Int): Boolean = {
      if (open < 0) false
      else rest match {
        case Nil => open == 0
        case '(' :: tail => check(tail, open + 1)
        case ')' :: tail => check(tail, open - 1)
        case _ :: tail => check(tail, open)
      }
    }

    check(chars, 0)
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
        case Nil => 0
        //use this coin again, or move on to the next one
        case coin :: tail => count(left - coin, available) + count(left, tail)
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
    def safe(column: Int, queens: List[Int], distance: Int): Boolean = queens match {
      case Nil => true
      case previous :: tail =>
        column != previous &&
          math.abs(column - previous) != distance &&
          safe(column, tail, distance + 1)
    }

    def place(row: Int, queens: List[Int]): Option[Array[Int]] = {
      if (row == size) Some(queens.reverse.toArray)
      else {
        def tryColumn(column: Int): Option[Array[Int]] = {
          if (column >= size) None
          else if (!safe(column, queens, 1)) tryColumn(column + 1)
          else {
            // try the next row,  go back if it doesn't work
            place(row + 1, column :: queens) match {
              case solution @ Some(_) => solution
              case None => tryColumn(column + 1)
            }
          }
        }

        tryColumn(0)
      }
    }

    if (size < 0) None
    else place(0, Nil)
  }
  //
  def printBoard(queens: Array[Int]): Unit = {
    def printRow(row: Int, column: Int): Unit = {
      if (column == queens.length) println()
      else {
        if (column > 0) print(" ")
        print(if (queens(row) == column) 1 else 0)
        printRow(row, column + 1)
      }
    }
  //
    def printRows(row: Int): Unit = {
      if (row < queens.length) {
        printRow(row, 0)
        printRows(row + 1)
      }
    }

    printRows(0)
  }
}

package recfun
//import common._

object Main {
  def main(args: Array[String]) = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }

    println("Parentheses balancing")
    println(balance("(a + b)".toList))

    println("Counting change")
    println(countChange(5, List(1, 2, 5)))

    println("N-Queens")
    nQueens(4).foreach(solution => printBoard(solution))
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if (r < 0 || c < 0 || c > r) 0
    else if (c == 0 || c == r) 1
    else pascal(c - 1, r - 1) + pascal(c, r - 1)
  }

  /**
   * Exercise 2 Parentheses Balancing
   */
  def balance(chars: List[Char]): Boolean = {
    def loop(rest: List[Char], open: Int): Boolean = {
      if (open < 0) false
      else rest match {
        case Nil => open == 0
        case '(' :: tail => loop(tail, open + 1)
        case ')' :: tail => loop(tail, open - 1)
        case _ :: tail => loop(tail, open)
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
    if (money < 0) 0
    else if (money == 0) 1
    else coins match {
      case Nil => 0
      case coin :: tail =>
        countChange(money - coin, coins) + countChange(money, tail)
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
    if (size < 0) None
    else {
      def isSafe(column: Int, queens: List[Int]): Boolean = {
        def check(remaining: List[Int], distance: Int): Boolean = remaining match {
          case Nil => true
          case queen :: tail =>
            queen != column && math.abs(queen - column) != distance && check(tail, distance + 1)
        }

        check(queens, 1)
      }

      def place(row: Int, queens: List[Int]): Option[List[Int]] = {
        if (row == size) Some(queens.reverse)
        else {
          def tryColumn(column: Int): Option[List[Int]] = {
            if (column == size) None
            else if (isSafe(column, queens))
              place(row + 1, column :: queens).orElse(tryColumn(column + 1))
            else tryColumn(column + 1)
          }

          tryColumn(0)
        }
      }

      place(0, Nil).map(_.toArray)
    }
  }

  def printBoard(solution: Array[Int]): Unit = {
    def printRow(row: Int): Unit = {
      if (row < solution.length) {
        def printColumn(column: Int): Unit = {
          if (column < solution.length) {
            print(if (solution(row) == column) "1 " else "0 ")
            printColumn(column + 1)
          }
        }

        printColumn(0)
        println()
        printRow(row + 1)
      }
    }

    printRow(0)
  }

}

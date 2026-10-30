package recfun
//import common._

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }

    println("balance")
    println(balance("(if (zero? x) max (/ 1 x))".toList))
    println(balance("I told him (that it's not (yet) done). (But he wasn't listening)".toList))
    println(balance(":-)".toList))
    println(balance("())(".toList))

    println("countChange")
    println(countChange(5, List(2, 3)))
    println(countChange(300, List(5, 10, 20, 50, 100, 200, 500)))

    println("nQueens")
    nQueens(8) match {
      case Some(sol) =>
        println(sol.mkString(", "))
        printBoard(sol)
      case None =>
        println("No solution for size 8")
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
    def loop(rest: List[Char], open: Int): Boolean = {
      if (open < 0) false
      else rest match {
        case Nil         => open == 0
        case '(' :: tail => loop(tail, open + 1)
        case ')' :: tail => loop(tail, open - 1)
        case _ :: tail   => loop(tail, open)
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
    if (money == 0) 1
    else if (money < 0 || coins.isEmpty) 0
    else countChange(money, coins.tail) + countChange(money - coins.head, coins)
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
    if (size <= 0) None
    else {
      val queens = new Array[Int](size)

      def isSafe(row: Int, col: Int): Boolean = {
        def check(prevRow: Int): Boolean = {
          if (prevRow >= row) true
          else if (queens(prevRow) == col) false
          else if (math.abs(queens(prevRow) - col) == row - prevRow) false
          else check(prevRow + 1)
        }
        check(0)
      }

      def place(row: Int): Boolean = {
        if (row == size) true
        else {
          def tryCol(col: Int): Boolean = {
            if (col >= size) false
            else if (isSafe(row, col)) {
              queens(row) = col
              if (place(row + 1)) true
              else tryCol(col + 1)
            }
            else tryCol(col + 1)
          }
          tryCol(0)
        }
      }

      if (place(0)) Some(queens.clone())
      else None
    }
  }

  def printBoard(solution: Array[Int]): Unit = {
    val n = solution.length
    for (row <- 0 until n) {
      println((0 until n).map(col => if (solution(row) == col) "1" else "0").mkString(" "))
    }
  }
}
package recfun

object Main {
  def main(args: Array[String]): Unit = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }

    println()
    println("Balance")
    println(balance("(if (zero? x) max (/ 1 x))".toList))                                  // true
    println(balance("I told him (that it's not (yet) done). (But he wasn't listening)".toList)) // true
    println(balance(":-)".toList))                                                         // false
    println(balance("())(".toList))                                                        // false
    println(balance("(()".toList))                                                         // false
    println(balance("".toList))                                                            // true

    println()
    println("Count change")
    println(countChange(5, List(2, 3)))                                  // 1
    println(countChange(4, List(1, 2)))                                  // 3
    println(countChange(0, List(1, 2)))                                  // 1
    println(countChange(3, List(2)))                                     // 0
    println(countChange(300, List(5, 10, 20, 50, 100, 200, 500)))        // 1022

    println()
    println("N-Queens")
    for (size <- 1 to 8) {
      nQueens(size) match {
        case Some(queens) => println(size + ": " + queens.mkString("[", ",", "]"))
        case None => println(size + ": no solution")
      }
    }

    println()
    println("Board 4x4:")
    nQueens(4).foreach(printBoard)
    println()
    println("Board 8x8:")
    nQueens(8).foreach(printBoard)
  }

  /**
   * Exercise 1
   * A number in the triangle is the sum of the two numbers above it.
   * The numbers on the edges (first and last in the row) are 1.
   */
  def pascal(c: Int, r: Int): Int = {
    if (c < 0 || c > r) throw new IllegalArgumentException("column must be from 0 to row")
    else if (c == 0 || c == r) 1
    else pascal(c - 1, r - 1) + pascal(c, r - 1)
  }

  /**
   * Exercise 2 Parentheses Balancing
   * I go through the list and count how many "(" are still open.
   * "(" adds 1, ")" subtracts 1. If the count gets below 0, a ")" has no
   * pair, so the answer is false. At the end the count must be 0.
   */
  def balance(chars: List[Char]): Boolean = {
    def check(rest: List[Char], open: Int): Boolean = {
      if (open < 0) false
      else if (rest.isEmpty) open == 0
      else if (rest.head == '(') check(rest.tail, open + 1)
      else if (rest.head == ')') check(rest.tail, open - 1)
      else check(rest.tail, open)
    }
    check(chars, 0)
  }

  /**
   * Exercise 3 Counting Change
   * Write a recursive function that counts how many different ways you can make
   * change for an amount, given a list of coin denominations. For example,
   * there is 1 way to give change for 5 if you have coins with denomiation
   * 2 and 3: 2+3.
   *
   * Idea: take the first coin. Either we use it (and the money becomes
   * money - coin, the same coins stay), or we never use it again (we throw
   * it away and keep the same money). The answer is the sum of both ways.
   */
  def countChange(money: Int, coins: List[Int]): Int = {
    if (coins.exists(coin => coin <= 0))
      throw new IllegalArgumentException("coins must be positive numbers")

    def count(m: Int, cs: List[Int]): Int = {
      if (m == 0) 1                 // we paid exactly, this is one way
      else if (m < 0) 0             // we paid too much
      else if (cs.isEmpty) 0        // no coins left, but money is left
      else count(m - cs.head, cs) + count(m, cs.tail)
    }
    count(money, coins)
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
   *
   * Idea: put the queens row by row. In each row try the columns from left
   * to right. If a column is safe, go to the next row. If the next rows
   * can not be finished, come back and try the next column (backtracking).
   */
  def nQueens(size: Int): Option[Array[Int]] = {


    def isSafe(col: Int, placed: List[Int]): Boolean = {
      def check(rest: List[Int], dist: Int): Boolean = {
        if (rest.isEmpty) true
        else if (rest.head == col || math.abs(rest.head - col) == dist) false
        else check(rest.tail, dist + 1)
      }
      check(placed, 1)
    }

    // tries to fill the rows from row to the end
    def placeRow(row: Int, placed: List[Int]): Option[List[Int]] = {
      if (row == size) Some(placed)
      else tryColumn(0, row, placed)
    }

    // tries the columns from col to the right in the row "row"
    def tryColumn(col: Int, row: Int, placed: List[Int]): Option[List[Int]] = {
      if (col == size) None
      else if (isSafe(col, placed)) {
        placeRow(row + 1, col :: placed) match {
          case Some(solution) => Some(solution)
          case None => tryColumn(col + 1, row, placed)
        }
      }
      else tryColumn(col + 1, row, placed)
    }

    if (size < 1) None
    else placeRow(0, Nil).map(solution => solution.reverse.toArray)
  }

  /**
   * Prints the board: 1 is a queen, 0 is an empty cell.
   */
  def printBoard(queens: Array[Int]): Unit = {
    for (row <- queens.indices) {
      for (col <- queens.indices) {
        if (queens(row) == col) print("1 ")
        else print("0 ")
      }
      println()
    }
  }

}

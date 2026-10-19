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
    if (c == 0 || c == r) 1
    else pascal(c - 1, r - 1) + pascal(c, r - 1)
  }

  /**
   * Exercise 2 Parentheses Balancing
   */
  def balance(chars: List[Char]): Boolean = {
    def check(chars: List[Char], open: Int): Boolean = {
      if (open < 0) false
      else if (chars.isEmpty) open == 0
      else if (chars.head == '(') check(chars.tail, open + 1)
      else if (chars.head == ')') check(chars.tail, open - 1)
      else check(chars.tail, open)
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
    val queens = Array.fill(size)(-1)

    def isSafe(row: Int, column: Int, previousRow: Int): Boolean = {
      if (previousRow == row) true
      else if (queens(previousRow) == column ||
        math.abs(queens(previousRow) - column) == row - previousRow) false
      else isSafe(row, column, previousRow + 1)
    }

    def place(row: Int): Boolean = {
      def tryColumn(column: Int): Boolean = {
        if (column == size) false
        else if (isSafe(row, column, 0)) {
          queens(row) = column
          if (place(row + 1)) true
          else tryColumn(column + 1)
        } else tryColumn(column + 1)
      }

      if (row == size) true
      else tryColumn(0)
    }

    if (place(0)) Some(queens) else None
  }

  def printBoard(queens: Array[Int]): Unit = {
    for (row <- queens.indices) {
      for (column <- queens.indices) {
        if (queens(row) == column) print("1 ")
        else print("0 ")
      }
      println()
    }
  }

}

package recfun
// import common._

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
    println(pascal(2, 4))
    println(balance(List('(', 'a', ')')))
    println(balance(List('(', ')', ')')))
    println(countChange(5, List(2, 3)))
    println(countChange(4, List(1, 2)))
    println(countChange(8, List(3, 2, 1)))

    nQueens(4) match {
      case Some(solution) => printBoard(solution)
      case None => println("Решение не найдено")
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
    def check(remaining: List[Char], opened: Int): Boolean = {
      if (opened < 0) false
      else if (remaining.isEmpty) opened == 0
      else if (remaining.head == '(')
        check(remaining.tail, opened + 1)
      else if (remaining.head == ')')
        check(remaining.tail, opened - 1)
      else
        check(remaining.tail, opened)
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
    require(coins.forall(_ > 0), "coin denominations must be positive")

    if (money == 0) 1
    else if (money < 0 || coins.isEmpty) 0
    else
      countChange(money - coins.head, coins) + countChange(money, coins.tail)
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
  def printBoard(solution: Array[Int]): Unit = {
    for (row <- solution.indices) {
      for (col <- solution.indices) {
        if (solution(row) == col) print("1 ")
        else print("0 ")
      }
      println()
    }
  }
  
  def nQueens(size: Int): Option[Array[Int]] = {
    if (size <= 0) None
    else {
      val queens = new Array[Int](size)
  
      def safe(row: Int, col: Int): Boolean = {
        def check(previousRow: Int): Boolean = {
          if (previousRow == row) true
          else {
            val previousCol = queens(previousRow)
  
            if (previousCol == col ||
                math.abs(previousCol - col) == row - previousRow) false
            else check(previousRow + 1)
          }
        }
  
        check(0)
      }
  
      def search(row: Int): Boolean = {
        if (row == size) true
        else {
          def tryColumn(col: Int): Boolean = {
            if (col == size) false
            else if (safe(row, col)) {
              queens(row) = col
  
              if (search(row + 1)) true
              else tryColumn(col + 1)
            }
            else tryColumn(col + 1)
          }
  
          tryColumn(0)
        }
      }
  
      if (search(0)) Some(queens.clone())
      else None
    }
  }

}

package recfun

object Main {
  def main(args: Array[String]): Unit = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }

    println("\nBalance")
    println(balance("(if (zero? x) max (/ 1 x))".toList))
    println(balance("())(".toList)) 
 
    println("\nCount change")
    println(countChange(5, List(2, 3)))
    println(countChange(4, List(1, 2)))
 
    println("\nN-Queens")
    nQueens(4) match {
      case Some(solution) =>
        println(solution.mkString("[", ",", "]"))
        printBoard(solution)
      case None => println("Решения нет")
    }
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if (c == 0 || c == r) 1 else pascal(c-1, r-1) + pascal(c, r-1)
  }

  /**
   * Exercise 2 Parentheses Balancing
   */
  def balance(chars: List[Char]): Boolean = {
   def loop(rest: List[Char], open: Int): Boolean = {
    if (open < 0) false 
    else if (rest.isEmpty) open==0 
    else if (rest.head == '(') loop(rest.tail, open + 1)
    else if (rest.head == ')') loop(rest.tail, open - 1)
    else loop(rest.tail, open)
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
    if (size <= 0) return None
 
    val queens = new Array[Int](size)
 
    def isSafe(row: Int, col: Int): Boolean =
      (0 until row).forall { r =>
        queens(r) != col && 
        math.abs(queens(r) - col) != row - r 
      }
 
    def place(row: Int): Boolean =
      if (row == size) true 
      else (0 until size).exists { col =>
        if (isSafe(row, col)) {
          queens(row) = col
          place(row + 1)
        } else false
      }
 
    if (place(0)) Some(queens) else None
  }

  def printBoard(queens: Array[Int]): Unit =
    for (row <- queens.indices) {
      val line = queens.indices.map(col => if (queens(row) == col) 1 else 0)
      println(line.mkString(" "))
    }

}

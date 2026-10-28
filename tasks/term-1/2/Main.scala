// package recfun
// import common._

object Main {
  def main(args: Array[String]): Unit = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }

    println("\nBalance checks:")
    println(balance("(if (zero? x) max (/ 1 x))".toList))
    println(balance(":-)".toList))

    println("\nCount change:")
    println(countChange(4, List(1, 2)))
    
    println("\nN-Queens (size 4):")
    nQueens(4).foreach(arr => printBoard(arr))
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
    def loop(chars: List[Char], count: Int): Boolean = {
      if (count < 0) false
      else chars match {
        case Nil => count == 0
        case '(' :: tail => loop(tail, count + 1)
        case ')' :: tail => loop(tail, count - 1)
        case _ :: tail => loop(tail, count)
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
    def isSafe(col: Int, row: Int, placed: List[(Int, Int)]): Boolean = {
      placed.forall { case (c, r) =>
        col != c && math.abs(col - c) != math.abs(row - r)
      }
    }

    def solve(row: Int, placed: List[(Int, Int)]): Option[List[(Int, Int)]] = {
      if (row == size) Some(placed)
      else {
        val validCols = 0.until(size).filter(col => isSafe(col, row, placed))
        validCols.view.flatMap { col =>
          solve(row + 1, (col, row) :: placed)
        }.headOption
      }
    }

    solve(0, Nil).map { placed =>
      placed.sortBy(_._2).map(_._1).toArray
    }
  }

  def printBoard(solution: Array[Int]): Unit = {
    val size = solution.length
    for (r <- 0.until(size)) {
      for (c <- 0.until(size)) {
        if (solution(r) == c) print("1 ") else print("0 ")
      }
      println()
    }
    println()
  }


}

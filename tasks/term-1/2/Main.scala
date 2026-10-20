package recfun
// import common._

object Main {
  def main(args: Array[String]) = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }

    println(balance("(if (zero? x) max (/ 1 x))".toList))
    println(balance("I told him (that it’s not (yet) done). (But he wasn't listening)".toList))
    println(balance(":-)".toList))
    println(balance("())(".toList))

    println(s"countChange(4, List(1, 2)) = ${countChange(4, List(1, 2))}")
    println(s"countChange(300, List(5, 10, 20, 50)) = ${countChange(300, List(5, 10, 20, 50))}")

    nQueens(4) match {
      case Some(board) =>
        println("Solution found:")
        printQueens(board)
      case None => println("No solution found")
    }
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    (c, r) match {
      case (col, row) if col < 0 || row < 0 || col > row => 0
      case (0, _) => 1
      case (col, row) if col == row => 1
      case _ => pascal(c - 1, r - 1) + pascal(c, r - 1)
    }
  }

  /**
   * Exercise 2 Parentheses Balancing
   */
  def balance(chars: List[Char]): Boolean = {
    def loop(remaining: List[Char], count: Int): Boolean = {
      if (count < 0) false
      else remaining match {
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
    def isSafeCheck(col: Int, rest: List[Int], dist: Int): Boolean = rest match {
      case Nil => true
      case head :: tail =>
        if (head == col || math.abs(head - col) == dist) false
        else isSafeCheck(col, tail, dist + 1)
    }

    def isSafe(col: Int, placed: List[Int]): Boolean = isSafeCheck(col, placed, 1)

    def search(placed: List[Int]): Option[List[Int]] = {
      if (placed.length == size) Some(placed)
      else {
        (0 until size).iterator
          .filter(col => isSafe(col, placed))
          .map(col => search(col :: placed))
          .collectFirst { case Some(solution) => solution }
      }
    }

    search(Nil).map(_.reverse.toArray)
  }

  def printQueens(board: Array[Int]): Unit = {
    for (row <- board.indices) {
      for (col <- board.indices) {
        if (board(row) == col) print("1 ")
        else print("0 ")
      }
      println()
    }
  }

}

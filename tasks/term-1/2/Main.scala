package recfun

object Main {
  def main(args: Array[String]) = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
    
    println()

    println(balance("(a(b)c)".toList)) // true
    println(balance("(a))(".toList)) // false

    println()

    println(countChange(5, List(2))) // 0
    println(countChange(5, List(2, 3))) // 1
    println(countChange(5, List(1, 2, 3))) // 5

    println()

    nQueens(4) match {
      case Some(q) =>
        printBoard(q)
      case None =>
        println("No solution")
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
    def loop(rest: List[Char], depth: Int): Boolean = {
      rest match{
        case Nil => depth == 0
        case '(' :: tail => loop(tail, depth + 1)
        case ')' :: tail => 
          if (depth == 0) false
          else loop(tail, depth - 1)
        case _ :: tail => loop(tail, depth)
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
    else if (money < 0) 0
    else coins match {
      case Nil => 0
      case c :: tail => countChange(money, tail) + countChange(money - c, coins)
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
    else place(Nil, size) match {
      case Some(solution) => Some(solution.reverse.toArray)
      case None => None
    }
  }

  def place(queens: List[Int], size: Int): Option[List[Int]] = {
    if (queens.length == size) Some(queens)
    else tryCol(0, queens, size)
  }

  def tryCol(col: Int, queens: List[Int], size: Int): Option[List[Int]] = {
    if (col == size) None
    else if (isSafe(col, queens, 1)) {
      place(col :: queens, size) match {
        case Some(solution) => Some(solution)
        case None => tryCol(col + 1, queens, size)
      }
    } else tryCol(col + 1, queens, size)
  }

  def isSafe(col: Int, queens: List[Int], distance: Int): Boolean = {
    queens match {
      case Nil => true
      case q :: tail =>
        if (q == col || math.abs(q - col) == distance) false
        else isSafe(col, tail, distance + 1)
    }
  }

  def printBoard(queens: Array[Int]): Unit = {
    for (row <- 0 until queens.length) {
      for (col <- 0 until queens.length) {
        if (queens(row) == col) print("1 ")
        else print("0 ")
      }
      println()
    }
  }

}

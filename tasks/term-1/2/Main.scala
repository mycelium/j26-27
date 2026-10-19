package recfun

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
    println("Parentheses Balancing")
    println(balance("(is normal( '(' = ')' ) balance)".toList))
    println(balance("there is no balance ((())))".toList))

    println("Count change:")
    println(countChange(5, List(2, 3)))
    println(countChange(4, List(1, 2)))
    println(countChange(8, List(3, 2, 1)))
    //println(countChange(8, List(3, -10, 1)))

    println("N-Queens 4:")
    printBoard(nQueens(4))
    println("N-Queens 3:")
    printBoard(nQueens(3))
    println("N-Queens 8:")
    printBoard(nQueens(8))
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if ( c == r || c == 0) 1
    else pascal(c-1, r-1) + pascal(c, r-1)

  }

  /**
   * Exercise 2 Parentheses Balancing
   */
  def balance(chars: List[Char]): Boolean = {
    def balance_inside(rest_str: List[Char], open_num: Int): Boolean =
    {
      if (open_num < 0 ) false
      else rest_str match
      {
        case Nil => open_num == 0
        case '(' :: tail => balance_inside(tail, open_num + 1)   
        case ')' :: tail => balance_inside(tail, open_num - 1) 
        case _ :: tail => balance_inside(tail, open_num)   
      }
    }
    balance_inside(chars, 0)
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
    if(size == 2 || size == 3 || size < 1) None
    else if (size == 1) Some(Array(0))
    else
    {
      def isSafe(placed: Array[Int], col: Int): Boolean = 
      {
        val row = placed.length
        !placed.contains(col) &&
        (0 until row).forall { i =>
          val d = placed(i) - col
          d != i - row && d != row - i
        }
      }
      def go(placed: Array[Int]): Option[Array[Int]] =
      {
        val row = placed.length
        if (row == size) Some(placed)
        else
        {
          (0 until size).iterator
            .filter(col => isSafe(placed, col))
            .map(col => go(placed :+ col))
            .collectFirst { case Some(sol) => sol }
        }
      }
      go(Array.empty[Int])
    }
  }

  def printBoard(res: Option[Array[Int]]): Unit = {
    res match {
      case Some(board) =>
      val size = board.length
        for (row <- 0 until size) {
          val line = (0 until size).map { col =>
            if (board(row) == col) "1" else "0"
          }.mkString(" ")
          println(line)
        }
        println()
      case None =>
        println("No result")
        println()
    }
  }
}
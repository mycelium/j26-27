package recfun

object Main {
  def main(args: Array[String]) = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }

    println("Is balanced: " + balance("(10 + 2) - (13 - 8)".toList))
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if (c < 0 || c < 0 || r < c) 0
    else if (c == 0 || c ==r) 1
    else pascal(c - 1, r -1) + pascal(c, r - 1)
  }

  /**
   * Exercise 2 Parentheses Balancing
   */
  def balance(chars: List[Char]): Boolean = {
    def count(rest: List[Char], opened: Int): Boolean = {
      if (rest.isEmpty) opened == 0
      else if (rest.head == '(') count(rest.tail, opened + 1)
      else if (rest.head == ')') {
        if (opened == 0) false
        else count(rest.tail, opened - 1)
      } else count(rest.tail, opened)
    }

    count(chars, 0)
  }

  /**
   * Exercise 3 Counting Change
   * Write a recursive function that counts how many different ways you can make
   * change for an amount, given a list of coin denominations. For example,
   * there is 1 way to give change for 5 if you have coins with denomiation
   * 2 and 3: 2+3.
   */
  // def countChange(money: Int, coins: List[Int]): Int = {
  //
  // }
  
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

  // def nQueens(size: Int): Option[Array[Int]] = {
  //
  // }
//
//   /**
//    * Exercise 3 Counting Change
//    * Write a recursive function that counts how many different ways you can make
//    * change for an amount, given a list of coin denominations. For example,
//    * there is 1 way to give change for 5 if you have coins with denomiation
//    * 2 and 3: 2+3.
//    */
//   def countChange(money: Int, coins: List[Int]): Int = {
//
//   }
//
//   /**
//    * Excerice 4 N-Queens Problem
//    * Write a function that provides a solution for n-queens problem if possible
//    * Input parameter represents board size and number of queens
//    * Output is an array: index represents row number and value represents column number.
//    * Example: nQueens(4): [1,3,0,2] or
//    * 0 1 0 0
//    * 0 0 0 1
//    * 1 0 0 0
//    * 0 0 1 0
//    */
//
//   def nQueens(size: Int): Option[Array[Int]] = {
//
//   }
//
}

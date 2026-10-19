package recfun
import common._

object Main {
  def main(args: Array[String]) = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
    val chars = String("(if (zero? x) max (/ 1 x))")
    print(balance(chars.toList))
    println()
    nQueens(8) match {
      case None => println("Решения нет")
      case Some(result) => {
        for (row <- 0 until result.length) {
          for (col <- 0 until result.length) {
            if (result(row) == col) print(1 + " ")
            else print(0 + " ")
          }
          println()
        }
      }
    }
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if (r < 2 || c == r || c == 0) 1
    else pascal(c, r - 1) + pascal(c - 1, r - 1)
  }

  /**
   * Exercise 2 Parentheses Balancing
   */
  def balance(chars: List[Char]): Boolean = {
    def counter(leftBracket: Int, rightBracket: Int, char_s: List[Char]): Boolean = {
      if (rightBracket > leftBracket) false
      else if (char_s.isEmpty) leftBracket == rightBracket
      else {
        if (char_s.head == '(') counter(leftBracket + 1, rightBracket, char_s.tail)
        else if (char_s.head == ')') counter(leftBracket, rightBracket + 1, char_s.tail)
        else counter(leftBracket, rightBracket, char_s.tail)
      }
    }
    counter(0, 0, chars)
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
    else if (coins.isEmpty) 0
    else if (money < coins.head) countChange(money, coins.tail)
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
    def go(chosen: List[Int]): Option[List[Int]] = {
      if (chosen.length == size) Some(chosen)
      else {
        def tryInsert(candidates: List[Int]): Option[List[Int]] = {
          if (candidates.isEmpty) None
          else {
            if (chosen.zipWithIndex.forall { case (equal, index) => equal != candidates.head && math.abs(equal - candidates.head) != index + 1 })
            go(candidates.head :: chosen).orElse(tryInsert(candidates.tail))
            else tryInsert(candidates.tail)
          }
        }
        tryInsert((0 until size).toList)
      }
    }
    go(Nil).map(result => result.reverse.toArray)
  }

}

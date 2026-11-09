package recfun

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
    println("balance:")
    println(balance("(a(b)c)".toList))
    println(balance("(a))(".toList)) 

    println("countChange:")
    println(countChange(5, List(2))) 
    println(countChange(5, List(2, 3)))
    println(countChange(5, List(1, 2, 3)))

    println("nQueens:")
    println(nQueens(4).map(_.mkString("[", ", ", "]")))
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if (c == 0 || c == r) {
      1
    } 
    else {
      pascal(c - 1, r - 1) + pascal(c, r - 1)
    }
  }

  /**
   * Exercise 2 Parentheses Balancing
   */
  def balance(chars: List[Char]): Boolean = {

  def check(chars: List[Char], opened: Int): Boolean = {
    chars match {

      case Nil => opened == 0

      case '(' :: tail => check(tail, opened + 1)

      case ')' :: tail => if (opened == 0) false else check(tail, opened - 1)

      case _ :: tail => check(tail, opened)
    }
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
    if (money == 0) {
      1
    }

    else if (money < 0) {
      0
    }

    else {
      coins match {
        case Nil => 0

        case coin :: tail => countChange(money, tail) + countChange(money - coin, coins)
      }
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

  def isSafe(col: Int, queens: List[Int], distance: Int): Boolean = {
    queens match {
      case Nil => true

      case q :: tail =>
        if (q == col || math.abs(q - col) == distance) {
          false
        }
        else {
          isSafe(col, tail, distance + 1)
        }
    }
  }

  def place(row: Int, queens: List[Int]): Option[List[Int]] = {
      if (row == size) {
        Some(queens.reverse)
      }
      else {
        def tryColumn(col: Int): Option[List[Int]] = {
          if (col == size) {
            None
          }
          else if (isSafe(col, queens, 1)) {
            place(row + 1, col :: queens) match {
              case Some(result) => Some(result)
              case None => tryColumn(col + 1)
            }
          }
          else {
            tryColumn(col + 1)
          }
        }
        tryColumn(0)
      }
    }

    if (size <= 0) {
      None
    }
  
    else {
      place(0, Nil) match {
        case Some(result) => Some(result.toArray)
        case None => None
      }
    }
  }
}
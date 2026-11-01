package recfun
import common._

object Main {
  def main(args: Array[String]): Unit = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
    
    println("Balance '(if (zero? x) max (/ 1 x))': " + balance("(if (zero? x) max (/ 1 x))".toList))
    println("Balance 'I told him (that it’s not (yet) done). (But he wasn’t listening)': " + balance("I told him (that it’s not (yet) done). (But he wasn’t listening)".toList))
    println("Balance ':-)': " + balance(":-)".toList))
    println("Balance '())(': " + balance("())(".toList))
    
    println("CountChange(4, List(1, 2)): " + countChange(4, List(1, 2))) // Должно быть 3
    println("N-Queens(4): " + nQueens(4).map(_.mkString(", ")).getOrElse("No solution"))
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
      if (chars.isEmpty) count == 0
      else if (chars.head == '(') loop(chars.tail, count + 1)
      else if (chars.head == ')') {
        if (count == 0) false
        else loop(chars.tail, count - 1)
      }
      else loop(chars.tail, count)
    }
    loop(chars, 0)
  }

  /**
   * Exercise 3 Counting Change
   */
  def countChange(money: Int, coins: List[Int]): Int = {
    if (money < 0) 0
    else if (money == 0) 1
    else if (coins.isEmpty) 0
    else {
      countChange(money - coins.head, coins) + countChange(money, coins.tail)
    }
  }
  
  /**
   * Excerice 4 N-Queens Problem
   */
    def nQueens(size: Int): Option[Array[Int]] = {
    def isSafe(queens: List[Int], col: Int, row: Int): Boolean = {
      queens.zipWithIndex.forall { case (qCol, qRow) =>
        qCol != col && Math.abs(qCol - col) != (row - qRow)
      }
    }

    def placeQueens(row: Int, queens: List[Int]): Option[List[Int]] = {
      if (row == size) {
        Some(queens.reverse)
      } else {
        (0 until size).foldLeft(None: Option[List[Int]]) { (acc, col) =>
          acc.orElse {
            if (isSafe(queens, col, row)) {
              placeQueens(row + 1, col :: queens)
            } else {
              None
            }
          }
        }
      }
    }

    placeQueens(0, List()).map(_.toArray)
  }
  }
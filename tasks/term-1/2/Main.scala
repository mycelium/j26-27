package recfun
//import common._

object Main {
def main(args: Array[String]): Unit = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }

    println("N-Queens (n = 8)")
    nQueens(8) match {
      case Some(queens) =>
        println(queens.mkString("[", ", ", "]"))
        printBoard(queens)
      case None => println("no solution")
    }
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
    // open - число пока не закрытых скобок
    def loop(rest: List[Char], open: Int): Boolean =
      if (open < 0) false                      // закрывающая скобка без пары
      else rest match {
        case Nil      => open == 0
        case '(' :: t => loop(t, open + 1)
        case ')' :: t => loop(t, open - 1)
        case _ :: t   => loop(t, open)
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
    // placed - колонки уже поставленных ферзей, ферзь предыдущей строки в начале списка
    def safe(col: Int, placed: List[Int]): Boolean =
      placed.zipWithIndex.forall { case (c, i) =>
        c != col && math.abs(c - col) != i + 1   // та же колонка или диагональ
      }

    def place(row: Int, placed: List[Int]): Option[List[Int]] =
      if (row == size) Some(placed)
      else (0 until size).iterator
        .filter(col => safe(col, placed))
        .map(col => place(row + 1, col :: placed))
        .collectFirst { case Some(solution) => solution }

    if (size <= 0) None
    else place(0, Nil).map(_.reverse.toArray)
  }

  /** Печать доски: 1 - ферзь, 0 - пустая клетка. */
def printBoard(queens: Array[Int]): Unit =
    queens.foreach { col =>
      println(queens.indices.map(c => if (c == col) 1 else 0).mkString(" "))
    }

}
package recfun

object Main {
  def main(args: Array[String]) = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }

    println("\n--- Проверка balance ---")
    println(balance("(if (zero? x) max (/ 1 x))".toList))
    println(balance("I told him (that it's not (yet) done). (But he wasn't listening)".toList))
    println(balance(":-)".toList))
    println(balance("())(".toList))

    println("\n--- Проверка countChange ---")
    println(countChange(4, List(1, 2)))
    println(countChange(5, List(2, 3)))
    println(countChange(5, List(5)))
    println(countChange(5, List(2)))

    println("\n--- Проверка nQueens ---")
    val result = nQueens(4)
    if (result.isDefined) {
      println("Решение (номера колонок по строкам): " + result.get.mkString(", "))
      val arr = result.get
      for (i <- 0 until 4) {
        for (j <- 0 until 4) {
          if (arr(i) == j) print("1 ") else print("0 ")
        }
        println()
      }
    } else {
      println("Решения нет")
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
    def loop(remaining: List[Char], openCount: Int): Boolean = {
      if (remaining.isEmpty) openCount == 0
      else {
        val head = remaining.head
        if (head == '(') loop(remaining.tail, openCount + 1)
        else if (head == ')') {
          if (openCount == 0) false
          else loop(remaining.tail, openCount - 1)
        }
        else loop(remaining.tail, openCount)
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
    def isSafe(queens: List[Int], col: Int): Boolean = {
      val row = queens.length
      queens.zipWithIndex.forall { case (qCol, i) =>
        val qRow = row - 1 - i
        qCol != col && Math.abs(qCol - col) != Math.abs(qRow - row)
      }
    }

    def placeQueens(row: Int, queens: List[Int]): Option[List[Int]] = {
      if (row == size) Some(queens.reverse)
      else {
        (0 until size).toStream.flatMap { col =>
          if (isSafe(queens, col)) {
            placeQueens(row + 1, col :: queens)
          } else {
            None
          }
        }.headOption
      }
    }

    placeQueens(0, List()).map(_.toArray)
  }
}
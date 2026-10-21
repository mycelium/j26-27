package recfun
// import common._

import scala.annotation.tailrec
 
object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }

    println("\nBalance")
    println(balance("((x)(1))".toList))  // true
    println(balance("(a) bc".toList))    // true
    println(balance(":-)".toList))       // false
    println(balance("())(".toList))      // false
 
    println("\nCount change")
    println(countChange(5, List(2, 3)))                   // 1
    println(countChange(4, List(1, 2)))                   // 3
    println(countChange(10, List(2, 5, 3, 6)))            // 5

    println("\nN-Queens")
    for (n <- List(4, 8, 3)) {
      val solution = nQueens(n)
      println("nQueens(" + n + "):")
      if (solution.isDefined) {
        println(solution.get.mkString("[", ",", "]"))
        printBoard(solution.get)
      } else {
        println("решения нет")
      }
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

    @tailrec // хвостово рекурсивная функция
    def loop(rest: List[Char], open: Int): Boolean =
      if (open < 0) false // количество открытых и еще не закрытых скобок
      else if (rest.isEmpty) open == 0
      else if (rest.head == '(') loop(rest.tail, open + 1)
      else if (rest.head == ')') loop(rest.tail, open - 1)
      else loop(rest.tail, open)
 
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
    // columns(row) - колонка, в которой стоит ферзь в строке row
    val columns = new Array[Int](size)

    // можно ли поставить ферзя в клетку (row, col), если выше уже стоят ферзи
    def isSafe(row: Int, col: Int): Boolean = {
      var safe = true
      for (prevRow <- 0 until row) {
        val prevCol = columns(prevRow)
        val sameColumn = prevCol == col
        val sameDiagonal = Math.abs(prevCol - col) == row - prevRow
        if (sameColumn || sameDiagonal) {
          safe = false
        }
      }
      safe
    }

    // расставляет ферзей, начиная со строки row; true, если удалось расставить всех
    def placeQueens(row: Int): Boolean = {
      if (row == size) {
        true
      } else {
        var found = false
        var col = 0
        while (col < size && !found) {
          if (isSafe(row, col)) {
            columns(row) = col
            found = placeQueens(row + 1)
          }
          col += 1
        }
        found
      }
    }

    if (placeQueens(0)) Some(columns) else None
  }

  /** Печать доски: 1 - ферзь, 0 - пустая клетка. */
  def printBoard(solution: Array[Int]): Unit = {
    for (row <- 0 until solution.length) {
      for (col <- 0 until solution.length) {
        if (solution(row) == col) print("1 ") else print("0 ")
      }
      println()
    }
  }
}



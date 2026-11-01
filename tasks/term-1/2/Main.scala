package recfun
import common._

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }

    println("\nBalance")
    println(balance("(if (zero? x) max (/ 1 x))".toList)) // true
    println(balance("I told him (that it's not (yet) done).\n(But he wasn't listening)".toList)) // true
    println(balance(":-)".toList)) // false
    println(balance("())(".toList)) // false

    println("\nCount change")
    println(countChange(4, List(1, 2))) // 3
    println(countChange(300, List(5, 10, 20, 50, 100, 200, 500))) // 1022

    println("\nN-Queens")
    for (n <- List(4, 8)) {
      nQueens(n) match {
        case Some(solution) =>
          println(s"n = $n: " + solution.mkString("[", ",", "]"))
          printBoard(solution)
        case None =>
          println(s"n = $n: no solution")
      }
    }
    println("n = 3: " + nQueens(3).map(_.mkString("[", ",", "]")).getOrElse("no solution"))
  }

  /**
   * Exercise 1
   * Элемент треугольника Паскаля: на краях строки 1,
   * иначе сумма двух элементов предыдущей строки.
   */
  def pascal(c: Int, r: Int): Int = {
    if (c == 0 || c == r) 1
    else pascal(c - 1, r - 1) + pascal(c, r - 1)
  }

  /**
   * Exercise 2 Parentheses Balancing
   * Идём по списку, храня количество открытых и ещё не закрытых скобок.
   * Если счётчик стал отрицательным - закрывающая скобка без пары.
   * В конце счётчик должен быть равен нулю.
   */
  def balance(chars: List[Char]): Boolean = {
    def loop(rest: List[Char], open: Int): Boolean =
      if (open < 0) false
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
   *
   * Все способы делятся на две непересекающиеся группы:
   *  - использующие первую монету (хотя бы раз): уменьшаем сумму на её номинал;
   *  - не использующие первую монету: отбрасываем её из списка.
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
   *
   * Рекурсивный перебор с возвратом (backtracking): ставим ферзя в очередную
   * строку в первую безопасную колонку; если дальше решения нет - пробуем
   * следующую колонку.
   */
  def nQueens(size: Int): Option[Array[Int]] = {
    // placed: колонки уже расставленных ферзей; head - предыдущая строка
    def safe(col: Int, placed: List[Int]): Boolean =
      placed.zipWithIndex.forall { case (c, i) =>
        val distance = i + 1 // расстояние по строкам до этого ферзя
        c != col && math.abs(c - col) != distance
      }

    def place(row: Int, placed: List[Int]): Option[List[Int]] =
      if (row == size) Some(placed)
      else tryCol(0, row, placed)

    def tryCol(col: Int, row: Int, placed: List[Int]): Option[List[Int]] =
      if (col >= size) None
      else if (safe(col, placed)) {
        place(row + 1, col :: placed) match {
          case found @ Some(_) => found
          case None => tryCol(col + 1, row, placed)
        }
      } else tryCol(col + 1, row, placed)

    place(0, Nil).map(_.reverse.toArray)
  }

  /**
   * Печать доски: 1 - ферзь, 0 - пустая клетка
   */
  def printBoard(solution: Array[Int]): Unit = {
    val size = solution.length
    for (row <- 0 until size)
      println((0 until size).map(col => if (solution(row) == col) 1 else 0).mkString(" "))
  }

}
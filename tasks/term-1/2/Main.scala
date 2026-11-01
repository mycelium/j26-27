package recfun
/* import common._ */
import scala.annotation.tailrec

object Main {
  def main(args: Array[String]): Unit = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  println("N-Queens (8)")
  nQueens(8) match {
    case Some(s) =>
      println(s.mkString("[", ", ", "]"))
      printBoard(s)
    case None => println("Решения нет")
  }
}

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    require(c >= 0 && c <= r, "колонка вне треугольника")
    if c == 0 || r == 0 || c == r then 1 else pascal(c-1, r-1) + pascal(c, r-1)
  }

  /**
   * Exercise 2 Parentheses Balancing
   */
  def balance(chars: List[Char]): Boolean = {
    @tailrec
    def go(chars: List[Char], acc: Int): Boolean = chars match
      case Nil => if acc == 0 then true else false
      case ('(' :: rest) => go(rest, acc+1)
      case (')' :: rest) => if acc == 0 then false else go(rest, acc-1)
      case (_ :: rest) => go(rest, acc)
    go(chars, 0) 
  }

  /**
   * Exercise 3 Counting Change
   * Write a recursive function that counts how many different ways you can make
   * change for an amount, given a list of coin denominations. For example,
   * there is 1 way to give change for 5 if you have coins with denomiation
   * 2 and 3: 2+3.
   */
  def countChange(money: Int, coins: List[Int]): Int = {
      if money <= 0 then 0 else coins match
        case Nil => 0
        case x :: xs => if money - x == 0 then 1 + countChange(money, xs) else countChange(money - x, (x :: xs)) + countChange(money, xs)
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
    require(size >= 0, "размер доски не может быть отрицательным")

    @tailrec
    def safe(col: Int, placed: List[Int], dist: Int): Boolean = placed match
      case Nil => true
      case c :: rest =>
        if c == col || math.abs(c - col) == dist then false
        else safe(col, rest, dist + 1)

    // расставляет ферзей начиная с ряда row
    def place(row: Int, placed: List[Int]): Option[List[Int]] =
      if row == size then Some(placed.reverse)
      else tryCols(0, row, placed)

    // перебирает колонки  в ряду row
    def tryCols(col: Int, row: Int, placed: List[Int]): Option[List[Int]] =
      if col == size then None
      else
        val withThisCol =
          if safe(col, placed, 1) then place(row + 1, col :: placed)
          else None
        withThisCol.orElse(tryCols(col + 1, row, placed))

    place(0, Nil).map(_.toArray)
  }

  def printBoard(solution: Array[Int]): Unit =
    val n = solution.length
    solution.foreach { col =>
      println((0 until n).map(c => if c == col then "1" else "0").mkString(" "))
    }

  def isValid(s: Array[Int]): Boolean =
    val n = s.length
    (0 until n).forall(i =>
      (i + 1 until n).forall(j => s(i) != s(j) && math.abs(s(i) - s(j)) != j - i))


}

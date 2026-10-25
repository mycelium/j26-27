package recfun
// import common._

object Main {
  def main(args: Array[String]): Unit = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }

    println("Balance")
    println(balance("(a(b)c)(d)".toList))
    println(balance("abc".toList))
    println(balance("".toList))
    println(balance("(()".toList))
    println(balance("())".toList))
    println(balance(")(".toList))

    println("Count Change")
    println(countChange(4, List(1, 2)))
    println(countChange(5, List(2, 3)))
    println(countChange(300, List(5, 10, 20, 50, 100, 200, 500)))

    println("N-Queens")
    for (n <- List(1, 2, 3, 4, 8)) {
      println("size = " + n)
      nQueens(n) match {
        case Some(solution) =>
          println(solution.mkString("[", ",", "]"))
          printBoard(solution)
        case None => println("Решения нет")
      }
    }
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int =
    if (c == 0 || c == r) 1
    else pascal(c - 1, r - 1) + pascal(c, r - 1)

  /**
   * Exercise 2 Parentheses Balancing
   */
  def balance(chars: List[Char]): Boolean = {
    def loop(rest: List[Char], open: Int): Boolean =
      if (open < 0) false
      else rest match {
        case Nil         => open == 0
        case '(' :: tail => loop(tail, open + 1)
        case ')' :: tail => loop(tail, open - 1)
        case _ :: tail   => loop(tail, open)
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
  def countChange(money: Int, coins: List[Int]): Int =
    if (money == 0) 1
    else if (money < 0 || coins.isEmpty) 0
    else countChange(money - coins.head, coins) + countChange(money, coins.tail)

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
    def safe(col: Int, placed: List[Int]): Boolean =
      placed.zipWithIndex.forall { case (c, i) =>
        val dist = i + 1
        c != col && math.abs(c - col) != dist
      }

    def place(row: Int, placed: List[Int]): Option[List[Int]] =
      if (row == size) Some(placed.reverse)
      else {
        def tryCol(col: Int): Option[List[Int]] =
          if (col == size) None
          else if (safe(col, placed)) {
            place(row + 1, col :: placed) match {
              case found @ Some(_) => found
              case None            => tryCol(col + 1)
            }
          } else tryCol(col + 1)
        tryCol(0)
      }

    place(0, Nil).map(_.toArray)
  }

  def printBoard(solution: Array[Int]): Unit =
    for (row <- solution.indices)
      println((0 until solution.length)
        .map(col => if (solution(row) == col) "1" else "0")
        .mkString(" "))

}

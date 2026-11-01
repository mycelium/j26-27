package recfun

object Main {
  def main(args: Array[String]): Unit = {
    for (row <- 0 to 5) {
      for (col <- 0 to row)
        print(pascal(col, row).toString + " ")
      println()
    }

    println()
    println(balance("([{[}])".toList))
    println(balance("{{(}[())]}".toList))
    println(balance("{{}[()]}".toList))
    println(balance("())(".toList))

    println()
    println(countChange(5, List(2, 3)))
    println(countChange(4, List(1, 2)))
    println(countChange(300, List(5, 10, 20, 50, 100, 200, 500)))

    println()
    for (n <- List(2, 3, 4, 8)) {
      println("size = " + n)
      nQueens(n) match {
        case Some(queens) =>
          println(queens.mkString("[", ",", "]"))
          printBoard(queens)
        case None =>
          println("No solution")
      }
      println()
    }
  }

  def pascal(c: Int, r: Int): Int = {
    if (c == 0 || c == r) 1
    else pascal(c - 1, r - 1) + pascal(c, r - 1)
  }

  def balance(chars: List[Char]): Boolean = {
    def isOpening(c: Char): Boolean = c == '(' || c == '[' || c == '{'

    def isClosing(c: Char): Boolean = c == ')' || c == ']' || c == '}'

    def matches(open: Char, close: Char): Boolean =
      (open == '(' && close == ')') ||
      (open == '[' && close == ']') ||
      (open == '{' && close == '}')

    def check(chars: List[Char], stack: List[Char]): Boolean = {
      if (chars.isEmpty) stack.isEmpty
      else if (isOpening(chars.head)) check(chars.tail, chars.head :: stack)
      else if (isClosing(chars.head)) {
        if (stack.isEmpty || !matches(stack.head, chars.head)) false
        else check(chars.tail, stack.tail)
      }
      else check(chars.tail, stack)
    }

    check(chars, Nil)
}

  def countChange(money: Int, coins: List[Int]): Int = {
    if (money == 0) 1
    else if (money < 0 || coins.isEmpty) 0
    else countChange(money - coins.head, coins) + countChange(money, coins.tail)
  }

  def nQueens(size: Int): Option[Array[Int]] = {
    def noConflict(col: Int, placed: List[Int], distance: Int): Boolean = placed match {
      case Nil => true
      case last :: others =>
        last != col && math.abs(last - col) != distance && noConflict(col, others, distance + 1)
    }
 
    def solve(placed: List[Int]): Option[List[Int]] =
      if (placed.length == size) Some(placed)
      else tryColumns(List.range(0, size), placed)
 
    def tryColumns(columns: List[Int], placed: List[Int]): Option[List[Int]] = columns match {
      case Nil => None
      case col :: rest =>
        if (noConflict(col, placed, 1)) solve(col :: placed).orElse(tryColumns(rest, placed))
        else tryColumns(rest, placed)
    }
 
    solve(Nil).map(_.reverse.toArray)
  }
 
  def printBoard(queens: Array[Int]): Unit = {
    for (row <- queens.indices) {
      for (column <- queens.indices) {
        if (queens(row) == column) print("1 ")
        else print("0 ")
      }
      println()
    }
  }
}

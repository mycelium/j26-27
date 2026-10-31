package recfun

object Main {

  def main(args: Array[String]): Unit = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row) {
        print(pascal(col, row) + " ")
      }
      println()
    }

    println("\nParentheses Balancing")
    println("(()) -> " + balance("(())".toList))
    println("a(b)c -> " + balance("a(b)c".toList))
    println("(() -> " + balance("(()".toList))
    println("())( -> " + balance("())(".toList))

    println("\nCounting Change")
    println("Money: 4, coins: 1, 2")
    println("Ways: " + countChange(4, List(1, 2)))

    println("\nMoney: 5, coins: 2, 3")
    println("Ways: " + countChange(5, List(2, 3)))

    println("\nN-Queens, size: 4")
    nQueens(4) match {
      case Some(solution) =>
        println("Solution: " + solution.mkString("[", ", ", "]"))
        print_board(solution)
      case None =>
        println("No solution")
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
    def loop(remaining: List[Char], open_count: Int): Boolean = {
      if (open_count < 0) false
      else if (remaining.isEmpty) open_count == 0
      else {
        val next_count = {
          if (remaining.head == '(') open_count + 1
          else if (remaining.head == ')') open_count - 1
          else open_count
        }

        loop(remaining.tail, next_count)
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
    else {
      countChange(money - coins.head, coins) +
        countChange(money, coins.tail)
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
    def is_safe(column: Int, queens: List[Int]): Boolean = {
      val row = queens.length

      queens.reverse.zipWithIndex.forall {
        case (other_column, other_row) =>
          column != other_column &&
            math.abs(row - other_row) !=
              math.abs(column - other_column)
      }
    }

    def solve(queens: List[Int]): Option[List[Int]] = {
      if (queens.length == size) Some(queens.reverse)
      else {
        def try_column(column: Int): Option[List[Int]] = {
          if (column == size) None
          else if (is_safe(column, queens)) {
            solve(column :: queens) match {
              case found @ Some(_) => found
              case None => try_column(column + 1)
            }
          } else {
            try_column(column + 1)
          }
        }

        try_column(0)
      }
    }

    if (size < 0) None
    else solve(Nil).map(_.toArray)
  }

  def print_board(solution: Array[Int]): Unit = {
    for (queen_column <- solution) {
      for (column <- solution.indices) {
        if (column == queen_column) print("1 ")
        else print("0 ")
      }
      println()
    }
  }
}
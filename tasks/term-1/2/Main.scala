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

    println("\nBracket balance")
    println(balance("else pascal(c - 1, r - 1) + pascal(c, r - 1)".toList))
    println(balance("if )a == 0(".toList))
    println(balance("if (f == 0".toList)) 

    println("\nMony Change")
    println(countChange(4, List(1, 2)))
    println(countChange(5, List(2, 3)))

    println("\nN-Queens")
    val solution = nQueens(5)
    solution match {
      case Some(cols) => 
        println("Решение найдено: " + cols.mkString(", "))
      case None => println("Решения не существует")
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
    def loop(chars: List[Char], count: Int): Boolean = {
      if (count < 0) false
      else chars match {
        case Nil => count == 0
        case head :: tail =>
          head match {
            case '(' => loop(tail, count + 1)
            case ')' => loop(tail, count - 1)
            case _ => loop(tail, count)
          }
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
    def isSafe(col: Int, placed: List[Int]): Boolean = {
      val row = placed.length
      placed.zip(List.range(row - 1, -1, -1)).forall { case (c, r) =>
        c != col && Math.abs(c - col) != row - r
      }
    }

    def solve(placed: List[Int]): Option[List[Int]] = {
      if (placed.length == size) Some(placed)
      else {
        (0 until size).find(col => isSafe(col, placed)).flatMap(col => solve(col :: placed))
      }
    }

    solve(List.empty).map(_.reverse.toArray)
  }

}

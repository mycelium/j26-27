package recfun

object Main {
  def main(args: Array[String]): Unit = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
    println("\n2. Parentheses Balancing")
    println(balance("(a + b)".toList))
    println(balance("(()".toList))
    println(balance("))(".toList))
    println(balance("(a)(b)".toList))

    println("\n3. Counting Change")
    println(countChange(5, List(2, 3)))
    println(countChange(5, List(1, 2, 5)))
    println(countChange(4, List(1, 2)))
    println(countChange(7, List(2, 3)))

    println("\n4. N-Queens")

    nQueens(4) match {
        case Some(queens) =>
            println("Solution:")
            println(queens.mkString("[", ", ", "]"))
            printBoard(queens)
        case None =>
            println("No solution")
  }
  }


    def pascal(c: Int, r: Int): Int = {
        if (r<0 || c<0 || c>r) 0
        else if (c==0 || c==r) 1
        else pascal(c-1, r-1) + pascal(c,r-1)
    }


  def balance(chars: List[Char]): Boolean = {
    def check(remaining: List[Char], opened: Int): Boolean={
        if (opened < 0) false

        else if (remaining.isEmpty) opened == 0

        else if (remaining.head == '(')
        check(remaining.tail, opened + 1)

        else if (remaining.head == ')')
        check(remaining.tail, opened - 1)

        else
        check(remaining.tail, opened)

    }
    check(chars,0)
  }

  def countChange(money: Int, coins: List[Int]): Int = {
    if (money == 0) 1
    else if (money < 0 || coins.isEmpty) 0
    else {
        countChange(money - coins.head, coins) +
        countChange(money, coins.tail)
    }
  
  }

  def nQueens(size: Int): Option[Array[Int]] = {
    def isSafe(queens: List[Int], col: Int): Boolean = {
        def check(previous: List[Int], distance: Int): Boolean = {
            if (previous.isEmpty) true
            else if (previous.head == col ||
            math.abs(previous.head - col) == distance
            ) false
            else check(previous.tail, distance + 1)
        }
  check(queens, 1)
}
def solve(row: Int, queens: List[Int]): Option[List[Int]] = {
    if (row == size) Some(queens.reverse)
    else {
      def tryColumn(col: Int): Option[List[Int]] = {
        if (col == size) None
        else if (isSafe(queens, col)) {
          solve(row + 1, col :: queens) match {
            case Some(solution) => Some(solution)
            case None => tryColumn(col + 1)
          }
        }

        else tryColumn(col + 1)
      }

      tryColumn(0)
    }
  }
  if (size < 0) None
  else solve(0, Nil).map(_.toArray)
  }
  

def printBoard(queens: Array[Int]): Unit = {
  for (row <- queens.indices) {
    for (col <- queens.indices) {
      if (queens(row) == col) print("1 ")
      else print("0 ")
    }
    println()
  }
}
}

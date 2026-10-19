package recfun

object Main {
  def main(args: Array[String]): Unit = {
    println("Exercise 1. Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row) {
        print(pascal(col, row) + "\t")
      }
      println()
    }
    println()

    println("Exercise 2. Parentheses Balancing")
    val exampleString: String = "(Example (string) for ( Exercise ) 2. (Parentheses Balancing))" // true
    println("Bracket balance: " + balance(exampleString.toList))
    println()

    println("Exercise 3. Counting Change")
    val money: Int = 4
    val coins: List[Int] = List(1, 2)
    println("Number of ways: " + countChange(money, coins))
    println()

    println("Exercise 4. N-Queens Problem")
    val size: Int = 4
    val solution: Option[Array[Int]] = nQueens(size)
    if (solution.isDefined) {
      val board: Array[Int] = solution.get
      printQueens(board)
    } else {
      println("No solution found for size " + size)
    }
  }

  /**
   * Exercise 1. Pascal's Triangle
   */
  def pascal(c: Int, r: Int): Int = {
    if (c == 0 || c == r) {
      1
    }
    else {
      pascal(c - 1, r - 1) + pascal(c, r - 1)
    }
  }

  /**
   * Exercise 2. Parentheses Balancing
   */
  def balance(chars: List[Char]): Boolean = {
    def coutingBrackets(chars: List[Char], countOpenBrackets: Int, countCloseBracket: Int): Boolean = {
      if (chars.isEmpty) {
        countOpenBrackets == countCloseBracket
      } else if (countCloseBracket > countOpenBrackets) {
        false
      } else {
        val currentSymbol: Char = chars.head
        val remainingList: List[Char] = chars.tail

        if (currentSymbol == '(') {
          coutingBrackets(remainingList, countOpenBrackets + 1, countCloseBracket)
        } else if (currentSymbol == ')') {
          coutingBrackets(remainingList, countOpenBrackets, countCloseBracket + 1)
        } else {
          coutingBrackets(remainingList, countOpenBrackets, countCloseBracket)
        }
      }
    }

    coutingBrackets(chars, 0, 0)
  }

  /**
   * Exercise 3. Counting Change
   * Write a recursive function that counts how many different ways you can make
   * change for an amount, given a list of coin denominations. For example,
   * there is 1 way to give change for 5 if you have coins with denomiation
   * 2 and 3: 2+3.
   */
  def countChange(money: Int, coins: List[Int]): Int = {
    if (money == 0) {
      1
    }
    else if (money < 0) {
      0
    } else if (coins.isEmpty) {
      0
    } else {
      countChange(money - coins.head, coins) + countChange(money, coins.tail)
    }
  }

  /**
   * Exercise 4. N-Queens Problem
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
    val board: Array[Int] = new Array[Int](size)

    def isSafe(row: Int, col: Int): Boolean = {
      def checkPreviousRows(currentRow: Int): Boolean = {
        if (currentRow == row) {
          true
        } else {
          val placedCol: Int = board(currentRow)
          if (placedCol == col || placedCol - currentRow == col - row || placedCol + currentRow == col + row) {
            false
          } else {
            checkPreviousRows(currentRow + 1)
          }
        }
      }

      checkPreviousRows(0)
    }

    def placeQueens(row: Int): Boolean = {
      if (row == size) {
        true
      } else {
        def tryColumns(col: Int): Boolean = {
          if (col == size) {
            false
          } else {
            if (isSafe(row, col)) {
              board(row) = col
              if (placeQueens(row + 1)) {
                true
              } else {
                tryColumns(col + 1)
              }
            } else {
              tryColumns(col + 1)
            }
          }
        }

        tryColumns(0)
      }
    }

    if (placeQueens(0)) {
      Some(board)
    } else {
      None
    }
  }

  def printQueens(board: Array[Int]): Unit = {
    val size: Int = board.length
    for (row <- 0 until size) {
      for (col <- 0 until size) {
        if (board(row) == col) {
          print("1 ")
        } else {
          print("0 ")
        }
      }
      println()
    }
  }
}
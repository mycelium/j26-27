//package recfun
//import common._

object Main {
  def main(args: Array[String]) = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
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
    def countOpened(chars: List[Char], opened: Int) : Boolean = {
      (chars, opened) match {
        case (Nil, _) => if (opened > 0) false else true
        case ('(' :: xs, op) => countOpened(xs, op+1)
        case (')' :: xs, op) => if (op == 0) false else countOpened(xs, op-1)
        case (x :: xs, op) => countOpened(xs, op)
      }
    }

    countOpened(chars, 0)
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


  // Unit это аналог Void
  def printResult(result : List[Int]): Unit = {
    val size = result.size

    def printLine(target : Int, current : Int) : Unit = {
      if (current == target) print("1 ")
      else print("0 ")

      if (current == size - 1) print("\n")
      else printLine(target, current + 1)
    }

    def printRows(rows : List[Int]) : Unit = {
      if (rows.nonEmpty) {
        printLine(rows.head, 0)
        printRows(rows.tail)
      }
    }

    printRows(result)
  }

  //Main.printResult(Main.nQueens(4).get.toList)


  //Это задача об n ферзях, идею решения подсмотрел в https://www.hello-algo.com/ru/chapter_backtracking/n_queens_problem/#3

  def nQueens(size: Int): Option[Array[Int]] = {
    val result = Array.fill(size)(0)

    val cols = Array.fill(size)(true)
    val diag1 = Array.fill(size * 2 - 1)(true)
    val diag2 = Array.fill(size * 2 - 1)(true)

    def nextRaw(row : Int, col : Int) : Option[Array[Int]] = {
      // т.к нумерация с нуля, если достигли size то успешно заполнили все строки до этого
      if (row == size) Some(result)

      else if (col >= size) None

      else if (!cols(col) || !diag1(row - col + size - 1) || !diag2(row + col)) {
        nextRaw(row, col + 1)
      }

      else {
        // пробуем занять клетку в столбце
        cols(col) = false
        diag1(row - col + size - 1) = false
        diag2(row + col) = false
        result(row) = col

        // переходим на некст строчку
        nextRaw(row + 1, 0) match {
          case Some(res) => Some(res)

          case None =>
            // если в итоге внизу поля ферзи не расставились, то откатываем ферзя
            cols(col) = true
            diag1(row - col + size - 1) = true
            diag2(row + col) = true

            // и пробуем следующий столбец в текущей строке
            nextRaw(row, col + 1)
        }
      }
    }

    nextRaw(0, 0)
  }

}

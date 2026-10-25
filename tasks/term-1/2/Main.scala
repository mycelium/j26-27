package recfun

object Main {
  def main(args: Array[String]): Unit = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
    println("Введите строку для проверки баланса круглых скобок:")
    val input = scala.io.StdIn.readLine()
    if (input == null) {
      return
    }
    println(s"Скобки сбалансированы: ${balance(input.toList)}")

    println("Введите сумму для размена (одно неотрицательное целое число):")
    val moneyInput = scala.io.StdIn.readLine()
    if (moneyInput == null) {
      return
    }

    try {
      val money = moneyInput.trim.toInt
      if (money < 0) {
        println("ОШИБКА: Сумма должна быть неотрицательной.")
        return
      }

      println("Введите номиналы монет через пробел (положительные целые числа):")
      val coinsInput = scala.io.StdIn.readLine()
      if (coinsInput == null) {
        return
      }
      val coins = coinsInput.trim.split("\\s+").filter(_.nonEmpty).map(_.toInt).toList
      println(s"Количество вариантов размена: ${countChange(money, coins)}")
    } catch {
      case _: NumberFormatException =>
        println("ОШИБКА: Введите одну целую сумму и целые номиналы монет через пробел.")
      case _: IllegalArgumentException =>
        println("ОШИБКА: Номиналы монет должны быть положительными.")
    }

    println("Введите размер шахматной доски (одно положительное целое число):")
    val sizeInput = scala.io.StdIn.readLine()
    if (sizeInput == null) {
      return
    }

    try {
      val size = sizeInput.trim.toInt
      if (size <= 0) {
        println("ОШИБКА: Размер доски должен быть положительным.")
      } else {
        nQueens(size) match {
          case Some(queens) =>
            println(s"Номера колонок по строкам (с 0): ${queens.mkString("[", ", ", "]")}")
            println("Расстановка королев:")
            printBoard(queens)
          case None =>
            println(s"Для доски $size x $size решения нет.")
        }
      }
    } catch {
      case _: NumberFormatException =>
        println("ОШИБКА: Введите одно положительное целое число для размера доски.")
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
    @scala.annotation.tailrec
    def check(remaining: List[Char], opened: Int): Boolean = {
      remaining match {
        case Nil => opened == 0
        case '(' :: tail => check(tail, opened + 1)
        case ')' :: tail => opened > 0 && check(tail, opened - 1)
        case _ :: tail => check(tail, opened)
      }
    }

    check(chars, 0)
  }

  /**
   * Exercise 3 Counting Change
   * Write a recursive function that counts how many different ways you can make
   * change for an amount, given a list of coin denominations. For example,
   * there is 1 way to give change for 5 if you have coins with denomiation
   * 2 and 3: 2+3.
   */
  def countChange(money: Int, coins: List[Int]): Int = {
    require(coins.forall(_ > 0), "Номиналы монет должны быть положительными.")

    def count(remaining: Int, denominations: List[Int]): Int = {
      if (remaining == 0) 1
      else if (remaining < 0 || denominations.isEmpty) 0
      else count(remaining - denominations.head, denominations) +
        count(remaining, denominations.tail)
    }

    count(money, coins.distinct)
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
    @scala.annotation.tailrec
    def isSafe(column: Int, placed: List[Int], distance: Int): Boolean = {
      placed match {
        case Nil => true
        case previousColumn :: tail =>
          column != previousColumn && math.abs(column - previousColumn) != distance &&
            isSafe(column, tail, distance + 1)
      }
    }

    def place(row: Int, placed: List[Int]): Option[Array[Int]] = {
      if (row == size) {
        Some(placed.reverse.toArray)
      } else {
        @scala.annotation.tailrec
        def tryColumn(column: Int): Option[Array[Int]] = {
          if (column == size) {
            None
          } else if (isSafe(column, placed, 1)) {
            place(row + 1, column :: placed) match {
              case solution @ Some(_) => solution
              case None => tryColumn(column + 1)
            }
          } else {
            tryColumn(column + 1)
          }
        }

        tryColumn(0)
      }
    }

    if (size <= 0) None else place(0, Nil)
  }

  def printBoard(queens: Array[Int]): Unit = {
    @scala.annotation.tailrec
    def printCell(row: Int, column: Int): Unit = {
      if (row < queens.length) {
        print(if (queens(row) == column) 1 else 0)
        if (column + 1 == queens.length) {
          println()
          printCell(row + 1, 0)
        } else {
          print(" ")
          printCell(row, column + 1)
        }
      }
    }

    printCell(0, 0)
  }

}

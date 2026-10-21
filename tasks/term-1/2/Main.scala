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

    println("\nКорректное использование скобок")
    println(balance("(Небольшие (проверки (ф-ции (balance))))".toList)) // true
    println(balance(")()()(".toList)) // false
    println(balance("()()()(".toList)) // false
    println(balance("{-[-(-]-[-}-{-)-[-[-}".toList)) // true

    println("\nПодсчет вариантов сдачи")
    println(countChange(4, List(1, 2))) // 3
    println(countChange(5, List(2, 3))) // 1
    println(countChange(5, List(1, 2, 3))) // 5

    println("\nРасстановка ферзей (4x4, 6x6)")
    nQueens(4) match {
      case Some(solution) =>
        println("Решение: [" + solution.mkString(",") + "]")
        print_(solution)
      case None =>
        println("Нет решения")
    }
    nQueens(6) match {
      case Some(solution) =>
        println("Решение: [" + solution.mkString(",") + "]")
        print_(solution)
      case None =>
        println("Нет решения")
    }
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if (c < 0 || c > r) 0
    else if (c == 0 || c == r) 1
    else pascal(c - 1, r - 1) + pascal(c, r - 1)
  }

  /**
   * Exercise 2 Parentheses Balancing
   */
  def balance(chars: List[Char]): Boolean = {
    def loop(ost: List[Char], flag: Int): Boolean = {
      if(flag < 0) false // счетчик в минус ушел: закрыли больше, чем открыли
      else ost match {
        case Nil => flag == 0 // дошли до конца списка, если счетчик = 0, все хорошо
        case '(' :: tail => loop(tail, flag + 1)
        case ')' :: tail => loop(tail, flag - 1)
        case _ :: tail => loop(tail, flag) // все кроме скобок
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
    if(money == 0) 1 // дошли до 0 - 1 способ есть
    else if(money < 0) 0 // не подходит
    else coins match {
      case Nil => 0 // если пустой список - 0 способов
      case head :: tail => countChange(money - head, coins) + countChange(money, tail)
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
    def isValid(row: Int, col: Int, queens: Array[Int]) : Boolean = {
      var i = 0
      while(i < row){
        val c = queens(i)
        if(c == col || Math.abs(c - col) == row - i){ // проверка не тот же ли столбец и не та же диагональ
          return false
        }
        i += 1
      }
      true
    }
    def solve(row: Int, current : Array[Int]): Option[Array[Int]] = {
      if(row == size){ // все строки заполнены => возвращаем
        Some(current.clone())
      } else {
        var result: Option[Array[Int]] = None
        var col = 0
        while(col < size && result.isEmpty){
          if(isValid(row, col, current)){ // если можем поставить
            current(row) = col // ставим
            result = solve(row + 1, current) // расставляем дальше со след. строки
          }
          col += 1 // двигаемся к следующему столбцу
        }
        result // либо Some(current.clone()), либо None
      }
    }
    if(size <= 0) None
    else solve(0, Array.ofDim[Int](size))
  }

  def print_(problem: Array[Int]): Unit = {
    val n = problem.length
    for (r <- 0 until n){
      for (c <- 0 until n){
        if (problem(r) == c) print("1 ")
        else print("0 ")
      }
      println()
    }
  }
}
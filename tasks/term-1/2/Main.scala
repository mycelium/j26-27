//> using scala 2.13
package recfun
//import common._

object Main {
  def main(args: Array[String]): Unit = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }

    printArrayQueens(nQueens(15))
    println()
    
    println(balance(("(((1+5) - 8 + 6()))".toList)))
    println(countChange(95, List(1,2,3,10,15,20)))
  }

 


  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = { 
    if (c == 0 || c == r) 1
    else pascal(c-1, r-1) + pascal(c, r-1)
  }

  /**
   * Exercise 2 Parentheses Balancing
   */
  def balance(chars: List[Char]): Boolean = {
    def counterBracket(chars: List[Char], BracketCounterOpen: Int, BracketCounterClose: Int, openOrClose: Boolean = false): Boolean = {
      if (chars.isEmpty) BracketCounterOpen == BracketCounterClose
      else {
        chars.head match {
          case '(' => counterBracket(chars.tail, BracketCounterOpen +1 ,BracketCounterClose, true)
          case ')' => if (openOrClose == false) false
                      else {
                        if (BracketCounterOpen - BracketCounterClose == 1) counterBracket(chars.tail, BracketCounterOpen, BracketCounterClose +1, false)
                        else counterBracket(chars.tail, BracketCounterOpen, BracketCounterClose +1, true)}
          case _ => counterBracket(chars.tail, BracketCounterOpen, BracketCounterClose, openOrClose)
        }
      
      }

    } 
    counterBracket(chars, 0, 0, false)
   
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
    val queens = new Array[Int](size)
    def nextQueensInstal(strQueens : Int) : Boolean = {
      if (strQueens == size) true
      else {
        var complet = false
        var not_in_the_steps: Boolean = true
        var diagonally_valid: Boolean = true
        for (next_step <- 0 to size -1){
          for ( previous_steps <- 0 to strQueens - 1){
            if (next_step == queens(previous_steps)) not_in_the_steps  = false
            if (math.abs(queens(previous_steps) - next_step) ==  strQueens - previous_steps) diagonally_valid = false
        }
        if (!complet && not_in_the_steps && diagonally_valid){ 
        queens(strQueens)  = next_step
        if (nextQueensInstal(strQueens + 1)) complet = true
        }
        not_in_the_steps = true
        diagonally_valid = true
        }
        complet
        }

        }
      if (nextQueensInstal(0)) Some(queens) else None
      }

      def printArrayQueens(queensOpt : Option[Array[Int]]) = {
        queensOpt match {
          case None => println("Решения не найдено")
          case Some(queens) =>
            for (str <- 0 to queens.length -1 ){
              for (row <- 0 to queens.length -1 ){
                if(row == queens(str)) {print(1)} else {print(0)}
                }        
                println()
              }
        }
      }

}
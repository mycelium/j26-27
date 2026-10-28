file:///C:/Users/arllr/j26-27/tasks/term-1/2/Main.scala
empty definition using pc, found symbol in pc: 
semanticdb not found
empty definition using fallback
non-local guesses:
	 -Some.
	 -Some#
	 -Some().
	 -scala/Predef.Some.
	 -scala/Predef.Some#
	 -scala/Predef.Some().
offset: 3374
uri: file:///C:/Users/arllr/j26-27/tasks/term-1/2/Main.scala
text:
```scala
package recfun
import common._
import scala.collection.mutable.Stack
import scala.io.StdIn.readLine

object Main {
  def main(args: Array[String]) = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
    //val chars = readLine("Enter a string to compute the balance: ")

  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if (c == 1 || c == r) 1
    else pascal (r - 1, c - 1) + pascal (r - 1, c)
  }

  /**
   * Exercise 2 Parentheses Balancing
   */
  def balance(chars: List[Char]): Boolean = {
    var charList: List[Char] = List('(', ')')
    var openingChars: List[Char] = List('(')
    val stack = Stack[Char]()

    for (char <- chars){
        if (charList.contains(char)){
            if (openingChars.contains(char)){
              stack.push(char)
            }
            else {
              if (stack.nonEmpty){
                stack.pop()
              }
              else{
                println(s"Parentheses are unbalanced, add 1 left parenthesis")
                return false
              }
            }
        }
    }
    val isBalanced = stack.isEmpty
    if (isBalanced){
      println("Parentheses are balanced")
    } 
    else{
      val count = stack.size
      val plural = if (count == 1) "parenthesis" else "parentheses"
      println(s"Parentheses are unbalanced, add $count right $plural")
    }

    isBalanced
  }

  /**
   * Exercise 3 Counting Change
   * Write a recursive function that counts how many different ways you can make
   * change for an amount, given a list of coin denominations. For example,
   * there is 1 way to give change for 5 if you have coins with denomiation
   * 2 and 3: 2+3.
   */
  def countChange(money: Int, coins: List[Int]): Int = {
    if (money < 0) 0
    else if (money == 0) 1
    else if (coins.isEmpty) 0
    else {
       countChange(money, coins.tail)  + countChange(money - coins.head, coins)
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
    var board = Array.fill(size, size)(0)

    def isSafe(row: Int, col: Int) : Boolean = {
      (0 until col).forall{i =>
        val rowSafe = board(row)(i) == 0
        val upperDiagSafe = if (row - (col - i) >= 0) board(row - (col-i))(i) == 0 else true
        val lowerDiagSafe = if (row + (col - i) < board.length) board(row + (col - i))(i) == 0 else true

        rowSafe && upperDiagSafe && lowerDiagSafe
        }
  }
    def backtrack(col: Int) : Boolean ={
      if (col >= size) return true

      for (row <- 0 until size){
        if (isSafe(i, col)) {
          board(i)(col) = 1 
          if (bactrack(col + 1)) return true
          board(i)(col) = 0
        }
      }
      false
    }

    if (!backtrack(0)):{
      println(s"Solution does not exist for N = $size")
      None
    }
    else{
      @@Some(board)
    }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 
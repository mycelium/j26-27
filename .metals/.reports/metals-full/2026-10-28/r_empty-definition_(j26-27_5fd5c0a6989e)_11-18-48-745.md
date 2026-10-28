file:///C:/Users/arllr/j26-27/tasks/term-1/2/Main.scala
empty definition using pc, found symbol in pc: 
semanticdb not found
empty definition using fallback
non-local guesses:
	 -common/Int#
	 -Int#
	 -scala/Predef.Int#
offset: 455
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
  def pascal(c: Int, r: Int): In@@t = {
    var triangle: List[Int] = List()
    

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

  }

}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 
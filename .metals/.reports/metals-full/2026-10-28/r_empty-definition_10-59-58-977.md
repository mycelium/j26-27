error id: file:///C:/Users/arllr/j26-27/tasks/term-1/2/Main.scala:List.
file:///C:/Users/arllr/j26-27/tasks/term-1/2/Main.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 648
uri: file:///C:/Users/arllr/j26-27/tasks/term-1/2/Main.scala
text:
```scala
package recfun
import common._
import scala.collection.mutable.Stack
import scala.io.StdIn.readLine

object Main {
  def main(args: Array[String]) {
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

  }

  /**
   * Exercise 2 Parentheses Balancing
   */
  def balance(chars: List[Char]): Boolean = {
    var charList: List[Char] = List('(', ')')
    var openingListList[Char] = @@List('(')
    var charList: List[Char] = List('(', ')')
    val stack = Stack[String]()

    for (char <- chars){
        if (charList.contains(char)){

        }
    }

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
# Lab 2: Scala, recursion

Group: 40102
Name: Ovi Md Shamin Yasir

## Files

`Main.scala` has the four functions from the task and a `main` that tests them. All four functions use recursion (with small inner functions, as the task allows). There are no loops inside them.

I removed the line `import common._` from the file, because there is no `common` package in this folder and the file does not use it.

## The functions

**`pascal(c, r)`**. A number in the triangle is the sum of the two numbers above it: `pascal(c - 1, r - 1) + pascal(c, r - 1)`. The first and the last number in a row (`c == 0` or `c == r`) are 1, this is where the recursion stops. If the column is outside of the row, the function throws an exception.

**`balance(chars)`**. I go through the list and count how many `(` are still open. `(` adds 1, `)` subtracts 1, other characters do nothing. If the count becomes less than 0, there is a `)` without a pair, so the answer is `false` (for example `())(` ). At the end the count must be 0. The inner function `check` has the counter as a parameter.

**`countChange(money, coins)`**. I take the first coin. There are two ways: use this coin (the money becomes `money - coin`, the coins stay the same), or never use this coin again (the money stays, the coin is removed from the list). The answer is the sum of the two ways. When the money is exactly 0, it is 1 way. When the money is below 0, or there are no coins but some money is left, it is 0 ways. Coins that are 0 or negative are not allowed (they would make the recursion endless), so the function throws an exception for them.

**`nQueens(size)`**. Backtracking: I put the queens row by row. In each row I try the columns from the left to the right. If a column is safe, I go to the next row. If the next rows can not be finished, I come back and try the next column. If all columns are tried and nothing works, the result is `None` (for size 2 and 3 there is no solution).

A new queen is safe when no queen is in the same column and no queen is on the same diagonal. For a queen that is `dist` rows above, the diagonals are `col - dist` and `col + dist`. This is checked in `isSafe`.

The result is an array: the index is the row, the value is the column. `printBoard` prints the board, where 1 is a queen and 0 is an empty cell.

## How to run

```
scalac Main.scala
scala recfun.Main
```

## Results

```
Pascal's Triangle
1 
1 1 
1 2 1 
1 3 3 1 
1 4 6 4 1 
1 5 10 10 5 1 
1 6 15 20 15 6 1 
1 7 21 35 35 21 7 1 
1 8 28 56 70 56 28 8 1 
1 9 36 84 126 126 84 36 9 1 
1 10 45 120 210 252 210 120 45 10 1 

Balance
true
true
false
false
false
true

Count change
1
3
1
0
1022

N-Queens
1: [0]
2: no solution
3: no solution
4: [1,3,0,2]
5: [0,2,4,1,3]
6: [1,3,5,0,2,4]
7: [0,2,4,6,1,3,5]
8: [0,4,7,5,2,6,1,3]

Board 4x4:
0 1 0 0 
0 0 0 1 
1 0 0 0 
0 0 1 0 

Board 8x8:
1 0 0 0 0 0 0 0 
0 0 0 0 1 0 0 0 
0 0 0 0 0 0 0 1 
0 0 0 0 0 1 0 0 
0 0 1 0 0 0 0 0 
0 0 0 0 0 0 1 0 
0 1 0 0 0 0 0 0 
0 0 0 1 0 0 0 0 
```

The answer for the 4x4 board is the same as the example in the task (`[1,3,0,2]`). For the balance tests the order is: a correct text with `(` `)`, a longer correct text, `:-)`, `())(`, `(()` and an empty text. `countChange(300, List(5, 10, 20, 50, 100, 200, 500))` is 1022.

Note: `pascal` and `countChange` call themselves many times with the same arguments, so they are slow for big numbers. For the sizes in this lab it is fast enough.

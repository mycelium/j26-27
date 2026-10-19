# Lab 1: Matrix multiplication

Group: 40102
Name: Ovi Md Shamin Yasir

## Files

`MatrixMult.java` has:

- `multiply` - the function from the task (fast version).
- `multiplySlow` - the usual version with three loops. I use it only to compare the time.
- `main` - a small test, then multiplication of big random matrices (500x500 and 1000x1000) with the time of both versions.

If the sizes do not fit (columns of the first matrix are not equal to rows of the second), `multiply` throws an exception.

## How to run

```
javac MatrixMult.java
java MatrixMult
```

## How I made it faster

**1. I changed the order of the loops.**

The usual order is `i, j, k`. In the inner loop `k` changes, so the program reads `secondMatrix[0][j]`, then `secondMatrix[1][j]`, then `secondMatrix[2][j]` and so on. These numbers are in different rows, so they are far from each other in memory and the processor cache does not help.

I use the order `i, k, j`. Now `j` changes in the inner loop, so the program reads numbers from one row of `secondMatrix` and writes to one row of `result`, one after another. This is much better for the cache. The answer is the same.

**2. I put `firstMatrix[i][k]` into a variable `a`.**

This number does not change while the inner loop runs, so there is no need to read it from the array again and again. The inner loop has less work to do.

## Results

```
fast: [[58.0, 64.0], [139.0, 154.0]]
slow: [[58.0, 64.0], [139.0, 154.0]]
Size 500x500
fast: 70 ms
slow: 382 ms
Size 1000x1000
fast: 479 ms
slow: 1538 ms
```

The small test shows that both versions give the same answer. For 500x500 the fast version is about 5 times faster, for 1000x1000 it is about 3 times faster. The time depends on the computer, so the numbers can be different on another one.

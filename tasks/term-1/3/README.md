# Lab 3: Scala, Functional Set

Group: 40102
Name: Ovi Md Shamin Yasir

## Files

- `src/main/scala/funsets/FunSets.scala` - all the functions that were `???` are written.
- `src/main/scala/funsets/Main.scala` - a few tests, they print the sets.

The other files (`build.sbt`, `common/package.scala`...) are not changed.

## The idea

A set is not a list of numbers. It is a function `Int => Boolean`, which answers the question "is this number in the set?". The functions below build new such functions from the old ones.

| function | how it works |
|---|---|
| `singletonSet(elem)` | `x => x == elem`, true only for one number |
| `union(s, t)` | `x => s(x) \|\| t(x)`, in `s` or in `t` |
| `intersect(s, t)` | `x => s(x) && t(x)`, in both |
| `diff(s, t)` | `x => s(x) && !t(x)`, in `s` but not in `t` |
| `filter(s, p)` | `x => s(x) && p(x)`, in `s` and `p` is true |
| `forall(s, p)` | goes through the numbers from -1000 to 1000 (`bound`), if a number is in `s` and `p` is false for it, the answer is `false` |
| `exists(s, p)` | "some number has `p`" is the same as "not all numbers have not `p`", so `!forall(s, x => !p(x))` |
| `map(s, f)` | `y => exists(s, x => f(x) == y)`, `y` is in the new set if some `x` from `s` gives `f(x) == y` |

`forall` and `exists` look only at the numbers from -1000 to 1000, because a function can not check all the integers one by one. `forall` is recursive (the inner function `iter` goes from `a` to `a + 1`).

## Where are the elements stored?

Nowhere. The set does not keep its elements. It keeps only the rule how to answer the question.

- `singletonSet(5)` is the function `x => x == 5`. The number 5 is saved inside the function (as the variable `elem`), but it is not a "list with one element", it is a part of the rule.
- `union(s, t)` saves the two functions `s` and `t`. The new function asks both of them.
- `map` saves `s` and `f`. The elements of the new set do not exist until somebody asks. To answer one question, `exists` goes through 2001 numbers.
- The set of all even numbers is `x => x % 2 == 0`. It has infinitely many elements, but it is only one small function in memory.

The elements "appear" only when we ask. For example `toString` asks the question for every number from -1000 to 1000 and prints the numbers with the answer `true`. So we pay with time (every question is calculated again, a set made with many `union` and `map` calls is slow), but we do not need memory for the elements.

## How to run

The simple way is to compile with `scalac` (in this folder):

```
mkdir out
scalac -d out src/main/scala/common/package.scala src/main/scala/funsets/FunSets.scala src/main/scala/funsets/Main.scala
scala -cp out funsets.Main
```

## Results

```
true
singletonSet(2): {2}
union of 1 and 2: {1,2}
intersect {1,2} and {2,3}: {2}
diff {1,2} and {2,3}: {1}
filter {1,2,3} for x > 1: {2,3}
forall {1,2,3} x < 5: true
forall {1,2,3} x < 3: false
exists {1,2,3} x == 2: true
exists {1,2,3} x == 7: false
map {1,2,3} with x * 2: {2,4,6}
map {1,2,3} with x - 10: {-9,-8,-7}
5 in even: false
1000 in even: true
1000000 in even: true
```

The last line is interesting: 1000000 is outside of the range -1000..1000, but `contains` works for it, because it just calls the function. Only `forall`, `exists` and `toString` are limited by `bound`.

I also compared all the functions with the normal Scala `Set` on 300 random sets and many different conditions (`p` and `f`). The answers were the same every time.

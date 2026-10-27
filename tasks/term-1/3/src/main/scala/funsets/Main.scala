package funsets

object Main extends App {
  import FunSets._

  val evens: Set = x => x % 2 == 0
  val small: Set = x => x >= 1 && x <= 6

  println(contains(singletonSet(1), 1))         // true
  printSet(union(singletonSet(1), singletonSet(3)))  // {1,3}
  printSet(intersect(small, evens))             // {2,4,6}
  printSet(diff(small, evens))                  // {1,3,5}
  printSet(filter(small, x => x > 4))                // {5,6}
  println(forall(intersect(small, evens), x => x % 2 == 0))  // true
  println(exists(small, x => x > 10))                // false
  printSet(map(small, x => x * x))              // {1,4,9,16,25,36}
}

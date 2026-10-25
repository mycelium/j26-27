package funsets

object Main extends App {
  import FunSets._

  val s1 = singletonSet(1)
  val s2 = singletonSet(2)
  val s3 = singletonSet(3)
  val s = union(union(s1, s2), s3)

  println("contains")
  println(contains(s1, 1))                // true
  println(contains(s1, 2))                // false

  println("union / intersect / diff")
  printSet(union(s1, s2))                 // {1,2}
  printSet(intersect(s, s2))              // {2}
  printSet(intersect(s1, s2))             // {}
  printSet(diff(s, s2))                   // {1,3}

  println("filter")
  printSet(filter(s, x => x > 1))         // {2,3}
  printSet(filter(s, x => x % 2 == 0))    // {2}

  println("forall / exists")
  println(forall(s, x => x > 0))          // true
  println(forall(s, x => x > 2))          // false
  println(exists(s, x => x > 2))          // true
  println(exists(s, x => x > 5))          // false

  println("map")
  printSet(map(s, x => x * 2))            // {2,4,6}
  printSet(map(s, x => 0))                // {0}
}
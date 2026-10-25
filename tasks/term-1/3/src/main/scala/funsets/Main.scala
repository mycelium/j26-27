package funsets

object Main extends App {
  import FunSets._

  val s1 = singletonSet(1)
  val s2 = singletonSet(2)
  val s3 = singletonSet(3)
  val s12 = union(s1, s2)
  val s123 = union(s12, s3)

  println(contains(s1, 1))                          // true
  println(contains(s1, 2))                          // false

  printSet(s12)                                     // {1,2}
  printSet(intersect(s12, s1))                      // {1}
  printSet(diff(s12, s1))                           // {2}
  printSet(filter(s123, x => x % 2 == 1))           // {1,3}

  println(forall(s123, x => x > 0))                 // true
  println(forall(s123, x => x < 3))                 // false
  println(exists(s123, x => x > 2))                 // true
  println(exists(s123, x => x > 10))                // false

  printSet(map(s123, x => x * 10))                  // {10,20,30}
  printSet(map(s123, x => x * x))                   // {1,4,9}
}

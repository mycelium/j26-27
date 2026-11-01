package funsets

object Main extends App {
  import FunSets._

  val s1 = singletonSet(1)
  val s2 = singletonSet(2)

  println(contains(s1, 1))
  println(contains(s1, 2))

  printSet(union(s1, s2))
  printSet(intersect(s1, s2))
  printSet(diff(s1, s2))

  printSet(filter(union(s1, s2), (x: Int) => x % 2 == 0))

  println(forall(s1, (x: Int) => x > 0))
  println(exists(s1, (x: Int) => x == 1))

  printSet(map(s1, (x: Int) => x + 10))
}

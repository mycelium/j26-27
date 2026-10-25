package funsets

object Main extends App {
  import FunSets._
  println(contains(singletonSet(1), 1))

  val a: Set = x => x >= 80 && x <= 190
  val b: Set = x => x >= 48 && x <= 52

  printSet(union(a, b))
  printSet(intersect(a, b))
  printSet(diff(a, b))
  printSet(filter(a, x => x % 3 == 0))
  println(forall(a,x => x % 3 == 0))
  println(exists(a,x => x % 3 == 0))
  printSet(map(a,x => x * 2))

}

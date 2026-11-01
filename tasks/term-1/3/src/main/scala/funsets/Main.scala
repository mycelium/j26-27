package funsets

object Main extends App {
  import FunSets._

  val set1 = singletonSet(1)
  val set2 = singletonSet(2)
  val set3 = singletonSet(3)

  println("singletonSet:")
  println(contains(set1, 1))

  println("union:")
  val unionSet = union(set1, set2)
  printSet(unionSet)

  println("intersect:")
  printSet(intersect(unionSet, set2))

  println("diff:")
  printSet(diff(unionSet, set2))

  println("filter:")
  val set123 = union(union(set1, set2), set3)
  printSet(filter(set123, x => x > 1))

  println("forall:")
  println(forall(set123, x => x > 0))

  println("exists:")
  println(exists(set123, x => x == 2))

  println("map:")
  printSet(map(set123, x => x * 2))
}
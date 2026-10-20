package funsets

object Main extends App {
  import FunSets._
  println(contains(singletonSet(1), 1))

  val s1 = singletonSet(1)
  val s2 = singletonSet(2)
  val s3 = union(s1, s2)
  println(contains(s3, 1))
  println(contains(s3, 2))

  val set = union(singletonSet(1), singletonSet(3))
  println(exists(set, x => x == 1))
  printSet(map(set, x => x * 2))
}

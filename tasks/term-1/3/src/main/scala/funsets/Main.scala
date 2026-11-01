package funsets

object Main extends App {
  import FunSets._

  val s1 = singletonSet(1)
  val s2 = singletonSet(2)
  val s3 = singletonSet(3)
  val s12 = union(s1, s2)
  val s23 = union(s2, s3)
  val s123 = union(s12, s3)

  println("contains(s1, 1): " + contains(s1, 1))
  println("contains(s1, 2): " + contains(s1, 2))

  print("union {1,2} и {3}: ")
  printSet(s123)

  print("intersect {1,2} и {2,3}: ")
  printSet(intersect(s12, s23))

  print("diff {1,2,3} и {2}: ")
  printSet(diff(s123, s2))

  print("filter {1,2,3}, x > 1: ")
  printSet(filter(s123, x => x > 1))

  println("forall {1,2,3}, x > 0: " + forall(s123, x => x > 0))
  println("forall {1,2,3}, x > 1: " + forall(s123, x => x > 1))

  println("exists {1,2,3}, x == 2: " + exists(s123, x => x == 2))
  println("exists {1,2,3}, x > 5: " + exists(s123, x => x > 5))

  print("map {1,2,3}, x * 10: ")
  printSet(map(s123, x => x * 10))
}
package funsets

object Main extends App {

  import FunSets._

  private val s1 = singletonSet(1)
  private val s2 = singletonSet(2)
  private val s3 = singletonSet(3)
  private val u = union(union(s1, s2), s3)

  println(contains(s1, 1)) // true
  println(FunSets.toString(u)) // {1,2,3}
  println(FunSets.toString(intersect(u, s2))) // {2}
  println(FunSets.toString(diff(u, s2))) // {1,3}
  println(FunSets.toString(filter(u, _ > 1))) // {2,3}
  println(forall(u, _ > 0)) // true
  println(exists(u, _ == 2)) // true
  println(FunSets.toString(map(u, _ * 2))) // {2,4,6}
}
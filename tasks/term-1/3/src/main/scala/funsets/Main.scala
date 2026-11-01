package funsets

object Main extends App {
  import FunSets._
  println(contains(singletonSet(1), 1))

  // prints a text and the elements of the set
  // (I write FunSets.toString, because plain "toString" in an object means
  // the toString of this object itself)
  def show(text: String, s: Set): Unit = println(text + ": " + FunSets.toString(s))

  val s1 = singletonSet(1)
  val s2 = singletonSet(2)
  val s3 = singletonSet(3)

  show("singletonSet(2)", s2)
  show("union of 1 and 2", union(s1, s2))

  val s12 = union(s1, s2)
  val s23 = union(s2, s3)
  show("intersect {1,2} and {2,3}", intersect(s12, s23))
  show("diff {1,2} and {2,3}", diff(s12, s23))

  val s123 = union(s12, s3)
  show("filter {1,2,3} for x > 1", filter(s123, x => x > 1))

  println("forall {1,2,3} x < 5: " + forall(s123, x => x < 5))
  println("forall {1,2,3} x < 3: " + forall(s123, x => x < 3))
  println("exists {1,2,3} x == 2: " + exists(s123, x => x == 2))
  println("exists {1,2,3} x == 7: " + exists(s123, x => x == 7))

  show("map {1,2,3} with x * 2", map(s123, x => x * 2))
  show("map {1,2,3} with x - 10", map(s123, x => x - 10))

  // a set with many elements: all even numbers. It is just one small function.
  val even: Set = x => x % 2 == 0
  println("5 in even: " + contains(even, 5))
  println("1000 in even: " + contains(even, 1000))
  println("1000000 in even: " + contains(even, 1000000))
}

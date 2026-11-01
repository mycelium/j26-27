package funsets

object Main extends App {
  import FunSets._

  val numbers = union(singletonSet(5), singletonSet(26))

  printSet(numbers)
  println(contains(numbers, 1))
  println(forall(numbers, x => x > 0))
  println(exists(numbers, x => x == 2))
  printSet(map(numbers, x => x * 10))
}

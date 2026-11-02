package funsets

object Main extends App {
  import FunSets._

  val one = singletonSet(1)
  val two = singletonSet(2)
  val three = singletonSet(3)
  val numbers = union(one, union(two, three))

  print("Set: ")
  printSet(numbers)

  print("Union with {4}: ")
  printSet(union(numbers, singletonSet(4)))

  print("Intersection with {2, 3}: ")
  printSet(intersect(numbers, union(two, three)))

  print("Difference with {2}: ")
  printSet(diff(numbers, two))

  print("Odd values: ")
  printSet(filter(numbers, value => value % 2 != 0))

  println(s"All values are positive: ${forall(numbers, value => value > 0)}")
  println(s"Contains an even value: ${exists(numbers, value => value % 2 == 0)}")

  print("Squares: ")
  printSet(map(numbers, value => value * value))
}

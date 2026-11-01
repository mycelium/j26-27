package funsets

object Main extends App {
  import FunSets._

  println("--- Проверка singletonSet и contains ---")
  println(contains(singletonSet(1), 1)) // true
  println(contains(singletonSet(1), 2)) // false

  println("\n--- Проверка union и intersect ---")
  val s1 = singletonSet(1)
  val s2 = singletonSet(2)
  val s3 = union(s1, s2)
  printSet(s3) // {1,2}
  
  val s4 = intersect(s3, union(singletonSet(2), singletonSet(3)))
  printSet(s4) // {2}

  println("\n--- Проверка diff и filter ---")
  val s5 = diff(s3, s1)
  printSet(s5) // {2}
  
  val s6 = filter(s3, x => x > 1)
  printSet(s6) // {2}

  println("\n--- Проверка map ---")
  val s7 = map(s3, x => x * 10)
  printSet(s7) // {10,20}

  println("\n--- Проверка forall и exists ---")
  println(forall(s3, x => x > 0)) // true
  println(exists(s3, x => x == 2)) // true
}
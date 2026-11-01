package funsets

object Main extends App {
  import FunSets._
  println(contains(singletonSet(1), 1))

  val s1 = singletonSet(1)
  val s2 = singletonSet(2)
  val s3 = singletonSet(3)

  // singletonSet
  assert(contains(s1, 1))
  assert(!contains(s1, 2))

  // union
  val s12 = union(s1, s2)
  assert(contains(s12, 1) && contains(s12, 2) && !contains(s12, 3))

  // intersect
  val s123 = union(s12, s3)
  assert(contains(intersect(s12, s123), 1))
  assert(contains(intersect(s12, s123), 2))
  assert(!contains(intersect(s12, s123), 3))
  assert(!contains(intersect(s1, s2), 1))

  // diff
  val d = diff(s123, s2)
  assert(contains(d, 1) && !contains(d, 2) && contains(d, 3))

  // filter
  val evens = filter(s123, x => x % 2 == 0)
  assert(!contains(evens, 1) && contains(evens, 2) && !contains(evens, 3))

  // forall
  assert(forall(s123, x => x > 0))
  assert(!forall(s123, x => x > 1))
  assert(forall(singletonSet(1000), x => x == 1000))
  assert(forall(singletonSet(-1000), x => x == -1000))
  assert(forall(x => false, x => false))   // пустое множество: условие выполняется для всех

  // exists
  assert(exists(s123, x => x == 2))
  assert(!exists(s123, x => x > 3))
  assert(!exists(x => false, x => true))   // в пустом множестве ничего нет
  assert(exists(singletonSet(1000), x => x == 1000))

  // map
  val m = map(s123, x => x * 2)
  assert(FunSets.toString(m) == "{2,4,6}")
  val m2 = map(union(union(s1, s3), singletonSet(4)), x => x - 1)
  assert(FunSets.toString(m2) == "{0,2,3}")
  assert(FunSets.toString(map(s123, x => 5)) == "{5}")

  printSet(s123)
  printSet(m)
  println("Все проверки пройдены")
}

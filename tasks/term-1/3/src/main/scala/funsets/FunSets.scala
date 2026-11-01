package funsets

object FunSets {
  // сет тут просто функция: даем число, получаем true или false
  type Set = Int => Boolean

  def contains(s: Set, elem: Int): Boolean = s(elem)

  def singletonSet(elem: Int): Set = x => x == elem

  def union(s: Set, t: Set): Set = x => s(x) || t(x)

  def intersect(s: Set, t: Set): Set = x => s(x) && t(x)

  def diff(s: Set, t: Set): Set = x => s(x) && !t(x)

  def filter(s: Set, p: Int => Boolean): Set = x => s(x) && p(x)

  // бесконечный сет целиком не перебрать, поэтому проверяем только этот диапазон
  val bound = 1000

  def forall(s: Set, p: Int => Boolean): Boolean = {
    def iter(a: Int): Boolean = {
      if (a > bound) true
      else if (s(a) && !p(a)) false
      else iter(a + 1)
    }
    iter(-bound)
  }

  def exists(s: Set, p: Int => Boolean): Boolean = !forall(s, x => !p(x))

  def map(s: Set, f: Int => Int): Set = y => exists(s, x => f(x) == y)

  def toString(s: Set): String = {
    val xs = for (i <- -bound to bound if contains(s, i)) yield i
    xs.mkString("{", ",", "}")
  }

  def printSet(s: Set): Unit = {
    println(toString(s))
  }
}

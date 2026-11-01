package funsets

import common._

object FunSets {

  type Set = Int => Boolean

  def contains(s: Set, elem: Int): Boolean = s(elem)

  def singletonSet(elem: Int): Set =
    (x: Int) => x == elem

  def union(s: Set, t: Set): Set =
    (x: Int) => s(x) || t(x)

  def intersect(s: Set, t: Set): Set =
    (x: Int) => s(x) && t(x)

  def diff(s: Set, t: Set): Set =
    (x: Int) => s(x) && !t(x)

  def filter(s: Set, p: Int => Boolean): Set =
    (x: Int) => s(x) && p(x)
  
  val bound = 1000

  def forall(s: Set, p: Int => Boolean): Boolean = {
    def iter(a: Int): Boolean = {
      if (a > bound) true
      else if (s(a) && !p(a)) false
      else iter(a + 1)
    }

    iter(-bound)
  }

  def exists(s: Set, p: Int => Boolean): Boolean =
    !forall(s, (x: Int) => !p(x))

  def map(s: Set, f: Int => Int): Set =
    (x: Int) => exists(s, (elem: Int) => f(elem) == x)

  def toString(s: Set): String = {
    val xs = for (i <- -bound to bound if contains(s, i)) yield i
    xs.mkString("{", ",", "}")
  }

  def printSet(s: Set): Unit = {
    println(toString(s))
  }
}

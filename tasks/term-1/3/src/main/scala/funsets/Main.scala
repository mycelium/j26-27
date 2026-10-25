package funsets

object Main extends App {
  import FunSets._

  var passed = 0

  def check(name: String, condition: Boolean): Unit = {
    assert(condition, s"Тест не пройден: $name")
    passed += 1
  }

  def checkSet(name: String, actual: Set, expected: List[Int]): Unit = {
    check(name, (-bound to bound).forall(x => contains(actual, x) == expected.contains(x)))
  }

  def printed(s: Set): String = {
    val output = new java.io.ByteArrayOutputStream()
    Console.withOut(new java.io.PrintStream(output, true, "UTF-8")) {
      printSet(s)
    }
    output.toString("UTF-8").replace("\r\n", "\n")
  }

  val empty: Set = _ => false
  val universal: Set = _ => true
  val first: Set = x => x == -2 || x == 0 || x == 3
  val second: Set = x => x == 0 || x == 3 || x == 4
  val limits = union(singletonSet(-bound), singletonSet(bound))
  val outside = singletonSet(bound + 1)

  check("contains: отрицательный элемент", contains(first, -2))
  check("contains: ноль", contains(first, 0))
  check("contains: отсутствующий элемент", !contains(first, 2))
  check("contains: пустое множество", !contains(empty, 0))
  check("contains: минимальное целое число", contains(universal, Int.MinValue))
  println("contains: OK")

  checkSet("singletonSet: положительный элемент", singletonSet(5), List(5))
  checkSet("singletonSet: отрицательный элемент", singletonSet(-5), List(-5))
  checkSet("singletonSet: ноль", singletonSet(0), List(0))
  checkSet("singletonSet: нижняя граница", singletonSet(-bound), List(-bound))
  checkSet("singletonSet: верхняя граница", singletonSet(bound), List(bound))
  println("singletonSet: OK")

  checkSet("union: пересекающиеся множества", union(first, second), List(-2, 0, 3, 4))
  checkSet("union: пустое множество слева", union(empty, first), List(-2, 0, 3))
  checkSet("union: пустое множество справа", union(first, empty), List(-2, 0, 3))
  checkSet("union: два пустых множества", union(empty, empty), Nil)
  checkSet("union: множество с самим собой", union(first, first), List(-2, 0, 3))
  checkSet("union: непересекающиеся множества", union(singletonSet(-1), singletonSet(1)), List(-1, 1))
  println("union: OK")

  checkSet("intersect: общие элементы", intersect(first, second), List(0, 3))
  checkSet("intersect: пустое множество справа", intersect(first, empty), Nil)
  checkSet("intersect: пустое множество слева", intersect(empty, first), Nil)
  checkSet("intersect: множество с самим собой", intersect(first, first), List(-2, 0, 3))
  checkSet("intersect: непересекающиеся множества", intersect(singletonSet(1), singletonSet(2)), Nil)
  checkSet("intersect: универсальное множество", intersect(first, universal), List(-2, 0, 3))
  println("intersect: OK")

  checkSet("diff: первое минус второе", diff(first, second), List(-2))
  checkSet("diff: второе минус первое", diff(second, first), List(4))
  checkSet("diff: множество минус само себя", diff(first, first), Nil)
  checkSet("diff: вычитание пустого множества", diff(first, empty), List(-2, 0, 3))
  checkSet("diff: вычитание из пустого множества", diff(empty, first), Nil)
  println("diff: OK")

  checkSet("filter: положительные элементы", filter(first, x => x > 0), List(3))
  checkSet("filter: чётные элементы", filter(first, x => x % 2 == 0), List(-2, 0))
  checkSet("filter: всегда истинное условие", filter(first, _ => true), List(-2, 0, 3))
  checkSet("filter: всегда ложное условие", filter(first, _ => false), Nil)
  checkSet("filter: пустое множество", filter(empty, _ => true), Nil)
  println("filter: OK")

  check("forall: условие выполнено для всех", forall(first, x => x >= -2))
  check("forall: есть контрпример", !forall(first, x => x >= 0))
  check("forall: пустое множество", forall(empty, _ => false))
  check("forall: ноль", forall(singletonSet(0), x => x % 2 == 0))
  check("forall: нижняя граница включена", !forall(singletonSet(-bound), x => x > -bound))
  check("forall: верхняя граница включена", !forall(singletonSet(bound), x => x < bound))
  check("forall: элемент вне диапазона", forall(outside, _ => false))
  println("forall: OK")

  check("exists: подходящий элемент существует", exists(first, x => x < 0))
  check("exists: подходящего элемента нет", !exists(first, x => x == 2))
  check("exists: пустое множество", !exists(empty, _ => true))
  check("exists: ноль", exists(singletonSet(0), x => x == 0))
  check("exists: нижняя граница включена", exists(limits, x => x == -bound))
  check("exists: верхняя граница включена", exists(limits, x => x == bound))
  check("exists: элемент вне диапазона", !exists(outside, _ => true))
  println("exists: OK")

  checkSet("map: умножение на два", map(first, x => x * 2), List(-4, 0, 6))
  checkSet("map: прибавление единицы", map(first, x => x + 1), List(-1, 1, 4))
  checkSet("map: тождественное преобразование", map(first, x => x), List(-2, 0, 3))
  checkSet("map: постоянное значение", map(first, _ => 7), List(7))
  checkSet("map: пустое множество", map(empty, _ => 7), Nil)
  checkSet("map: совпадающие результаты",
    map(union(singletonSet(-2), singletonSet(2)), x => math.abs(x)), List(2))
  val shiftedLimit = map(singletonSet(bound), x => x + 1)
  check("map: результат вне диапазона обхода", contains(shiftedLimit, bound + 1))
  check("map: исходный элемент отсутствует в результате", !contains(shiftedLimit, bound))
  println("map: OK")

  check("toString: пустое множество", FunSets.toString(empty) == "{}")
  check("toString: один отрицательный элемент", FunSets.toString(singletonSet(-1)) == "{-1}")
  check("toString: несколько элементов", FunSets.toString(first) == "{-2,0,3}")
  check("toString: границы диапазона", FunSets.toString(limits) == "{-1000,1000}")
  check("toString: элемент вне диапазона", FunSets.toString(outside) == "{}")
  println("toString: OK")

  check("printSet: пустое множество", printed(empty) == "{}\n")
  check("printSet: один элемент", printed(singletonSet(3)) == "{3}\n")
  check("printSet: несколько элементов", printed(first) == "{-2,0,3}\n")
  check("printSet: элемент вне диапазона", printed(outside) == "{}\n")
  println("printSet: OK")

  println(s"Все проверки успешно пройдены. Всего: $passed.")
}

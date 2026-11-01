package funsets

object Main extends App {
  import FunSets._
  println(contains(singletonSet(1), 1))

  val sSet1 = singletonSet(1)
  val sSet2 = singletonSet(2)

  println("\nUnion: ")
  val unionSet = union(sSet1, sSet2)

  println(contains(unionSet, 2))
  println(contains(unionSet, 3))

  println("\nIntersection")
  val intersectSet = intersect(sSet1, unionSet)

  println(contains(intersectSet, 1))
  println(contains(intersectSet, 2))

  println("\nDifference")
  val diffSet = diff(unionSet, intersectSet)

  println(contains(diffSet, 1))
  println(contains(diffSet, 2))

  println("\nFilter")
  val filterSet = filter(unionSet, x => x > 1)  

  println(contains(filterSet, 1))   
  println(contains(filterSet, 2))  

  println("\nForall")
  println(forall(unionSet, x => x > 0))   
  println(forall(unionSet, x => x > 1))  

  println("\nExists")
  println(exists(unionSet, x => x > 1))  
  println(exists(unionSet, x => x > 5))   

  println("\nMap")
  val mapSet = map(unionSet, x => x * 2)  

  println(contains(mapSet, 2))   
  println(contains(mapSet, 4))   
  println(contains(mapSet, 1))  
  printSet(mapSet)

}

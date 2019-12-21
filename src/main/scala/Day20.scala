import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Queue

object Day20 {
  trait Tile { val isTeleport: Boolean; val isWall: Boolean; val name: String}

  case class Tp(name: String) extends Tile{ val isTeleport = true; val isWall = false}
  case class Empty() extends Tile{ val isTeleport: Boolean = false; val isWall = false; val name = "NO" }
  case class Wall() extends Tile{val isTeleport: Boolean = false; val isWall = true; val name = "NO" }

  val input = scala.io.Source.fromFile("data/data20.txt", "UTF-8").getLines.map(_.split("").toVector).toVector

  def part1(): Unit = {
    // parse grid
    val posMap: scala.collection.mutable.Map[IPos, Tile] = scala.collection.mutable.Map.empty
    for (i <- input.indices) {
      for (j <- input(0).indices) {
        if (input(i)(j) == "#") posMap(IPos(j,i)) = Wall()
        else if (input(i)(j) == ".") {
          if (input(i - 1)(j)(0).toChar.isLetter) {
            posMap(IPos(j,i)) = Tp(input(i - 2)(j) + input(i - 1)(j))
          }
          else if (input(i + 1)(j)(0).toChar.isLetter) {
            posMap(IPos(j,i)) = Tp(input(i + 1)(j) + input(i + 2)(j))
          }
          else if (input(i)(j + 1)(0).toChar.isLetter) {
            posMap(IPos(j,i)) = Tp(input(i)(j + 1) + input(i)(j + 2))
          }
          else if (input(i)(j - 1)(0).toChar.isLetter) {
            posMap(IPos(j,i)) = Tp(input(i)(j - 2) + input(i)(j - 1))
          }
          else posMap(IPos(j,i)) = Empty()
        }
      }
    }
    // tp link setup
    val tpLinks: scala.collection.mutable.Map[IPos, IPos] = scala.collection.mutable.Map.empty
    for (i <- posMap.filter(p => p._2.isTeleport)) {
      val link = posMap.find(p => i._2.name == p._2.name && i._1 != p._1)
      if (link != None) {println(link.get._2.name); tpLinks(i._1) = link.get._1}
    }
    
    val start = posMap.find(p => p._2.name == "AA").get
    val end = posMap.find(p => p._2.name == "ZZ").get
    val mainPosList: ArrayBuffer[IPos] = ArrayBuffer(start._1)
    val positions: Queue[IPos] = Queue(start._1)
    var count = 0
    while (!positions.contains(end._1)) {
      for (i <- positions.indices) {
        val head = positions.dequeue()
        var newLocations: Vector[IPos] = Vector.empty
        for (i <- head.y - 1 to head.y + 1; j <- head.x-1 to head.x + 1; if ((i == head.y || j == head.x))){
          newLocations :+= IPos(j,i)
        }
        if (posMap(head).isTeleport && tpLinks.contains(head)) newLocations :+= tpLinks(head)
        newLocations = newLocations.filter(p => posMap.contains(p) && !posMap(p).isWall && !mainPosList.contains(p))
        positions ++= newLocations
        mainPosList ++= newLocations
      }
      count += 1
    }
    println(count)
  }

  def printGrid(xs: scala.collection.mutable.Map[IPos, Tile]) = {
    val x = xs.map(_._1.x)
    val y = xs.map(_._1.y)
    for (i <- y.min to y.max) {
      for (j <- x.min to x.max) {
        if (!xs.contains(IPos(j,i))) print(" ")
        else if (xs(IPos(j,i)).isWall) print("#")
        else if (xs(IPos(j,i)).isTeleport) print("T")
        else print(".")
      }
      println("")
    }
  }

  case class State(pos: IPos, level: Int, prevLevels: Set[String])

  def part2(): Unit = {
     // parse grid
     val posMap: scala.collection.mutable.Map[IPos, Tile] = scala.collection.mutable.Map.empty
     for (i <- input.indices) {
       for (j <- input(0).indices) {
         if (input(i)(j) == "#") posMap(IPos(j,i)) = Wall()
         else if (input(i)(j) == ".") {
           if (input(i - 1)(j)(0).toChar.isLetter) {
             posMap(IPos(j,i)) = Tp(input(i - 2)(j) + input(i - 1)(j))
           }
           else if (input(i + 1)(j)(0).toChar.isLetter) {
             posMap(IPos(j,i)) = Tp(input(i + 1)(j) + input(i + 2)(j))
           }
           else if (input(i)(j + 1)(0).toChar.isLetter) {
             posMap(IPos(j,i)) = Tp(input(i)(j + 1) + input(i)(j + 2))
           }
           else if (input(i)(j - 1)(0).toChar.isLetter) {
             posMap(IPos(j,i)) = Tp(input(i)(j - 2) + input(i)(j - 1))
           }
           else posMap(IPos(j,i)) = Empty()
         }
       }
     }
     // tp link setup
     val tpLinks: scala.collection.mutable.Map[IPos, IPos] = scala.collection.mutable.Map.empty
     for (i <- posMap.filter(p => p._2.isTeleport)) {
       val link = posMap.find(p => i._2.name == p._2.name && i._1 != p._1)
       if (link != None) {println(link.get._2.name); tpLinks(i._1) = link.get._1}
     }
     
     val start = posMap.find(p => p._2.name == "AA").get
     val end = posMap.find(p => p._2.name == "ZZ").get
     val states: Queue[State] = Queue(State(start._1,0, Set()))
     var count = 0
     var found = false
     val mainPosList: scala.collection.mutable.Set[(IPos,Int)] = scala.collection.mutable.Set((start._1,0))
     while (!found && states.length != 0) {
       println(count)
       for (i <- states.indices) {
          val head = states.dequeue()
          var newStates: Vector[State] = Vector.empty
          for (i <- head.pos.y - 1 to head.pos.y + 1; j <- head.pos.x-1 to head.pos.x + 1; if ((i == head.pos.y || j == head.pos.x))){
            newStates :+= State(IPos(j,i), head.level, head.prevLevels)
          }
          if (posMap(head.pos).isTeleport && tpLinks.contains(head.pos)) {
            if (head.prevLevels.contains(posMap(head.pos).name)) {
              newStates :+= State(tpLinks(head.pos), head.level - 1, head.prevLevels - posMap(head.pos).name)
            }
            else {
              newStates :+= State(tpLinks(head.pos), head.level + 1, head.prevLevels + posMap(head.pos).name)
            }
          }
          states ++= newStates.filter(p => posMap.contains(p.pos) && !posMap(p.pos).isWall && !mainPosList.contains((p.pos,p.level)))
          mainPosList ++= newStates.map(p => (p.pos,p.level))
       }
       count += 1
       val k = states.find(p => p.pos == end._1 && p.level == 0)
       if (k != None) {found = true; println("BOIIIII")}
     }
     println(count)
  }

  def apply() = {
    println("SOLUTION DAY 20")
    println("Running part 1")
    //part1()
    println("Running part 2")
    part2()
  }
}
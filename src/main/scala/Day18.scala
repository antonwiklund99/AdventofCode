import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Queue

case class IPos(x: Int, y: Int) {
  def +(p: IPos) = IPos(x + p.x, y + p.y)
  def -(p: IPos) = IPos(x - p.x, y - p.y)
}

case class Key(name: String, pos: IPos) {
  val distanceToKeys: Map[String, Int] = Map.empty
}

case class State(pos: IPos, walkedKeys: Vector[Key], steps: Int)

object Day18 {
  val input = scala.io.Source.fromFile("data/data18.txt", "UTF-8").getLines.map(_.split("")).toArray
  val alphabet: Set[String] = "abcdefghijklmnopqrstuvwxyz".split("").toSet
  val keys: Map[String, Key] = input.flatten.filter(p => alphabet.contains(p)).map({ s =>
    val y = input.indexWhere(_.contains(s))
    (s -> Key(s, IPos(input(y).indexOf(s), y)))
  }).toMap
  val keyNames = keys.map(_._1).toSet
  val doorNames = keyNames.map(_.toUpperCase())
  val entrance: IPos = {
    val y = input.indexWhere(_.contains("@"))
    IPos(input(y).indexOf("@"), y)
  }
  /*
  def part1(): Unit = {
    for (fromKey <- keys; toKey <- keys; if (fromKey != toKey && !fromKey._2.distanceToKeys.contains(toKey._1))) {
      getKeyInfo(fromKey._2, toKey._2)
    }
  }

  def part2(): Unit = {

  }

  def getKeyInfo(fromKey: Key, toKey: Key): Unit = {
    val mainPosList: scala.collection.mutable.Set[IPos] = scala.collection.mutable.Set(IPos(0,0))
    val states: Queue[State] = Queue(State(fromKey.pos, Vector.empty, 0))
    var found = false
    while (!found) {
      for (i <- states.indices) {
        val state = states.dequeue()
        var newLocations: Vector[State] = Vector.empty
        for (i <- state.pos.y - 1 to state.pos.y + 1; j <- state.pos.x-1 to state.pos.x + 1){
          if ((state.pos.x == j || state.pos.y == i) && !mainPosList.contains(IPos(j,i)) && input(j)(i) != "#") {
            if (keyNames.contains(input(i)(j))) {
              state.walkedKeys.foreach({k => 
                if (!k.distanceToKeys.contains(input(i)(j))){
                  keys(k.name).distanceToKeys(input(i)(j)) = state.steps - fromKey.distanceToKeys(k.name)
                  keys(input(i)(j)).distanceToKeys(k.name) = state.steps - fromKey.distanceToKeys(k.name)
                }
              })
              fromKey.distanceToKeys(input(i)(j)) = state.steps
              keys(input(i)(j)).distanceToKeys(fromKey.name) = state.steps
              newLocations :+= State(IPos(j,i), state.walkedKeys :+ keys(input(i)(j)), state.steps + 1)
            }
            else newLocations :+= State(IPos(j,i), state.walkedKeys, state.steps + 1)
          }
        }
        states ++= newLocations
        mainPosList ++= newLocations.map(_.pos)
      }
    }
  }
  */
  def apply() = {
    println("SOLUTION DAY 18")
    println("Running part 1")
    //part1()
    println("Running part 2")
    //part2()
  }
}
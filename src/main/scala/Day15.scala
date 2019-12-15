import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Queue

case class Droid(comp: Comp13, counter: Int, pos: Pos, status: Long = 1L) {
  def west(): Droid = {
    val newComp = comp.clone()
    val res = newComp.run(3L)
    Droid(newComp, counter + 1, pos + Pos(-1,0), res)
  }

  def east(): Droid = {
    val newComp = comp.clone()
    val res = newComp.run(4L)
    Droid(newComp, counter + 1, pos + Pos(1,0), res)
  }

  def south(): Droid = {
    val newComp = comp.clone()
    val res = newComp.run(2L)
    Droid(newComp, counter + 1, pos + Pos(0,-1), res)
  }

  def north(): Droid = {
    val newComp = comp.clone()
    val res = newComp.run(1L)
    Droid(newComp, counter + 1, pos + Pos(0,1), res)
  }
}

object Day15 {
  val initMem = scala.io.Source.fromFile("data/data15.txt", "UTF-8").getLines.toVector(0).split(',').map(_.toLong).toVector

  def printGrid(xs: ArrayBuffer[Pos]): Unit = {
    var res = ""
    val xMax = xs.map(_.x.toInt).max
    val xMin = xs.map(_.x.toInt).min
    val yMax = xs.map(_.y.toInt).max
    val yMin = xs.map(_.y.toInt).min
    for (y <- yMin to yMax) {
      for (x <- xMin to xMax) {
        if (xs.contains(Pos(x,y))) res += "0"
        else res += " "
      }
      res += "\n"
    }
    print("\u001b[2J")
    println(res)
  }

  def part1(): Droid = {
    val mainPosList: ArrayBuffer[Pos] = ArrayBuffer(Pos(0,0))
    val droids: Queue[Droid] = Queue(Droid(new Comp13(initMem.toArray ++ Array.fill(10000)(0L)), 0, Pos(0,0)))
    var oxy: Option[Droid] = None
    while (oxy == None) {
      for (i <- droids.indices) {
        val droid = droids.dequeue()
        val newLocations = Vector(droid.west, droid.east, droid.north, droid.south).filter(p => !mainPosList.contains(p.pos) && p.status != 0)
        droids ++= newLocations
        mainPosList ++= newLocations.map(_.pos)
      }
      oxy = droids.find(p => p.status == 2)
      //printGrid(mainPosList)
    }
    oxy.get
  }

  def part2(): Unit = {
    val oxy = part1()
    val mainPosList: ArrayBuffer[Pos] = ArrayBuffer(oxy.pos)
    val droids: Queue[Droid] = Queue(Droid(oxy.comp, 0, oxy.pos, oxy.status))
    var i = 0
    while (!droids.isEmpty) {
      i = droids(0).counter
      for (i <- droids.indices) {
        val droid = droids.dequeue()
        val newLocations = Vector(droid.west, droid.east, droid.north, droid.south).filter(p => !mainPosList.contains(p.pos) && p.status != 0)
        droids ++= newLocations
        mainPosList ++= newLocations.map(_.pos)
      }
      printGrid(mainPosList)
    }
    println(i)
  }

  def apply() = {
    println("SOLUTION DAY 15")
    println("Running part 1")
    println(part1())
    println("Running part 2")
    part2()
  }
}
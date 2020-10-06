import scala.collection.mutable.Queue

object Day19 {
  val initMem = scala.io.Source.fromFile("data/data19.txt", "UTF-8").getLines.toVector(0).split(',').map(_.toLong).toVector

  def checkPos(x: Int, y: Int): Boolean = {
    val comp = new Comp17(initMem.toArray ++ Array.fill(10000)(0L))
    comp.run(Queue(x, y))
    comp.lastOutput == 1
  }
  
  def part1(): Unit = {
    var count = 0
    for (i <- 0 to 49; j <- 0 to 49; if (checkPos(j,i))) count += 1
    println(count)
  }

  def part2(): Unit = {
    var found = false
    var startY = 100
    while(!found) {
      var startX = 0
      while (!checkPos(startX,startY)) startX += 1
      println(s"CHECKING X = $startX Y = $startY")
      if (checkPos(startX + 99, startY) && checkPos(startX + 99, startY - 99)) {println(s"X = $startX Y = ${startY-99}"); found = true}
      startY += 1
    }
  }

  def apply() = {
    println("SOLUTION DAY 19")
    println("Running part 1")
    part1()
    println("Running part 2")
    part2()
  }
}
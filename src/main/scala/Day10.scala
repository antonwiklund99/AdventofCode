case class Pos(x: Double, y: Double) {
  def +(p: Pos) = new Pos(x + p.x, y + p.y)
  def -(p: Pos) = new Pos(x - p.x, y + p.y)
  def pointsBetween(p: Pos): Vector[Pos] = {
    var res: Vector[Pos] = Vector.empty
    if (y == p.y) {
      for (i <- math.min(x, p.x).toInt + 1 to math.max(x, p.x).toInt - 1) {
        res :+= Pos(i.toDouble,y)
      }
    }
    else if (x == p.x) {
      for (i <- math.min(y, p.y).toInt + 1 to math.max(y, p.y).toInt - 1) {
        res :+= Pos(x,i.toDouble)
      }
    }
    else {
      val k: Double = (y - p.y) / (x - p.x)
      val m: Double = y - k*x
      //println("y = " + k + "x + " + m)
      var incr = 0
      if (x > p.x) incr = -1
      else incr = 1
      for (xx <- x.toInt + incr to p.x.toInt - incr by incr) {
        val yy = k*xx.toDouble + m
        if ((yy % 1) == 0) {
          res :+= Pos(xx.toDouble, yy)
        }
      }
    }
    res
  }
}

object Day10 {
  val input = scala.io.Source.fromFile("data/data10.txt", "UTF-8").getLines.toVector
 
  def part1(): Unit = {
    val grid = input.map(_.split(""))
    var positions: Vector[Pos] = Vector.empty
    for (i <- grid.indices) {
      for (j <- grid(0).indices) {
        if (grid(i)(j) == "#") positions :+= Pos(j,i)
      }
    }
    var res = 0
    for (p <- positions) {
      var sum = 0
      for (pos <- positions.diff(Vector(p))) {
        val interPoints = p.pointsBetween(pos)
        if (interPoints.forall(f => !positions.contains(f))) sum += 1
      }
      res = math.max(sum, res)
    }
    println(res)
  }

  def part2(): Unit = {

  }

  def apply() = {
    println("SOLUTION DAY 10")
    println("Running part 1")
    part1()
    println("Running part 2")
    //part2()
  }
}
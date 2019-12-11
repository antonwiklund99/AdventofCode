case class Pos(x: Double, y: Double) extends Ordered[Pos] {
  def +(p: Pos) = new Pos(x + p.x, y + p.y)
  def -(p: Pos) = new Pos(x - p.x, y - p.y)
  def angleTo(p: Pos): Double = {
    val diff = p - this
    var a = math.atan2(math.abs(diff.y), math.abs(diff.x))
    // Första kvadrant
    if (diff.x >= 0 && diff.y >= 0) a
    // Andra kvadranten
    else if (diff.x < 0 && diff.y >= 0) math.Pi - a
    // Tredje kvadranten
    else if (diff.x < 0 && diff.y < 0) math.Pi + a
    // Fjärde kvadranten
    else 2*math.Pi - a
  }

  def compare(that: Pos): Int = {
    if (that.y == this.y) {
      if (that.x == that.y) 0
      else if (this.x > that.x) 1
      else -1
    }
    else if (this.y > that.y) 1
    else -1
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
    for (pos <- positions) {
      var sum = 0
      sum = positions.diff(Vector(pos)).map(p => pos.angleTo(p)).distinct.length
      res = math.max(sum,res)
    }
    println(res)
  }

  def part2(): Unit = {
    val grid = input.map(_.split(""))
    var positions: Vector[Pos] = Vector.empty
    val base = Pos(31,20)
    for (i <- grid.indices) {
      for (j <- grid(0).indices) {
        // Byt plats på x och y axeln
        if (grid(i)(j) == "#") positions :+= Pos(j, i)
      }
    }
    val xs = positions.diff(Vector(base)).sortBy(p => base.angleTo(p))
    println(xs)
    //for (i <- 0 until 200) {
    //}
  }

  def apply() = {
    println("SOLUTION DAY 10")
    println("Running part 1")
    part1()
    println("Running part 2")
    part2()
  }
}
case class Pos(x: Double, y: Double) {
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

  def length(p: Pos): Double = math.hypot(math.abs(x -p.x), math.abs(y - p.y))
}

object Day10 {
  val input = scala.io.Source.fromFile("data/data10.txt", "UTF-8").getLines.toVector
 
  def part1(): Unit = {
    val grid = input.map(_.split(""))
    var positions: Vector[Pos] = Vector.empty
    for (i <- grid.indices; j <- grid(0).indices) {
      if (grid(i)(j) == "#") positions :+= Pos(j,i)
    }
    println(positions.map(pos => positions.diff(Vector(pos)).map(p => pos.angleTo(p)).distinct.length).max)
  }

  def part2(): Unit = {
    val grid = input.map(_.split(""))
    var positions: Vector[Pos] = Vector.empty
    val base = Pos(20,31)
    for (i <- grid.indices) {
      for (j <- grid(0).indices) {
        // Byt plats på x och y axeln
        if (grid(i)(j) == "#") positions :+= Pos(i,j)
      }
    }
    val xs = positions.diff(Vector(base)).sortBy(p => {
      if (base.angleTo(p) == math.Pi) 0
      else if (base.angleTo(p) > math.Pi) 3*math.Pi - base.angleTo(p)
      else math.Pi - base.angleTo(p)
    })
    
    var count = 0
    var i = 0
    var lastKill = Pos(0,0)
    while (count < 200) {
      val pts = xs.distinct.filter(p => xs(i).angleTo(base) == p.angleTo(base)).sortBy(_.length(base))
      lastKill = pts.head
      count += 1
      i += pts.length
    }
    println(lastKill)
    println(lastKill.y*100 + lastKill.x)
  }

  def apply() = {
    println("SOLUTION DAY 10")
    println("Running part 1")
    part1()
    println("Running part 2")
    part2()
  }
}
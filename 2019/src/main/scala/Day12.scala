import scala.util.matching.Regex
import scala.collection.mutable

class DPos(var x: Int, var y: Int, var z: Int) {
  def +(that: DPos): DPos = new DPos(x + that.x, y + that.y, z + that.z)
  def energy(): Int = math.abs(x) + math.abs(y) + math.abs(z)
  override def toString = s"Pos($x,$y,$z)"
}

class Moon(var pos: DPos, var velocity: DPos = new DPos(0,0,0)) {
  def applyVelocity(): Unit = pos += velocity
  override def toString(): String = s"Pos = $pos Velocity = $velocity"
}

object Day12 {
  val input = scala.io.Source.fromFile("data/data12.txt", "UTF-8").getLines.toVector
  
  def part1(): Unit = {
    val pattern = """(-?\d+)""".r
    val moons = input.map(pattern.findAllIn(_).toVector).map(f => new Moon(new DPos(f(0).toInt,f(1).toInt,f(2).toInt)))
    for (i <- 1 to 1000) {
      for (m <- moons.toSet[Moon].subsets.map(_.toVector).filter(p => p.length == 2).toVector) {
        if (m(0).pos.x > m(1).pos.x)      {m(0).velocity.x -= 1; m(1).velocity.x += 1}
        else if (m(0).pos.x < m(1).pos.x) {m(0).velocity.x += 1; m(1).velocity.x -= 1}

        if (m(0).pos.y > m(1).pos.y)      {m(0).velocity.y -= 1; m(1).velocity.y += 1}
        else if (m(0).pos.y < m(1).pos.y) {m(0).velocity.y += 1; m(1).velocity.y -= 1}

        if (m(0).pos.z > m(1).pos.z)      {m(0).velocity.z -= 1; m(1).velocity.z += 1}
        else if (m(0).pos.z < m(1).pos.z) {m(0).velocity.z += 1; m(1).velocity.z -= 1}
      }
      moons.foreach(_.applyVelocity())
    }
    println(moons.map(p => p.pos.energy * p.velocity.energy()).sum)
  }
  
  def lcm(xs: Seq[BigInt]): BigInt = xs.foldLeft(1:BigInt){
    (a, b) => b * a /
    Stream.iterate((a,b)){case (x,y) => (y, x%y)}.dropWhile(_._2 != 0).head._1.abs
  }

  def part2(): Unit = {
    val pattern = """(-?\d+)""".r
    val moons = input.map(pattern.findAllIn(_).toVector).map(f => new Moon(new DPos(f(0).toInt,f(1).toInt,f(2).toInt)))
    var prevStatesX: Set[Vector[(Int,Int)]] = Set(moons.map(m => (m.pos.x, m.velocity.x)))
    var prevStatesY: Set[Vector[(Int,Int)]] = Set(moons.map(m => (m.pos.y, m.velocity.y)))
    var prevStatesZ: Set[Vector[(Int,Int)]] = Set(moons.map(m => (m.pos.z, m.velocity.z)))
    var (xCycle, yCycle,zCycle) = (0,0,0)
    var i = 1
    while(xCycle == 0 || yCycle == 0 || zCycle == 0) {
      for (m <- moons.toSet[Moon].subsets.map(_.toVector).filter(p => p.length == 2).toVector) {
        if (m(0).pos.x > m(1).pos.x)      {m(0).velocity.x -= 1; m(1).velocity.x += 1}
        else if (m(0).pos.x < m(1).pos.x) {m(0).velocity.x += 1; m(1).velocity.x -= 1}

        if (m(0).pos.y > m(1).pos.y)      {m(0).velocity.y -= 1; m(1).velocity.y += 1}
        else if (m(0).pos.y < m(1).pos.y) {m(0).velocity.y += 1; m(1).velocity.y -= 1}

        if (m(0).pos.z > m(1).pos.z)      {m(0).velocity.z -= 1; m(1).velocity.z += 1}
        else if (m(0).pos.z < m(1).pos.z) {m(0).velocity.z += 1; m(1).velocity.z -= 1}
      }
      moons.foreach(_.applyVelocity())
      if (xCycle == 0 && prevStatesX.contains(moons.map(m => (m.pos.x, m.velocity.x)))) {println("Found x: " + i);xCycle = i}
      if (yCycle == 0 && prevStatesY.contains(moons.map(m => (m.pos.y, m.velocity.y)))) {println("Found y: " + i);yCycle = i}
      if (zCycle == 0 && prevStatesZ.contains(moons.map(m => (m.pos.z, m.velocity.z)))) {println("Found z: " + i);zCycle = i}
      prevStatesX += moons.map(m => (m.pos.x, m.velocity.x))
      prevStatesY += moons.map(m => (m.pos.y, m.velocity.y))
      prevStatesZ += moons.map(m => (m.pos.z, m.velocity.z))
      i += 1
    }

    println(s"x = $xCycle y = $yCycle z = $zCycle")
    println(lcm(List(xCycle,yCycle,zCycle)))
  }

  def apply() = {
    println("SOLUTION DAY 12")
    println("Running part 1")
    part1()
    println("Running part 2")
    part2()
  }
}
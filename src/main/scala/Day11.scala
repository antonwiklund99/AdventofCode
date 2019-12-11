class Compu(var mem: Array[Long], var pc: Int = 0, var relativeBase: Int = 0) {
  private def parseArgs(numArgs: Int): Vector[Int] = {
    var modes = mem(pc).toString.substring(0,math.max(mem(pc).toString.length() - 2,0)).map(_.toString.toInt).reverse
    for (i <- 0 until numArgs - modes.length) modes = modes :+ 0
    var res: scala.collection.mutable.ArrayBuffer[Int] = scala.collection.mutable.ArrayBuffer.empty
    for (i <- modes.indices) {
      val x = modes(i) match {
        case 0 => mem(pc + i + 1).toInt
        case 1 => pc + i + 1
        case 2 => mem(pc + i + 1).toInt + relativeBase
        case _ => {throw new Exception("WRONG MODE"); -1}
      }
      res += x
    }
    res.toVector
  }

  def run(in: Long): Vector[Long] = {
    var out: Vector[Long] = Vector.empty
    while (pc < mem.length && mem(pc) != 99) {
      //println("pc = " + pc + " mem(pc) = " + mem(pc) + " relativeBase = " + relativeBase + " mem(12) = " + mem(12))
      mem(pc).toString.last.toString.toInt match {
        case 1 => {
          val indexes = parseArgs(3)
          mem(indexes(2)) = mem(indexes(0)) + mem(indexes(1))
          pc += 4
        }
        case 2 => {
            val indexes = parseArgs(3)
            mem(indexes(2)) = mem(indexes(0)) * mem(indexes(1))
            pc += 4
        }
        case 3 => {
          val indexes = parseArgs(1)
          mem(indexes(0)) = in
          pc += 2
        }
        case 4 => {
          val indexes = parseArgs(1)
          out :+= mem(indexes(0))
          pc += 2
          if (out.length == 2) return out
        }
        case 5 => {
          val indexes = parseArgs(2)
          if (mem(indexes(0)) != 0) pc = mem(indexes(1)).toInt
          else pc += 3
        }
        case 6 => {
          val indexes = parseArgs(2)
          if (mem(indexes(0)) == 0) pc = mem(indexes(1)).toInt
          else pc += 3
        }
        case 7 => {
          val indexes = parseArgs(3)
          mem(indexes(2)) = if (mem(indexes(0)) < mem(indexes(1))) 1 else  0
          pc += 4
        }
        case 8 => {
          val indexes = parseArgs(3)
          mem(indexes(2)) = if (mem(indexes(0)) == mem(indexes(1))) 1 else  0
          pc += 4
        }
        case 9 => {
          val indexes = parseArgs(1)
          relativeBase += mem(indexes(0)).toInt
          pc += 2
        }
        case _ => {println("mem(pc) = " + mem(pc)); println("instruction = " + mem(pc).toString.last.toString.toInt); throw new Exception()}
      }
    }
    Vector(-1,-1)
  }
}

object Day11 {
  val initMem = scala.io.Source.fromFile("data/data11.txt", "UTF-8").getLines.toVector(0).split(',').map(_.toLong).toVector

  def part1(): Unit = {
    val computer = new Compu(initMem.toArray ++ Array.fill(10000)(0L))
    // 0 black 1 white
    var positions: scala.collection.mutable.Map[Pos,Int] = scala.collection.mutable.Map(Pos(0,0) -> 0)
    var pos = Pos(0,0)
    var dir = "U"
    var out = Vector(0,0)
    while (out != Vector(-1,-1)) {
      out = computer.run({
        if(positions.contains(pos)) positions(pos)
        else 0}).map(_.toInt)
      if (out != Vector(-1,-1)) {
        positions(pos) = out(0)
        pos += {
          dir match {
            case "U" => if (out(1) == 0) {dir = "L";Pos(-1,0)} else {dir = "R"; Pos(1,0)}
            case "L" => if (out(1) == 0) {dir = "D";Pos(0,1)} else {dir = "U"; Pos(0,-1)}
            case "D" => if (out(1) == 0) {dir = "R";Pos(1,0)} else {dir = "L"; Pos(-1,0)}
            case "R" => if (out(1) == 0) {dir = "U";Pos(0,-1)} else {dir = "D"; Pos(0,1)}
          }
        }
      }
    }
    println(positions.keys.toVector.length)
  }

  def part2(): Unit = {
    val computer = new Compu(initMem.toArray ++ Array.fill(10000)(0L))
    // 0 black 1 white
    var positions: scala.collection.mutable.Map[Pos,Int] = scala.collection.mutable.Map(Pos(0,0) -> 1)
    var pos = Pos(0,0)
    var dir = "U"
    var out = Vector(0,0)
    while (out != Vector(-1,-1)) {
      out = computer.run({
        if(positions.contains(pos)) positions(pos)
        else 0}).map(_.toInt)
      if (out != Vector(-1,-1)) {
        positions(pos) = out(0)
        pos += {
          dir match {
            case "U" => if (out(1) == 0) {dir = "L";Pos(-1,0)} else {dir = "R"; Pos(1,0)}
            case "L" => if (out(1) == 0) {dir = "D";Pos(0,1)} else {dir = "U"; Pos(0,-1)}
            case "D" => if (out(1) == 0) {dir = "R";Pos(1,0)} else {dir = "L"; Pos(-1,0)}
            case "R" => if (out(1) == 0) {dir = "U";Pos(0,-1)} else {dir = "D"; Pos(0,1)}
          }
        }
      }
    }
    val sortedY = positions.toVector.map(_._1.y.toInt).sorted
    val sortedX = positions.toVector.map(_._1.x.toInt).sorted
    for (i <- sortedY.head - 2 to sortedY.last + 2) {
      for (j <- sortedX.head - 2 to sortedX.last + 2) {
        if (positions.contains(Pos(j,i)) && positions(Pos(j,i)) == 1) print("#")
        else print("-")
      }
      println("")
    } 
  }

  def apply() = {
    println("SOLUTION DAY 11")
    println("Running part 1")
    part1()
    println("Running part 2")
    part2()
  }
}
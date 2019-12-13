class Comp13(var mem: Array[Long], var pc: Int = 0, var relativeBase: Int = 0) {
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

  def run(input: Long): Long = {
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
          mem(indexes(0)) = input
          pc += 2
        }
        case 4 => {
          val indexes = parseArgs(1)
          pc += 2
          return mem(indexes(0))
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
    -1
  }
}
case class Tile(x: Long, y: Long, id: Long)

object Day13 {
  val initMem = scala.io.Source.fromFile("data/data13.txt", "UTF-8").getLines.toVector(0).split(',').map(_.toLong).toVector

  def part1(): Unit = {
    val comp: Comp13 = new Comp13(initMem.toArray ++ Array.fill(10000)(0L))
    var tiles: Vector[Tile] = Vector.empty
    var halt = false
    while(!halt) {
      var res: Vector[Long] = Vector(comp.run(0),comp.run(0),comp.run(0))
      if (res.contains(-1)) halt = true
      else tiles +:= Tile(res(0), res(1), res(2))
    }
    println(tiles.filter(p => p.id == 2).length)
  }

  def part2(): Unit = {
    val comp: Comp13 = new Comp13(initMem.toArray ++ Array.fill(10000)(0L))
    comp.mem(0) = 2
    var tiles: scala.collection.mutable.Map[(Long,Long),Tile] = scala.collection.mutable.Map.empty
    var halt = false
    var ballDirX = 0L
    var lastBallX = 0L
    var ballDirY = 0L
    var lastBallY = 0L
    while(!halt) {
      var res: Vector[Long] = Vector.empty
      var ball = tiles.values.find(_.id == 4)
      //println(ball)
      var player = tiles.values.find(_.id == 3)
      var x = 0
      for (i <- 0 to 2) {
        if (player == None || ball == None) res :+= comp.run(0)
        else if (ball.get.x == player.get.x && ball.get.y == player.get.y - 1) res :+= comp.run(0)
        else if (player.get.x > ball.get.x + ballDirX) res :+= comp.run(-1)
        else if (player.get.x < ball.get.x + ballDirX) res :+= comp.run(1)
        else res :+= comp.run(0)
        x += 1
      }
      //println(tiles.values.filter(p => p.x == 22 && p.y == 20).toVector)
      //if (tiles.contains((res(0),res(1)))) println(tiles((res(0),res(1))))
      //println(res)
      if (res(2) == 4) {
        println("BALL MOVED TO " + res(0) + "," + res(1))
        ballDirX = {
          if (tiles.contains((res(0) + ballDirX,res(1) + ballDirY)) && (tiles((res(0) + ballDirX,res(1) + ballDirY)).id == 2 ||
              tiles((res(0) + ballDirX,res(1) + ballDirY)).id == 1))  {
            println("its gonna hit wall or block")
            if (ballDirX == -1) 1 else -1}
          else res(0) - lastBallX
        }
        ballDirY = {
          if (tiles.contains((res(0) + ballDirX,res(1) + ballDirY)) && (tiles((res(0) + ballDirX,res(1) + ballDirY)).id == 2 ||
              tiles((res(0) + ballDirX,res(1) + ballDirY)).id == 1 || tiles((res(0) + ballDirX,res(1) + ballDirY)).id == 3)) {
            println("its gonna hit wall or block or padel")
            if (ballDirY == -1) 1 else -1
          }
          else res(1) - lastBallY
        }
        println(s"x = $ballDirX y = $ballDirY")
        lastBallX = res(0)
        lastBallY = res(1)
      }
      else if (res(2) == 3) println("PADEL MOVED TO " + res(0) + "," + res(1))
      if (res.forall(_ == -1)) halt = true
      else if (res(0) == -1 && res(1) == 0) println(res(2))
      else {
        tiles((res(0),res(1))) = Tile(res(0), res(1), res(2))
      }
    }
  }

  def apply() = {
    println("SOLUTION DAY 13")
    println("Running part 1")
    part1()
    println("Running part 2")
    part2()
  }
}
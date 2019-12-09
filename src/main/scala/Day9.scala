class Comp(var mem: Array[Long], var pc: Int = 0, var relativeBase: Int = 0) {
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

  def run(): Unit = {
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
          mem(indexes(0)) = 2
          pc += 2
        }
        case 4 => {
          val indexes = parseArgs(1)
          println(mem(indexes(0)))
          pc += 2
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
  }
}

object Day9 {
  val initMem = scala.io.Source.fromFile("data/data9.txt", "UTF-8").getLines.toVector(0).split(',').map(_.toLong).toVector

  def part1(): Unit = {
    val comp: Comp = new Comp(initMem.toArray ++ Array.fill(10000)(0L))
    comp.run()
  }

  def part2(): Unit = {

  }

  def apply() = {
    println("SOLUTION DAY 9")
    println("Running part 1")
    part1()
    println("Running part 2")
    //part2()
  }
}
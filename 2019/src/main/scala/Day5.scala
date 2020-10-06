object Day5 {
  def part1(): Unit = {
    val mem = scala.io.Source.fromFile("data/data5.txt", "UTF-8").getLines.toVector(0).split(',').map(_.toInt).toArray
    var pc = 0

    def getValues(): (Int,Int) = {
      var isPosModes = mem(pc).toString.substring(0,math.max(mem(pc).toString.length() - 2,0)).map(_ == '0')
      for (i <- 0 until 2 - isPosModes.length) isPosModes = true +: isPosModes
      val i = if (isPosModes(1)) mem(mem(pc + 1)) else mem(pc + 1)
      val j = if (isPosModes(0)) mem(mem(pc + 2)) else mem(pc + 2)
      (i,j)
    }

    while (pc < mem.length && mem(pc) != 99) {
      mem(pc).toString.last.toString.toInt match {
        case 1 => {
          val (i,j) = getValues()
          mem(mem(pc + 3)) = i + j
          pc += 4
        }
        case 2 => {
          val (i,j) = getValues()
          mem(mem(pc + 3)) = i * j
          pc += 4
        }
        case 3 => { mem(mem(pc + 1)) = 5; pc += 2 }
        case 4 => { println(mem(mem(pc + 1))); pc += 2 }
        case 5 => {
          val (i,j) = getValues()
          if (i != 0) pc = j
          else pc += 3
        }
        case 6 => {
          val (i,j) = getValues()
          if (i == 0) pc = j
          else pc += 3
        }
        case 7 => {
          val (i,j) = getValues()
          mem(mem(pc + 3)) = if (i < j) 1 else 0
          pc += 4
        }
        case 8 => {
          val (i,j) = getValues()
          mem(mem(pc + 3)) = if (i == j) 1 else 0
          pc += 4
        }
        case _ => {println("mem(pc) = " + mem(pc) + "\ninstruction = " + mem(pc).toString.last.toInt); throw new Exception()}
      }
    }
    mem(0)
  }

  def part2(): Unit = {

  }

  def apply() = {
    println("SOLUTION DAY 5")
    println("Running part 1")
    println(part1())
    println("Running part 2")
    //part2()
  }
}
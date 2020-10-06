object Day2 {
  val input = scala.io.Source.fromFile("data/data2.txt", "UTF-8").getLines.toVector(0).split(',').map(_.toInt).toVector

  def part1(noun: Int = 12, verb: Int = 2): Int = {
    val mem = input.toArray
    var pc = 0
    mem(1) = noun
    mem(2) = verb
    while (pc < mem.length && mem(pc) != 99) {
      mem(mem(pc + 3)) = mem(pc) match {
        case 1 => mem(mem(pc + 1)) + mem(mem(pc + 2))
        case 2 => mem(mem(pc + 1)) * mem(mem(pc + 2))
        case _ => {println("unknown opcode" + mem(pc)); mem(mem(pc + 3))}
      }
      pc += 4
    }
    mem(0)
  }

  def part2(): Unit = {
    for (i <- 0 to 99; j <- 0 to 99) {
      try {
        if (part1(i,j) == 19690720) println("noun=" + i + " verb=" + j)
      } catch {
        case e: Exception => -1
      }
    }
  }

  def apply() = {
    println("SOLUTION DAY 2")
    println("Running part 1")
    println(part1())
    println("Running part 2")
    part2()
  }
}
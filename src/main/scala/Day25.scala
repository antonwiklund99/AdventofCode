import scala.collection.mutable.Queue

class Comp25(var mem: Array[Long], var pc: Int = 0, var relativeBase: Int = 0) {
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

  var input: Queue[Long] = Queue.empty

  def run(): Long = {
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
          if (input.size == 0) return -1
          mem(indexes(0)) = input.dequeue()
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

object Day25 {
  val initMem = scala.io.Source.fromFile("data/data25.txt", "UTF-8").getLines.toVector(0).split(',').map(_.toLong).toVector

  def part1(): Unit = {
    var commands = Vector("south", "take monolith", "east", "take asterisk", "west", "east", "north",
           "south", "west", "north", "west", "take coin", "south", "north", "north", "east",
           "take astronaut ice cream", "west", "south", "south", "north", "east", "north",
           "east", "west", "north", "take mutex", "east", "west", "west", "take astrolabe",
           "west", "take dehydrated water", "south", "north", "west", "take wreath", "east", "south",
           "east", "north", "north", "inv")
    //var commands: Vector[String] = Vector.empty
    val comp = new Comp25(initMem.toArray ++ Array.fill(10000)(0L))
    //for (c <- "")
    for (cmd <- commands) {
      var str = ""
      while (!str.containsSlice("Command?")) {
        val c = comp.run()
        if (c == -1) throw new Exception("kuken")
        str += c.toChar
      }
      comp.input = Queue.empty
      cmd.foreach(comp.input += _.toLong)
      comp.input += '\n'
    }

    val items = Vector("astronaut ice cream", "wreath", "coin", "dehydrated water", "asterisk", "monolith", "astrolabe", "mutex")
    val itemCombs = items.toSet.subsets.map(_.toVector).toVector
    for (comb <- itemCombs) {
      commands = items.map("drop " + _ + "\n") ++ comb.map("take " + _ + "\n").appended("north\n")
      for (cmd <- commands) {
        var str = ""
        while (!str.containsSlice("Command?")) {
          val c = comp.run()
          if (c == -1) {
            println(str)
            return
          }
          str += c.toChar
        }
        comp.input = Queue.empty
        cmd.foreach(comp.input += _.toLong)
        comp.input += '\n'
      }
      println(comb)
    }

    /*var cmd = ""
    while (cmd != "quit") {
      var c = ' '
      while (c != '?' && c != -1.toChar) {
        c = comp.run().toChar
        print(c)
      }
      cmd = scala.io.StdIn.readLine("\n")
      commands = commands.appended(cmd)
      comp.input = Queue.empty
      cmd.foreach(comp.input += _.toLong)
      comp.input += '\n'
    }
    println(commands)
    */
  }

  def part2(): Unit = {

  }

  def apply() = {
    println("SOLUTION DAY 25")
    println("Running part 1")
    part1()
    println("Running part 2")
    //part2()
  }
}
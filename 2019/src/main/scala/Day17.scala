import scala.collection.mutable.ArrayBuffer

class Comp17(var mem: Array[Long], var pc: Int = 0, var relativeBase: Int = 0) {
  var lastOutput = 0L
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

  override def clone(): Comp13 = new Comp13(mem.clone(), pc, relativeBase)

  def run(input: scala.collection.mutable.Queue[Long]): Long = {
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
          mem(indexes(0)) = input.dequeue
          pc += 2
        }
        case 4 => {
          val indexes = parseArgs(1)
          pc += 2
          lastOutput = mem(indexes(0))
          print(mem(indexes(0)).toChar)
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

object Day17 {
  val initMem = scala.io.Source.fromFile("data/data17.txt", "UTF-8").getLines.toVector(0).split(',').map(_.toLong).toVector

  def part1(): Unit = {
    val comp = new Comp13(initMem.toArray ++ Array.fill(10000)(0L))
    var res = 'i'
    var grid: ArrayBuffer[ArrayBuffer[Char]] = ArrayBuffer(ArrayBuffer.empty)
    var  i = 0
    while (res != -1.toChar) {
      res = comp.run(0L).toChar
      print(res)
      if (res == '\n') {i += 1; grid += ArrayBuffer.empty}
      else grid(i) += res
    }
    var count = 0
    grid = grid.slice(0, grid.length - 2)
    for (i <- grid.indices) {
      for (j <- grid(0).indices) {
        if (grid(i)(j) == '#' && (i - 1 > -1 && grid(i-1)(j) == '#') &&
           (i + 1 < grid.length && grid(i+1)(j) == '#') && (j - 1 > -1 && grid(i)(j - 1) == '#') &&
           (j + 1 < grid(0).length && grid(i)(j+1) == '#')) count += i*j
      }
    }
    println(count)
  }

  def part2(): Unit = {
    
    val comp1 = new Comp13(initMem.toArray ++ Array.fill(10000)(0L))
    var res = 'i'
    var grid: ArrayBuffer[ArrayBuffer[Char]] = ArrayBuffer(ArrayBuffer.empty)
    var  i = 0
    while (res != -1.toChar) {
      res = comp1.run(0L).toChar
      if (res == '\n') {i += 1; grid += ArrayBuffer.empty}
      else grid(i) += res
    }
    var positions: Vector[Pos] = Vector.empty
    grid = grid.slice(0, grid.length - 2)
    for (i <- grid.indices) {
      for (j <- grid(0).indices) {
        if (grid(i)(j) == '#') positions :+= Pos(j,i)
      }
    }
    grid = grid.slice(0, grid.length - 2)
    var pos = Pos(42,14)
    var dir = Pos(0,-1)
    var found = false
    var instructions: ArrayBuffer[String] = ArrayBuffer.empty
    var forwardInRow = 0
    while (!found) {
      if (positions.contains(pos + dir)) {
        pos = pos + dir
        forwardInRow += 1
      }
      else if (dir != Pos(-1,0) && positions.contains(pos + Pos(1,0))) {
        if (forwardInRow > 0) { instructions += forwardInRow.toString; forwardInRow = 0 }
        instructions += {dir match {
          case Pos(0,-1) => "R"
          case Pos(0,1)  => "L"
          case e => {throw new Exception("OHNO" + e); "0"}
        }}
        dir = Pos(1,0)
      }
      else if (dir != Pos(1,0) && positions.contains(pos + Pos(-1,0))) {
        if (forwardInRow > 0) { instructions += forwardInRow.toString; forwardInRow = 0 }
        instructions += {dir match {
          case Pos(0,-1) => "L"
          case Pos(0,1)  => "R"
          case e => {throw new Exception("OHNO" + e); "0"}
        }}
        dir = Pos(-1,0)
      }
      else if (dir != Pos(0,-1) && positions.contains(pos + Pos(0,1))) {
        if (forwardInRow > 0) { instructions += forwardInRow.toString; forwardInRow = 0 }
        instructions += {dir match {
          case Pos(1,0)   => "R"
          case Pos(-1,0)  => "L"
          case e => {throw new Exception("OHNO" + e); "0"}
        }}
        dir = Pos(0,1)
      }
      else if (dir != Pos(0,1) && positions.contains(pos + Pos(0,-1))) {
        if (forwardInRow > 0) { instructions += forwardInRow.toString; forwardInRow = 0 }
        instructions += {dir match {
          case Pos(1,0)   => "L"
          case Pos(-1,0)  => "R"
          case e => {throw new Exception("OHNO" + e); "0"}
        }}
        dir = Pos(0,-1)
      }
      else { found = true; if (forwardInRow > 0) instructions += forwardInRow.toString}
    }
    
    val comp = new Comp17(initMem.toArray ++ Array.fill(10000)(0L))
    comp.mem(0) = 2
    
    val combs = (3 to 10).map(instructions.sliding(_).toVector).flatten.combinations(3)
    var instructionString = instructions.mkString
    println("Starting find")
    val funcs = combs.find(p => instructionString.replaceAll(p(0).mkString, "").replaceAll(p(1).mkString,"").replaceAll(p(2).mkString, "") == "").get
    var mainRoutine = ""
    println(funcs)
    println("Starting search for main routine")
    val aa = funcs(0).mkString
    val bb = funcs(1).mkString
    val cc = funcs(2).mkString
    while (instructionString != "") {
      if (instructionString.indexOf(aa) == 0) {
        println("A")
        mainRoutine += "A"
        instructionString = instructionString.slice(aa.length, instructionString.length())
      }
      if (instructionString.indexOf(bb) == 0) {
        println("B")
        mainRoutine += "B"
        instructionString = instructionString.slice(bb.length, instructionString.length())
      }
      if (instructionString.indexOf(cc) == 0) {
        println("C")
        mainRoutine += "C"
        instructionString = instructionString.slice(cc.length, instructionString.length())
      }
    }
    val a = funcs(0).mkString("", ",", "\n")
    val b = funcs(1).mkString("", ",", "\n")
    val c = funcs(2).mkString("", ",", "\n")
    val video = "n\n"


    val in: scala.collection.mutable.Queue[Long] = scala.collection.mutable.Queue.empty
    (mainRoutine.split("").mkString("", ",", "\n") + a + b + c + video).toCharArray.foreach(in += _.toLong)
    comp.run(in)
    println(comp.lastOutput)
  }

  def apply() = {
    println("SOLUTION DAY 17")
    println("Running part 1")
    part1()
    println("Running part 2")
    part2()
  }
}
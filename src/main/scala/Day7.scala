class Amplifier(val mem: Array[Int], var pc: Int)

object Day7 {
  val initMem = scala.io.Source.fromFile("data/data7.txt", "UTF-8").getLines.toVector(0).split(',').map(_.toInt).toVector

  def part1(): Unit = {
    def run(a: Amplifier, amp: Int, input:Int): Int = {
      def getValues(): (Int,Int) = {
        var isPosModes = a.mem(a.pc).toString.substring(0,math.max(a.mem(a.pc).toString.length() - 2,0)).map(_ == '0')
        for (i <- 0 until 2 - isPosModes.length) isPosModes = true +: isPosModes
        val i = if (isPosModes(1)) a.mem(a.mem(a.pc + 1)) else a.mem(a.pc + 1)
        val j = if (isPosModes(0)) a.mem(a.mem(a.pc + 2)) else a.mem(a.pc + 2)
        (i,j)
      }
    
      var l = amp
      while (a.pc < a.mem.length && a.mem(a.pc) != 99) {
        a.mem(a.pc).toString.last.toString.toInt match {
          case 1 => {
            val (i,j) = getValues()
            a.mem(a.mem(a.pc + 3)) = i + j
            a.pc += 4
          }
          case 2 => {
              val (i,j) = getValues()
              a.mem(a.mem(a.pc + 3)) = i * j
              a.pc += 4
          }
          case 3 => {a.mem(a.mem(a.pc + 1)) = l; l = input; a.pc += 2}
          case 4 => {var res = a.mem(a.mem(a.pc + 1)); a.pc += 2; return res}
          case 5 => {
            val (i,j) = getValues()
            if (i != 0) a.pc = j
            else a.pc += 3
          }
          case 6 => {
            val (i,j) = getValues()
            if (i == 0) a.pc = j
            else a.pc += 3
          }
          case 7 => {
            val (i,j) = getValues()
            a.mem(a.mem(a.pc + 3)) = if (i < j) 1 else  0
            a.pc += 4
          }
          case 8 => {
            val (i,j) = getValues()
            a.mem(a.mem(a.pc + 3)) = if (i == j) 1 else  0
            a.pc += 4
          }
          case _ => {println("mem(pc) = " + a.mem(a.pc)); println("instruction = " + a.mem(a.pc).toString.last.toInt); throw new Exception()}
        }
      }
      -1
    }

    var res = 0
    for (one <- 5 to 9;two <- 5 to 9; three <- 5 to 9; four <- 5 to 9; five <- 5 to 9) {
      val xs = Vector(one,two,three,four,five)
      if (xs == xs.distinct) {
        var amplifiers: Array[Amplifier] = Array(
          new Amplifier(initMem.toArray, 0),
          new Amplifier(initMem.toArray, 0),
          new Amplifier(initMem.toArray, 0),
          new Amplifier(initMem.toArray, 0),
          new Amplifier(initMem.toArray, 0)
        )
        var i = 0
        var lastE = 0
        var first  = true
        var x = 0
        while (x != -1) {
          x = run(amplifiers(i), if (first) xs(i) else x, x)
          i += 1
          if (i == xs.length) {lastE = x; i = 0; first = false}
        }
        
        if (lastE > res) res = lastE
      }
    }
    println(res)
  }

  def part2(): Unit = {

  }

  def apply() = {
    println("SOLUTION DAY 7")
    println("Running part 1")
    part1()
    println("Running part 2")
    //part2()
  }
}
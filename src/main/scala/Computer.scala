class Computer(val mem: Array[Int], var pc: Int = 0) {
    var newOutput: Boolean = false
    var output: Int = 0
    var halt: Boolean = false
    var inputs: scala.collection.mutable.Queue[Int] = scala.collection.mutable.Queue.empty

    def step(): Unit = {
        def getValues(): (Int,Int) = {
          var isPosModes = mem(pc).toString.substring(0,math.max(mem(pc).toString.length() - 2,0)).map(_ == '0')
          for (i <- 0 until 2 - isPosModes.length) isPosModes = true +: isPosModes
          val i = if (isPosModes(1)) mem(mem(pc + 1)) else mem(pc + 1)
          val j = if (isPosModes(0)) mem(mem(pc + 2)) else mem(pc + 2)
          (i,j)
        }

        newOutput = false
        mem(pc).toString.takeRight(2).toInt match {
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
            case 3 => {
                mem(mem(pc + 1)) = inputs.dequeue
                pc += 2
            }
            case 4 => {
                output = mem(mem(pc + 1))
                newOutput = true
                pc += 2
            }
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
                mem(mem(pc + 3)) = if (i < j) 1 else  0
                pc += 4
            }
            case 8 => {
                val (i,j) = getValues()
                mem(mem(pc + 3)) = if (i == j) 1 else  0
                pc += 4
            }
            case 99 => halt = true
            case _ => {println("mem(pc) = " + mem(pc)); println("instruction = " + mem(pc).toString.last.toInt); throw new Exception()}
        }
    }
}
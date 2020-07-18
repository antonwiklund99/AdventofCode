import scala.collection.mutable.Queue
import scala.collection.mutable.ArrayBuffer

case class Package(address: Int, x: Long, y: Long)

class NetworkComp(var mem: Array[Long], var pc: Int = 0, var relativeBase: Int = 0, val outputQueue: Queue[Package]) {
  var lastOutput = 0L
  val input: Queue[Long] = Queue.empty
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

  def run() = {
    var outputBuff: ArrayBuffer[Long] = ArrayBuffer.empty
    while (pc < mem.length && mem(pc) != 99) {
      println("pc = " + pc + " mem(pc) = " + mem(pc) + " mem(pc+1) = " + mem(pc+1) + " mem(pc+2) = " + mem(pc+2)+ " relativeBase = " + relativeBase + " mem(62) = " + mem(62))
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
          mem(indexes(0)) = if (input.isEmpty) -1 else input.dequeue
          pc += 2
        }
        case 4 => {
          val indexes = parseArgs(1)
          pc += 2
          outputBuff += mem(indexes(0))
          if (outputBuff.length == 3) {
            outputQueue += Package(outputBuff(0).toInt, outputBuff(1), outputBuff(2))
            outputBuff = ArrayBuffer.empty
          }
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

class Network(outputQueue: Queue[Package], networks: Array[NetworkComp]) extends Thread {
  override def run() = {
    while (outputQueue.find(p => p.address == 255) == None) {
      if (!outputQueue.isEmpty) {
        val p = outputQueue.dequeue()
        println("FOUND OUTPUT " + p)
        networks(p.address).input ++ Vector(p.x,p.y)
      }
    }
    println(outputQueue.find(p => p.address == 255).get)
    System.exit(0)
  }
}

object Day23 {
  val initMem = scala.io.Source.fromFile("data/data23.txt", "UTF-8").getLines.toVector(0).split(',').map(_.toLong).toVector

  def part1(): Unit = {
    val outputQueue: Queue[Package] = Queue.empty
    val networks: Array[NetworkComp] = Array.fill(50)(new NetworkComp(initMem.toArray ++ Array.fill(10000)(0L), 0, 0,outputQueue))
    val n = new Network(outputQueue, networks)
    /*for (i <- networks) i.start()
    n.start()*/
    networks(0).run()
  }

  def part2(): Unit = {

  }

  def apply() = {
    println("SOLUTION DAY 23")
    println("Running part 1")
    part1()
    println("Running part 2")
    //part2()
  }
}
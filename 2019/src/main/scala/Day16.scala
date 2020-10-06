import scala.collection.mutable.ArrayBuffer

object Day16 {
  val input = scala.io.Source.fromFile("data/data16.txt", "UTF-8").getLines.toVector(0).split("").map(_.toString.toInt).toVector

  def part1(): Unit = {
    var res = input
    val basePattern = Vector(0,1,0,-1)
    for (k <- 0 until 100) {
      var newRes: Vector[Int] = Vector.empty
      for (i <- res.indices) {
        val matchingPattern: ArrayBuffer[Int] = ArrayBuffer.empty
        while (matchingPattern.length <= res.length) {
          basePattern.foreach(matchingPattern ++= ArrayBuffer.fill(1 + i)(_))
        }
        newRes :+= math.abs(res.indices.map(j => matchingPattern(j + 1)*res(j)).sum) % 10
      }
      res = newRes
    }
    println(res.take(8))
  }

  def part2(): Unit = {
    var res = Vector.fill(10000)(input).flatten
    val messageOffset = input.take(7).mkString.toInt
    res = res.slice(messageOffset, res.length)
    for (k <- 1 to 100) {
      var newRes: Vector[Int] = Vector.empty
      var sum = 0
      for (i <- res.length - 1 to 0 by -1) {
        sum += res(i)
        newRes = math.abs(sum % 10) +: newRes
      }
      res = newRes
      println(k + "/100")
    }
    println(res.take(8).mkString)
  }

  def apply() = {
    println("SOLUTION DAY 16")
    println("Running part 1")
    //part1()
    println("Running part 2")
    part2()
  }
}
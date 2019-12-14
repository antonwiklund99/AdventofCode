case class Conversion(from: Vector[String], costFrom: Vector[Long], to: String, costTo: Long) {
  def getElementsForAmount(n: Long): Vector[(String,Long)] = {
    val nbrsOfTimeTaken = math.ceil(n.toDouble/costTo.toDouble).toLong
    from.zip(costFrom).map(p => (p._1, p._2*nbrsOfTimeTaken))
  }

  def excessForAmount(n: Long): Long = math.ceil(n.toDouble/costTo.toDouble).toLong*costTo - n
  def fromOre(): Boolean = (from.contains("ORE") && from.length == 1)
}

object Day14 {
  var input = scala.io.Source.fromFile("data/data14.txt", "UTF-8").getLines.toVector
  val costMap = parseInput()

  def parseInput(): scala.collection.mutable.Map[String, Conversion] = {
    val parsed = input.map(_.split("=>").map(s => s.split(" ").map(p => p.replaceAll(",","")).filter(p => !p.isEmpty()).toVector).toVector)
    val m: scala.collection.mutable.Map[String, Conversion] = scala.collection.mutable.Map.empty
    parsed.foreach(s => {
      var from: Vector[String] = Vector.empty
      var fromCost: Vector[Long] = Vector.empty
      s(0).grouped(2).foreach(f => {from :+= f(1);fromCost :+= f(0).toLong})
      m(s(1)(1)) = Conversion(from, fromCost, s(1)(1), s(1)(0).toLong)
    })
    m
  }
  
  def calcOre(k: Long = 1L): Long = {
    var oreMap: scala.collection.mutable.Map[String, Long] = scala.collection.mutable.Map().withDefaultValue(0)

    def rec(amount: Long, s: String): Long = {
      var compensatedAmount = amount
      while(costMap(s).excessForAmount(compensatedAmount) != 0 && oreMap(s) != 0) {
        compensatedAmount -= 1
        oreMap(s) -= 1
      }
      val elem = costMap(s).getElementsForAmount(compensatedAmount)
      oreMap(s) += costMap(s).excessForAmount(compensatedAmount)
      if (costMap(s).fromOre) elem(0)._2
      else elem.map(i => rec(i._2, i._1)).sum
    }
    
    rec(k,"FUEL")
  }
  
  def part1(): Unit = {
    println(calcOre(1L))
  }
  def part2(): Unit = {
    var upper = Long.MaxValue
    var lower = 0L
    while (lower <= upper) {
      var res = math.abs(calcOre((upper + lower)/2))
      if (res > 1000000000000L) upper = (upper + lower)/2 - 1
      else if (res < 1000000000000L) lower = (upper + lower)/2 + 1
    }
    println((upper + lower)/2)
  }

  def apply() = {
    println("SOLUTION DAY 14")
    println("Running part 1")
    part1()
    println("Running part 2")
    part2()
  }
}
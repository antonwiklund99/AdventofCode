object Day1 {
  val input = scala.io.Source.fromFile("data/data1.txt", "UTF-8").getLines.toVector

  def part1(): Unit = println("Answer:" + input.map(_.toInt/3 - 2).sum)

  def part2(): Unit = {
    var sum = 0
    def rec(m: Int): Unit = if (m > 0) {sum += m; rec(m/3 - 2)}

    input.foreach(m => rec(m.toInt/3 - 2))
    println("Answer:" + sum)
  }

  def apply() = {
    println("SOLUTION DAY 1")
    println("Running part 1")
    part1()
    println("Running part 2")
    part2()
  }
}

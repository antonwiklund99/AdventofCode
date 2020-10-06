object Day8 {
  var layers = scala.io.Source.fromFile("data/data8.txt", "UTF-8").getLines.toVector(0).toString.split("").map(_.toInt).grouped(25*6).toVector

  def part1(): Unit = {
    var res: Vector[scala.collection.mutable.Map[Int,Int]] = Vector.empty
    for (layer <- layers) {
      val freq: scala.collection.mutable.Map[Int,Int] = scala.collection.mutable.Map.empty
      for (j <- 0 until 25*6) {
        if (freq.contains(layer(j))) freq(layer(j)) += 1
        else freq(layer(j)) = 1
      }
      res = res :+ freq
    }
    res = res.sortBy(f => f(0))
    println(res(0)(1)*res(0)(2))
  }

  def part2(): Unit = {
    var res: Vector[String] = Vector.empty
    for (i <- 0 until 25*6) {
      var n = 0
      var done = false
      if (i % 25 == 0 && i != 0) res = res :+ "\n"
      while (!done) {
        if (layers(n)(i) != 2) {res = res :+ {if (layers(n)(i) == 1) "#" else " "};done=true}
        else n += 1
      }
    }
    println(res.mkString(""))
  }

  def apply() = {
    println("SOLUTION DAY 8")
    println("Running part 1")
    part1()
    println("Running part 2")
    part2()
  }
}
object Day4 {
  def part1(): Unit = {
    var res = 0
    for (i <- 168630 to 718098) {
      var low = 0
      var j = 0
      var in = i.toString.split("")
      var fail = false
      while (!fail && j < in.length) {
        val x = in(j).toInt
        if (x < low) {
          fail = true
        }
        low = x
        j += 1
      }
      if (!fail && in.distinct.length != in.length) res += 1
    }
    println(res)
  }

  def part2(): Unit = {
    var res = 0
    for (i <- 168630 to 718098) {
      var low = 0
      var j = 0
      var in = i.toString.split("").map(_.toInt)
      var fail = false
      var oneDouble = false
      while (!fail && j < in.length) {
        val x = in(j)
        if (x < low) {
          fail = true
        }
        else if (!oneDouble && j + 1 < in.length && x == in(j + 1) && in.count(_ == x) == 2) oneDouble = true
        low = x
        j += 1
      }
      if (!fail && oneDouble) res += 1
    }
    println(res)
  }

  def apply() = {
    println("SOLUTION DAY 4")
    println("Running part 1")
    //part1()
    println("Running part 2")
    part2()
  }
}
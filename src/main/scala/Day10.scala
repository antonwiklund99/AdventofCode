object Day10 {
  val input = scala.io.Source.fromFile("data/data10.txt", "UTF-8").getLines.toVector

  def part1(): Unit = {
    val grid = input.map(_.split(""))
    var positions: Vector[(Int,Int)] = Vector.empty
    for (i <- grid.indices) {
      for (j <- grid(0).indices) {
        if (grid(i)(j) == "#") positions :+= (j,i)
      }
    }
    var res = 0
    //for (pos <- positions) {
      val pos = (0,1)
      var sum = 0
      //println("\npos = " + pos)
      for (p <- positions) {
        if (p != pos) {
          val diff = (p._1 - pos._1, p._2 - pos._2)
          println("pos = " + pos + " p = " + p + " diff = " + diff)
          if ((diff._1 != 0 && diff._2 != 0) &&
              ((math.abs(diff._1) % math.abs(diff._2) != 0) &&
              (math.abs(diff._2) % math.abs(diff._1) != 0)))
               sum += 1
          else {
            var incrY = 0
            var incrX = 0
            if (math.abs(diff._1) > math.abs(diff._2)) {
              incrX = {
                if (diff._1 != 0 && diff._2 != 0) diff._1 / math.abs(diff._2)
                else if (diff._1 > -1) 1
                else -1
              }
              incrY = {
                if (diff._2 < 0) -1
                else if (diff._2 == 0) 0
                else 1
              }
            }
            else {
              incrY = {
                if (diff._1 != 0 && diff._2 != 0) diff._2 / math.abs(diff._1)
                else if (diff._2 > -1) 1
                else -1
              }
              incrX = {
                if (diff._1 == 0) 0
                else if (diff._1 < 0) -1
                else 1
              }
            }
            var head = (pos._1 + incrX, pos._2 + incrY)
            var fail = false
            println("incrX = " + incrX + " incrY = " + incrY)
            while (head != p && !fail) {
              if (grid(head._2)(head._1) == "#") fail = true
              else head = (head._1 + incrX, head._2 + incrY)
            }
            if (!fail) sum += 1
          }
          println("sum = " + sum)
        }
      //}
      //println("pos = " + pos + " sum = " + sum)
      res = math.max(res,sum)
    }
    println(res)
  }

  def part2(): Unit = {

  }

  def apply() = {
    println("SOLUTION DAY 10")
    println("Running part 1")
    part1()
    println("Running part 2")
    //part2()
  }
}
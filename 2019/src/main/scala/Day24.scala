import scala.collection.mutable.Set
import scala.collection.mutable.Map
import scala.collection.mutable.ArrayBuffer

object Day24 {
  val input = scala.io.Source.fromFile("data/data24.txt", "UTF-8").getLines.toVector

  def part1(): Unit = {
    def countNeighbours(xs: Array[Array[String]], x: Int, y: Int): Int = {
      var count = 0
      if (y > 0 && xs(y - 1)(x) == "#") count += 1
      if (x > 0 && xs(y)(x - 1) == "#") count += 1
      if (x + 1 < xs(0).length && xs(y)(x + 1) == "#") count += 1
      if (y + 1 < xs.length && xs(y + 1)(x) == "#") count += 1
      count
    }

    var state = input.map(_.split("")).toArray
    for (x <- state) println(x.mkString(""))
    println("")
    // each state can be represented by a 25 bit binary number where pos 0 in layout is bit 0 in the number
    val prevStates: Set[Int] = Set.empty
    var n: Int = 0
    var done = false
    while (!done) {
      n = 0
      val newState = state.map(_.clone()).toArray
      for (y <- state.indices; x <- state(y).indices) {
        if (state(y)(x) == "#") n += (1 << (x + 5*y))

        val nbrs = countNeighbours(state, x, y)
        if (state(y)(x) == "#" && nbrs != 1) newState(y)(x) = "."
        else if (state(y)(x) == "." && (nbrs == 1 || nbrs == 2)) newState(y)(x) = "#"
      }
      state = newState
      if (prevStates(n)) done = true
      prevStates += n
    }
    println(n)
  }

  def part2(): Unit = {
    // Map for each levels tiles
    var levels: Map[Int, Array[Array[String]]] = Map((0, input.map(_.split("")).toArray))
    val emptyLevel = Array(
      Array(".", ".", ".", ".", "."),
      Array(".", ".", ".", ".", "."),
      Array(".", ".", ".", ".", "."),
      Array(".", ".", ".", ".", "."),
      Array(".", ".", ".", ".", ".")
    )
    def countNeighbours(level: Int, x: Int, y: Int): Int = {
      //println("level = " + level + " x = " + x + " y = " + y)
      val xs = levels(level)
      var count = 0
      if (y > 0 && xs(y - 1)(x) == "#") count += 1
      else if (y - 1 < 0 && levels.contains(level - 1) && levels(level - 1)(1)(2) == "#") count += 1
      
      if (x > 0 && xs(y)(x - 1) == "#") count += 1
      else if (x - 1 < 0 && levels.contains(level - 1) && levels(level - 1)(2)(1) == "#") count += 1
      
      if (x + 1 < xs(0).length && xs(y)(x + 1) == "#") count += 1
      else if (x + 1 >= xs(0).length && levels.contains(level - 1) && levels(level - 1)(2)(3) == "#") count += 1

      if (y + 1 < xs.length && xs(y + 1)(x) == "#") count += 1
      else if (y + 1 >= xs.length && levels.contains(level - 1) && levels(level - 1)(3)(2) == "#") count += 1

      if (levels.contains(level + 1)) {
        val below = levels(level + 1) // below = inside tile (2,2)
        if (x == 2 && y == 1) {
          count += below(0).count(_ == "#")
        } else if (x == 2 && y == 3) {
          count += below.last.count(_ == "#")
        } else if (x == 1 && y == 2) {
          count += below.map(_(0)).count(_ == "#")
        } else if (x == 3 && y == 2) {
          count += below.map(_.last).count(_ == "#")
        }
      }
      //println("count = " + count)
      count
    }

    // extremely inefficient but it works
    for (i <- (0 to 199)) {
      val newLevels: Map[Int, Array[Array[String]]] = Map.empty
      val edgeLow: Int = levels.minBy(_._1)._1 - 1
      val edgeHigh: Int = levels.maxBy(_._1)._1 + 1
      levels(edgeHigh) = emptyLevel.map(_.clone())
      levels(edgeLow) = emptyLevel.map(_.clone())
      levels.foreach(level => {
        val state = level._2
        val newState = state.map(_.clone()).toArray
        
        for (y <- state.indices; x <- state(y).indices; if (!(x == 2 && y == 2))) {
          val nbrs = countNeighbours(level._1, x, y)
          if (state(y)(x) == "#" && nbrs != 1) newState(y)(x) = "."
          else if (state(y)(x) == "." && (nbrs == 1 || nbrs == 2)) newState(y)(x) = "#"
        }
        newLevels(level._1) = newState
      })
      if (!newLevels(edgeLow).exists(_.contains("#"))) newLevels.remove(edgeLow)
      if (!newLevels(edgeHigh).exists(_.contains("#"))) newLevels.remove(edgeHigh)
      levels = newLevels
    }
    println(levels.map(x => x._2.map(f => f.count(p => p == "#"))).flatten.sum)
  }

  def apply() = {
    println("SOLUTION DAY 24")
    println("Running part 1")
    //part1()
    println("Running part 2")
    part2()
  }
}
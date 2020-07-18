object Day24 {
  val input = scala.io.Source.fromFile("data/data24.txt", "UTF-8").getLines.toVector

  def getNbrOfNeighbours(xs: Array[Array[String]], x: Int, y: Int): Int = {
    var count = 0
    for (i <- math.max(y - 1,0) to math.min(y + 1, xs.length - 1)) {
      for (j <- math.max(x - 1,0) to math.min(x + 1, xs(i).length - 1); if (i == y || j == x)) {
        if (xs(i)(j) == "#") count += 1
      }
    }
    count
  }

  def part1(): Unit = {
    var state = input.map(_.split("")).toArray
    var prevStates: Vector[String] = Vector.empty
    while (!prevStates.exists(p => p.equals(state))) {
      println(state.map(_.mkString).mkString("\n"))
      var newState = state.clone
      prevStates :+= state.map(_.mkString("")).mkString("")
      for (i <- state.indices; j <- state(i).indices) {
        val n = getNbrOfNeighbours(state,j,i)
        if (state(i)(j) == "#" && n != 1) newState(i)(j) = "."
        else if (state(i)(j) == "." && (n == 1 || n == 2)) newState(i)(j) = "#"
      }
      state = newState
      scala.io.StdIn.readLine("prompt")
    }
  }

  def part2(): Unit = {

  }

  def apply() = {
    println("SOLUTION DAY 24")
    println("Running part 1")
    part1()
    println("Running part 2")
    //part2()
  }
}
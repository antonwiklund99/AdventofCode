import scala.collection.mutable.ArrayBuffer

object Day17 {
  val initMem = scala.io.Source.fromFile("data/data17.txt", "UTF-8").getLines.toVector(0).split(',').map(_.toLong).toVector

  def part1(): Unit = {
    val comp = new Comp13(initMem.toArray ++ Array.fill(10000)(0L))
    var res = 'i'
    var grid: ArrayBuffer[ArrayBuffer[Char]] = ArrayBuffer(ArrayBuffer.empty)
    var  i = 0
    while (res != -1.toChar) {
      res = comp.run(0L).toChar
      print(res)
      if (res == '\n') {i += 1; grid += ArrayBuffer.empty}
      else grid(i) += res
    }
    var count = 0
    grid = grid.slice(0, grid.length - 2)
    println(grid)
    for (i <- grid.indices) {
      for (j <- grid(0).indices) {
        if (grid(i)(j) == '#' && (i - 1 > -1 && grid(i-1)(j) == '#') &&
           (i + 1 < grid.length && grid(i+1)(j) == '#') && (j - 1 > -1 && grid(i)(j - 1) == '#') &&
           (j + 1 < grid(0).length && grid(i)(j+1) == '#')) count += i*j
      }
    }
    println(count)
  }

  def part2(): Unit = {

  }

  def apply() = {
    println("SOLUTION DAY 17")
    println("Running part 1")
    part1()
    println("Running part 2")
    //part2()
  }
}
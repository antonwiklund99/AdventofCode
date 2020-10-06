object Day21 {
  val initMem = scala.io.Source.fromFile("data/data21.txt", "UTF-8").getLines.toVector(0).split(',').map(_.toLong).toVector

  def part1(): Unit = {
    val comp = new Comp17(initMem.toArray ++ Array.fill(10000)(0L))
    // J = !(A & B & C) & D
    val program: Vector[String] = Vector(
    "OR A T",
    "AND B T",
    "AND C T",
    "NOT T J",
    "AND D J",
    "WALK"
    )
    val in: scala.collection.mutable.Queue[Long] = scala.collection.mutable.Queue.empty
    (program.mkString("", "\n", "\n")).toCharArray.foreach(in += _.toLong)
    comp.run(in)
    println(comp.lastOutput)
  }

  def part2(): Unit = {
    val comp = new Comp17(initMem.toArray ++ Array.fill(10000)(0L))
    // J = (!(A && B & C) && D) && (H || E)
    val program: Vector[String] = Vector(
    "OR A T",
    "AND B T",
    "AND C T",
    "NOT T J",
    "AND D J",
    "OR H T",
    "OR E T",
    "AND T J",
    "RUN"
    )
    val in: scala.collection.mutable.Queue[Long] = scala.collection.mutable.Queue.empty
    (program.mkString("", "\n", "\n")).toCharArray.foreach(in += _.toLong)
    comp.run(in)
    println(comp.lastOutput)
  }

  def apply() = {
    println("SOLUTION DAY 21")
    println("Running part 1")
    //part1()
    println("Running part 2")
    part2()
  }
}
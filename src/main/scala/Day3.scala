object Day3 {
  val input = scala.io.Source.fromFile("data/data3.txt", "UTF-8").getLines.toVector

  def part1(): Unit = {
    var firstPos: Vector[(Int,Int)] = Vector.empty
    var secondPos: Vector[(Int,Int)] = Vector.empty
    var currentPos = (0,0)
    for (p <- input(0).split(',')) {
      p.head match {
        case 'R' => for(i <- 1 to p.tail.toInt) firstPos = firstPos :+ (currentPos._1 + i, currentPos._2)
        case 'L' => for(i <- 1 to p.tail.toInt) firstPos = firstPos :+ (currentPos._1 - i, currentPos._2)
        case 'U' => for(i <- 1 to p.tail.toInt) firstPos = firstPos :+ (currentPos._1, currentPos._2 + i)
        case 'D' => for(i <- 1 to p.tail.toInt) firstPos = firstPos :+ (currentPos._1, currentPos._2 - i)
        case i => println("ohno")
      }
      currentPos = firstPos.last
    }
    currentPos = (0,0)
    for (p <- input(1).split(',')) {
      p.head match {
        case 'R' => for(i <- 1 to p.tail.toInt) secondPos = secondPos :+ (currentPos._1 + i, currentPos._2)
        case 'L' => for(i <- 1 to p.tail.toInt) secondPos = secondPos :+ (currentPos._1 - i, currentPos._2)
        case 'U' => for(i <- 1 to p.tail.toInt) secondPos = secondPos :+ (currentPos._1, currentPos._2 + i)
        case 'D' => for(i <- 1 to p.tail.toInt) secondPos = secondPos :+ (currentPos._1 , currentPos._2- i)
        case i => println("ohno")
      }
      currentPos = secondPos.last
    }
    val intersec = firstPos.intersect(secondPos).sortBy(x => math.abs(x._1) + math.abs(x._2))
    println(math.abs(intersec(0)._1) + math.abs(intersec(0)._2))
  }

  def part2(): Unit = {
    var firstPos: Vector[(Int,Int)] = Vector.empty
    var firstStep: scala.collection.mutable.Map[(Int,Int), Int] = scala.collection.mutable.Map.empty
    var secondPos: Vector[(Int,Int)] = Vector.empty
    var secondStep: scala.collection.mutable.Map[(Int,Int), Int] = scala.collection.mutable.Map.empty
    var currentPos = (0,0)
    var steps = 0
    for (p <- input(0).split(',')) {
      p.head match {
        case 'R' => for(i <- 1 to p.tail.toInt) {
          steps += 1
          firstPos = firstPos :+ (currentPos._1 + i, currentPos._2)
          if (!firstStep.contains(firstPos.last)) firstStep(firstPos.last) = steps
        }
        case 'L' => for(i <- 1 to p.tail.toInt) {
          steps += 1
          firstPos = firstPos :+ (currentPos._1 - i, currentPos._2)
          if (!firstStep.contains(firstPos.last)) firstStep(firstPos.last) = steps
        }
        case 'U' => for(i <- 1 to p.tail.toInt) {
          steps += 1
          firstPos = firstPos :+ (currentPos._1, currentPos._2 + i)
          if (!firstStep.contains(firstPos.last)) firstStep(firstPos.last) = steps
        }
        case 'D' => for(i <- 1 to p.tail.toInt) {
          steps += 1
          firstPos = firstPos :+ (currentPos._1, currentPos._2 - i)
          if (!firstStep.contains(firstPos.last)) firstStep(firstPos.last) = steps
        }
        case b => println("ohno")
      }
      currentPos = firstPos.last
    }
    currentPos = (0,0)
    steps = 0
    for (p <- input(1).split(',')) {
      p.head match {
        case 'R' => for(i <- 1 to p.tail.toInt) {
          steps += 1
          secondPos = secondPos :+ (currentPos._1 + i, currentPos._2)
          if (!secondStep.contains(secondPos.last)) secondStep(secondPos.last) = steps
        }
        case 'L' => for(i <- 1 to p.tail.toInt) {
          steps += 1
          secondPos = secondPos :+ (currentPos._1 - i, currentPos._2)
          if (!secondStep.contains(secondPos.last)) secondStep(secondPos.last) = steps
        }
        case 'U' => for(i <- 1 to p.tail.toInt) {
          steps += 1
          secondPos = secondPos :+ (currentPos._1, currentPos._2 + i)
          if (!secondStep.contains(secondPos.last)) secondStep(secondPos.last) = steps
        }
        case 'D' => for(i <- 1 to p.tail.toInt) {
          steps += 1
          secondPos = secondPos :+ (currentPos._1 , currentPos._2- i)
          if (!secondStep.contains(secondPos.last)) secondStep(secondPos.last) = steps
        }
        case i => println("ohno")
      }
      currentPos = secondPos.last
    }
    val intersec = firstPos.intersect(secondPos).map(p => firstStep(p) + secondStep(p)).sorted
    println(intersec(0))
  }

  def apply() = {
    println("SOLUTION DAY 3")
    println("Running part 1")
    part1()
    println("Running part 2")
    part2()
  }
}

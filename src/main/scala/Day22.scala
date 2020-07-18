object Day22 {
  val input = scala.io.Source.fromFile("data/data22.txt", "UTF-8").getLines.toVector

  def part1(): Unit = {
    var deck = (0 to 10006).toVector
    for (line <- input) {
      if (line == "deal into new stack") {
        deck = deck.reverse
      }
      else {
        val words = line.split(" ")
        words(0) match {
          case "cut" => {
            val n = math.abs(words(1).toInt)
            if (words(1).toInt > 0) deck = deck.slice(n, deck.length) ++ deck.take(n)
            else deck = deck.takeRight(n) ++ deck.slice(0, deck.length - n)
          }
          case "deal" => {
            val xs: Array[Int] = Array.fill(deck.length)(0)
            for (i <- 0 until deck.length) {
              xs((i*words(3).toInt) % deck.length) = deck(i)
            }
            deck = xs.toVector
          }
          case default => throw new Exception("UNKNOWN: " + default)
        }
      }
    }
    println(deck.indexOf(2019))
  }

  def part2(): Unit = {
    /*
    val cards = 119315717514047L
    val repeats = 101741582076661L
    var offset = 0L
    var increment = 1L

    for (line <- input) {
      if (line == "deal into new stack") {
        increment *= -1
        offset += increment
      }
      else {
        val words = line.split(" ")
        
        words(0) match {
          case "cut" => {
            offset += increment * words(1).toInt
          }
          case "deal" => {
            val xs: Array[Int] = Array.fill(deck.length)(0)
            for (i <- 0 until deck.length) {
              xs((i*words(3).toInt) % deck.length) = deck(i)
            }
            deck = xs.toVector
          }
          case default => throw new Exception("UNKNOWN: " + default)
        }
      }
    }
    println(deck.indexOf(2019))*/
  }

  def apply() = {
    println("SOLUTION DAY 22")
    println("Running part 1")
    //part1()
    println("Running part 2")
    part2()
  }
}
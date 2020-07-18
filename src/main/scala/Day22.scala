import scala.collection.mutable.Map
import scala.collection.immutable.LazyList.cons
import java.math.BigInteger

object Day22 {
  val input = scala.io.Source.fromFile("data/data22.txt", "UTF-8").getLines.toVector

  def part1(): Unit = {
    var deck: Vector[Int] = (0 to 10006).toVector
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
    var n: BigInt = 119315717514047L

    var a: BigInt = 1
    var b: BigInt = 0
    for (line <- input) {
      if (line == "deal into new stack") {
        a = -a
        b = -1 - b
      }
      else {
        val words = line.split(" ")
        words(0) match {
          case "cut" => {
            b -= words(1).toLong
          }
          case "deal" => {
            a *= words(3).toLong
            b *= words(3).toLong
          }
          case default => throw new Exception("UNKNOWN: " + default)
        }
      }
      a = a.mod(n)
      b = b.mod(n)
    }

    val repeats: BigInt = 101741582076661L
    val repeatedA = a.modPow(repeats, n)
    val repeatedB = (b * (repeatedA - 1) * (a - 1).modInverse(n)).mod(n)

    // inverse of f(x)=repeatedA*x + repeatedB x=2020
    println(((2020 - repeatedB) * repeatedA.modInverse(n)).mod(n))
  }

  def apply() = {
    println("SOLUTION DAY 22")
    println("Running part 1")
    part1()
    println("Running part 2")
    part2()
  }
}
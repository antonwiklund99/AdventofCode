object Main {
  def main(args: Array[String]) = {
    scala.io.StdIn.readLine("Enter day to run: ").toInt match {
      case 1 => Day1()
      case 2 => Day2()
      case 3 => Day3()
      case 4 => Day4()
      case 5 => Day5()
      case 6 => Day6()
      case 7 => Day7()
      case 8 => Day8()
      case 9 => Day9()
      case 10 => Day10()
      case 11 => Day11()
      case 12 => Day12()
      case 13 => Day13()
      case 14 => Day14()
      case 15 => Day15()
      case 16 => Day16()
      case 17 => Day17()
      case 18 => Day18()
      case 19 => Day19()
      case 20 => Day20()
      case 21 => Day21()
      case 22 => Day22()
      case 23 => Day23()
      case 24 => Day24()
      case 25 => Day25()
      case _ => println("Unknown option")
    }
  }
}
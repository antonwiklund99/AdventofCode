class Node(var name: String) {
  var parent: Node = null
  var orbits: scala.collection.mutable.ArrayBuffer[Node] = scala.collection.mutable.ArrayBuffer.empty
}

object Day6 {
  val input = scala.io.Source.fromFile("data/data6.txt", "UTF-8").getLines.toVector
  val m: scala.collection.mutable.Map[String,Node] = scala.collection.mutable.Map.empty

  def initNodes(): Unit = {
    for (l <- input) {
      var s = l.split("\\)")
      var (x,y) = (s(0),s(1))
      if (!m.contains(x)) m(x) = new Node(x)
      if (!m.contains(y)) m(y) = new Node(y)
      m(x).orbits += m(y)
      m(y).parent = m(x)
    }
  }

  def part1(): Unit = {
    var sum = 0
    def rec(n: Node): Unit = n.orbits.foreach(i => {sum += 1;rec(i)})
    
    m.toVector.foreach(n => rec(n._2))
    println(sum)
  }
  
  def part2(): Unit = {
    def rec(n: Node, jumps: Int): (Boolean,Int) = {
      if (n.name == "SAN") (true, jumps)
      else {
        for (i <- n.orbits) {
          var r = rec(i, jumps + 1)
          if (r._1) return (true, r._2)
        }
        (false, jumps + 1)
      }
    }

    var head = m("YOU").parent
    var i = 0
    var res = (false,0)
    while (!res._1 && head.name != "SAN") {
      res = rec(head, i)
      head = head.parent
      i += 1
    }
    println(res._2 - 1)
  }

  def apply() = {
    initNodes()
    println("SOLUTION DAY 6")
    println("Running part 1")
    part1()
    println("Running part 2")
    part2()
  }
}
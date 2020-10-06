import scala.collection.mutable.Map
import scala.collection.mutable.Set
import scala.collection.mutable.ArrayBuffer

case class IPos(x: Int, y: Int) {
  def +(p: IPos) = IPos(x + p.x, y + p.y)
  def -(p: IPos) = IPos(x - p.x, y - p.y)
  def allDirections() = Array(IPos(x + 1, y), IPos(x - 1, y), IPos(x, y + 1), IPos(x, y - 1))
}

object Day18 {
  val input = scala.io.Source.fromFile("data/data18.txt", "UTF-8").getLines.map(_.toCharArray).toArray
  
  val capitalLetters: scala.collection.immutable.Set[Char] = ('A' to 'Z').toSet
  val smallLetters: scala.collection.immutable.Set[Char] = ('a' to 'z').toSet
  val allKeys: scala.collection.immutable.Set[Char] = input.flatten.filter(smallLetters.contains(_)).toSet

  var calculated1: Map[IPos, Map[Set[Char], Int]] = Map.empty // map where lowest step from pos with key is calculated[pos][keys]

  def evalPosition1D(pos: Array[Array[Char]], keys: Set[Char], cutoff: Int): Int = {
    if (keys == allKeys) {
      return 0;
    } 
    
    val y: Int = pos.indexWhere(a => a.contains('@'))
    val start: IPos = IPos(pos(y).indexOf('@'), y)
    if (calculated1.contains(start) && calculated1(start).contains(keys)) {
      return calculated1(start)(keys);
    } else {
      var visited: Set[IPos] = Set(start)
      var current: Set[IPos] = Set(start)
      var stepsAdded: Int = 0
      var stepsBest: Int = Integer.MAX_VALUE - 1000
      var stepsSinceKey: Int = 0
      while (current.size != 0) {
        if (stepsAdded > cutoff) {
          return Integer.MAX_VALUE - 1000
        }
        stepsAdded += 1
        current = current.map(_.allDirections).flatten

        for (p <- current) {
          val value = pos(p.y)(p.x)
          if (value == '#' || visited.contains(p) || (capitalLetters.contains(value) && !keys.contains(value.toLower))) {
            current.remove(p)
          }
          else if (allKeys.contains(value)) {
            pos(p.y)(p.x) = '@'
            pos(start.y)(start.x) = '.'
            keys.add(value)
            var res = stepsAdded + evalPosition1D(pos, keys, Math.min(stepsBest - stepsSinceKey, cutoff - stepsAdded))
            stepsBest = Math.min(res, stepsBest)
            pos(p.y)(p.x) = value
            pos(start.y)(start.x) = '@'
            keys.remove(value)
            current.remove(p)
            stepsSinceKey = 0
          }
          else {
            stepsSinceKey += 1
          }
          visited.add(p)
        }
      }      
      if (!calculated1.contains(start)) calculated1(start) = Map.empty
      calculated1(start) += ((keys.clone(), stepsBest))
      stepsBest
    }
  }
  
  var calculated2: Map[Set[IPos], Map[Set[Char], Int]] = Map.empty
  def evalPosition4D(pos: Array[Array[Char]], keys: Set[Char]): Int = {
    if (keys == allKeys) {
      return 0;
    } 
    
    var starts: Set[IPos] = Set.empty
    for (y <- pos.indices) {
      for (x <- pos(y).indices) {
        if (pos(y)(x) == '@') {
          starts.add(IPos(x,y))
        }
      }
    }

    if (calculated2.contains(starts) && calculated2(starts).contains(keys)) {
      return calculated2(starts)(keys);
    } else {
      var stepsBest: Int = Integer.MAX_VALUE - 1000
      starts.foreach(start => {
        var visited: Set[IPos] = Set(start)
        var current: Set[IPos] = Set(start)
        var stepsAdded: Int = 0
        while (current.size != 0) {
          stepsAdded += 1
          current = current.map(_.allDirections).flatten

          for (p <- current) {
            val value = pos(p.y)(p.x)
            if (value == '#' || visited.contains(p) || (capitalLetters.contains(value) && !keys.contains(value.toLower))) {
              current.remove(p)
            }
            else if (allKeys.contains(value)) {
              var x = pos.updated(p.y, pos(p.y).updated(p.x, '@'))
              x.update(start.y, x(start.y).updated(start.x, '.'))
              keys.add(value)
              var res = stepsAdded + evalPosition4D(x, keys)
              stepsBest = Math.min(res, stepsBest)
              keys.remove(value)
              current.remove(p)
            }
            visited.add(p)
          }
        }
      })
      if (!calculated2.contains(starts)) calculated2(starts) = Map.empty
      calculated2(starts) += ((keys.clone(), stepsBest))
      stepsBest
    }
  }

  def part1(): Unit = {
    println(evalPosition1D(input, Set.empty, Integer.MAX_VALUE - 1000))
  }

  def part2(): Unit = {
    val y: Int = input.indexWhere(a => a.contains('@'))
    val x: Int = input(y).indexOf('@')
    // Split
    input(y - 1)(x - 1) = '@'
    input(y - 1)(x)     = '#'
    input(y - 1)(x + 1) = '@'
    input(y)(x - 1) = '#'
    input(y)(x)     = '#'
    input(y)(x + 1) = '#'
    input(y + 1)(x - 1) = '@'
    input(y + 1)(x)     = '#'
    input(y + 1)(x + 1) = '@'
    println(evalPosition4D(input, Set()))
  }

  def apply() = {
    println("SOLUTION DAY 18")
    println("Running part 1")
    part1()
    println("Running part 2")
    part2()
  }
}
package adventofcode.day03

import adventofcode.day03.Move.*
import java.io.File
import kotlin.math.abs


data class Point(val x: Int, val y: Int) {

    operator fun plus(other: Point) = copy(x = x + other.x, y = y + other.y)

    fun dist(other: Point = port): Int = abs(x - other.x) + abs(y - other.y)

}

val port = Point(0, 0)

typealias Vector = Point

typealias Trace = List<Point>

sealed class Move(val vector: Vector, val steps: Int) {

    class Up(steps: Int) : Move(Vector(0, 1), steps)
    class Down(steps: Int) : Move(Vector(0, -1), steps)
    class Left(steps: Int) : Move(Vector(-1, 0), steps)
    class Right(steps: Int) : Move(Vector(1, 0), steps)

    fun generateTrace(start: Point): Trace =
        generateSequence(start + vector) { pt -> pt + vector }.take(steps).toList()

}

fun String.toMove(): Move {
    if (this.length < 2) throw IllegalArgumentException("invalid move $this")
    val code = this[0]
    val steps = this.substring(1).toInt()
    return when (code) {
        'U'  -> Up(steps)
        'D'  -> Down(steps)
        'L'  -> Left(steps)
        'R'  -> Right(steps)
        else -> throw IllegalArgumentException("invalid move code $code")
    }
}

fun String.trace(): Trace {
    val initialTrace = listOf(port)
    val moves = split(",").map(String::toMove)
    return moves.fold(initialTrace) { acc, move -> acc + move.generateTrace(acc.last()) }
}

fun distToNearestIntersect(tr1: Trace, tr2: Trace): Int? =
    tr1.drop(1).intersect(tr2.drop(1)).map { it.dist() }.min()

fun main() {
    val lines = File("./input/day03.txt").readLines()
    val tr1 = lines[0].trace()
    val tr2 = lines[1].trace()
    println(distToNearestIntersect(tr1, tr2))
}
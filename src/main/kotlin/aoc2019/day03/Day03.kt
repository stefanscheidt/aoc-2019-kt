package aoc2019.day03

import aoc2019.day03.Move.*
import java.io.File
import kotlin.math.abs

// --- Points

data class Point(val x: Int, val y: Int) {

    operator fun plus(other: Point) = copy(x = x + other.x, y = y + other.y)

    fun dist(other: Point = port): Int = abs(x - other.x) + abs(y - other.y)

}

val port = Point(0, 0)

// --- Moves and Traces

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

private fun parseMove(move: String): Move {
    if (move.length < 2) throw IllegalArgumentException("invalid move $move")
    val code = move[0]
    val steps = move.substring(1).toInt()
    return when (code) {
        'U' -> Up(steps)
        'D' -> Down(steps)
        'L' -> Left(steps)
        'R' -> Right(steps)
        else -> throw IllegalArgumentException("invalid move code $code")
    }
}

fun traceOf(moves: String): List<Point> {
    val initialTrace = listOf(port)
    return moves.split(",")
        .map(::parseMove)
        .fold(initialTrace) { acc, move -> acc + move.generateTrace(acc.last()) }
        .drop(1)
}

// --- Trace functions

fun intersectionsOf(tr1: Trace, tr2: Trace): Set<Point> = tr1.intersect(tr2)

fun distanceToNearestIntersectionOf(tr1: Trace, tr2: Trace): Int? =
    intersectionsOf(tr1, tr2).map { it.dist() }.minOrNull()

fun stepsToPoint(point: Point, trace: Trace): Int =
    trace.takeWhile { it != point }.count() + 1

fun minimalWireSteps(tr1: Trace, tr2: Trace): Int? =
    intersectionsOf(tr1, tr2).map { stepsToPoint(it, tr1) + stepsToPoint(it, tr2) }.minOrNull()

// --- Main

fun main() {
    val lines = File("./input/day03.txt").readLines()
    val tr1 = traceOf(lines[0])
    val tr2 = traceOf(lines[1])
    println("Part 1: ${distanceToNearestIntersectionOf(tr1, tr2)}")
    println("Part 2: ${minimalWireSteps(tr1, tr2)}")
}

package adventofcode.common

data class Coordinate(val x: Int, val y: Int) {
    operator fun plus(other: Coordinate): Coordinate =
        Coordinate(x + other.x, y + other.y)

    operator fun unaryMinus(): Coordinate =
        Coordinate(-x, -y)

    operator fun minus(other: Coordinate): Coordinate =
        this + (-other)
}

fun c(x: Int, y: Int): Coordinate =
    Coordinate(x, y)

typealias Vector = Coordinate

fun vect(x: Int, y: Int): Vector =
    c(x, y)
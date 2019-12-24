package adventofcode.day10

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2


class Day10Test {

    @Test
    fun `parse map`() {
        val map = """
            .#..#
            .....
            #####
            ....#
            ...##
        """.trimIndent()

        val asteroids: AsteroidField = parseAsteroidField(map)

        assertThat(asteroids).containsExactlyInAnyOrder(
            c(1, 0), c(4, 0),
            c(0, 2), c(1, 2), c(2, 2), c(3, 2), c(4, 2),
            c(4, 3),
            c(3, 4), c(4, 4)
        )
    }

    @Test
    fun `size of a field`() {
        val map = """
            ....
            ...#
            ..#.
        """.trimIndent()

        val size = size(parseAsteroidField(map))

        assertThat(size).isEqualTo(c(3, 2))
    }

    @Test
    fun `ray of an asteroid`() {
        val a = c(0, 0)
        val size = c(9, 9)

        assertThat(rayBehind(a, c(3, 1), size))
            .containsExactlyInAnyOrder(c(6, 2), c(9, 3))
        assertThat(rayBehind(a, c(3, 3), size))
            .containsExactlyInAnyOrder(c(4, 4), c(5, 5), c(6, 6), c(7, 7), c(8, 8), c(9, 9))
    }

    @Test
    fun `part one example one`() {
        val map = """
            .#..#
            .....
            #####
            ....#
            ...##
        """.trimIndent()
        val asteroids = parseAsteroidField(map)

        assertThat(optimalAsteroidForMonitoring(asteroids)).isEqualTo(c(3, 4) to 8)
    }

    @Test
    fun `part one example two`() {
        val map = """
            ......#.#.
            #..#.#....
            ..#######.
            .#.#.###..
            .#..#.....
            ..#....#.#
            #..#....#.
            .##.#..###
            ##...#..#.
            .#....####
        """.trimIndent()
        val asteroids = parseAsteroidField(map)

        assertThat(optimalAsteroidForMonitoring(asteroids)).isEqualTo(c(5, 8) to 33)
    }

    @Test
    fun `part one example three`() {
        val map = """
            #.#...#.#.
            .###....#.
            .#....#...
            ##.#.#.#.#
            ....#.#.#.
            .##..###.#
            ..#...##..
            ..##....##
            ......#...
            .####.###.
        """.trimIndent()
        val asteroids = parseAsteroidField(map)

        assertThat(optimalAsteroidForMonitoring(asteroids)).isEqualTo(c(1, 2) to 35)
    }

    @Test
    fun `part one`() {
        val map = File("./input/day10.txt").readText()
        val asteroids = parseAsteroidField(map)

        assertThat(optimalAsteroidForMonitoring(asteroids)).isEqualTo(c(17, 23) to 296)
    }

    @Test
    fun `part two`() {
        val map = File("./input/day10.txt").readText()
        val asteroids = parseAsteroidField(map)
        val station = optimalAsteroidForMonitoring(asteroids)!!.first

        val vaporized = vaporized(asteroids, station)

        assertThat(vaporized[200 - 1]).isEqualTo(c(2, 4))
    }

}

data class Coordinate(val x: Int, val y: Int) {
    operator fun plus(other: Coordinate): Coordinate =
        c(x + other.x, y + other.y)

    operator fun unaryMinus(): Coordinate =
        c(-x, -y)

    operator fun minus(other: Coordinate): Coordinate =
        this + (-other)
}

fun c(x: Int, y: Int): Coordinate =
    Coordinate(x, y)

typealias Asteroid = Coordinate
typealias AsteroidField = Set<Asteroid>

fun parseAsteroidField(map: String): AsteroidField {
    return map.split(System.lineSeparator()).withIndex()
        .flatMap { (y, row) ->
            row.withIndex().fold(emptySet<Coordinate>()) { acc, (x, c) ->
                if (c == '.') acc else acc + c(x, y)
            }
        }.toSet()
}

fun size(field: AsteroidField): Coordinate =
    c(field.maxBy(Coordinate::x)?.x ?: 0, field.maxBy(Coordinate::y)?.y ?: 0)

fun vect(a: Coordinate, b: Coordinate): Coordinate {
    val dx = b.x - a.x
    val dy = b.y - a.y
    val gcd = gcd(abs(dx), abs(dy))
    return c(dx / gcd, dy / gcd)
}

// excluding b
fun rayBehind(a: Coordinate, b: Coordinate, size: Coordinate): Set<Coordinate> {
    val v = vect(a, b)
    return generateSequence(b + v) { it + v }
        .takeWhile { it.x in (0..size.x) && it.y in (0..size.y) }
        .toSet()
}

fun visibleAsteroids(station: Asteroid, field: AsteroidField): AsteroidField {
    val candidates = field - station
    val size = size(field)
    val hiddenAsteroids = candidates
        .flatMap { rayBehind(station, it, size) }
        .toSet()
    return candidates - hiddenAsteroids
}

fun optimalAsteroidForMonitoring(asteroids: AsteroidField): Pair<Asteroid, Int>? =
    asteroids
        .associateWith { visibleAsteroids(it, asteroids).size }
        .maxBy { it.value }
        ?.toPair()

fun dist(c1: Coordinate, c2: Coordinate): Int =
    abs(c1.x - c2.x) + abs(c1.y - c2.y)

fun angle(target: Coordinate, station: Coordinate): Double {
    val vector = target - station
    return PI - atan2(vector.x.toDouble(), vector.y.toDouble())
}

fun vaporizedByOneRotation(asteroids: AsteroidField, station: Coordinate): List<Coordinate> =
    (asteroids - station)
        .groupBy { angle(it, station) }
        .toSortedMap()
        .mapValues { (_, xs) -> xs.minBy { x -> dist(x, station) } }
        .values
        .filterNotNull()
        .toList()

fun vaporized(asteroids: AsteroidField, station: Asteroid): List<Asteroid> {
    var targets = asteroids - station
    var result = emptyList<Coordinate>()
    while (targets.isNotEmpty()) {
        val vaporized = vaporizedByOneRotation(targets, station)
        targets = targets - vaporized
        result = result + vaporized
    }
    return result
}

fun gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)

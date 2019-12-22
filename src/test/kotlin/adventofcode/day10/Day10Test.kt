package adventofcode.day10

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.abs


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

        assertThat(ray(a, c(3, 1), size))
            .containsExactlyInAnyOrder(c(6, 2), c(9, 3))
        assertThat(ray(a, c(3, 3), size))
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

}

data class Coordinate(val x: Int, val y: Int) {
    operator fun plus(other: Coordinate): Coordinate =
        c(x + other.x, y + other.y)
}

fun c(x: Int, y: Int): Coordinate =
    Coordinate(x, y)

fun dist(c1: Coordinate, c2: Coordinate): Int =
    abs(c2.x - c1.x) + abs(c2.y - c1.y)

typealias AsteroidField = Set<Coordinate>

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

typealias Ray = Set<Coordinate>

fun ray(a: Coordinate, b: Coordinate, size: Coordinate): Ray {
    val dx = b.x - a.x
    val dy = b.y - a.y
    val gcd = gcd(abs(dx), abs(dy))
    val v = c(dx / gcd, dy / gcd)
    return generateSequence(b + v) { it + v }
        .takeWhile { it.x in (0..size.x) && it.y in (0..size.y) }
        .toSet()
}

fun visibleAsteroids(asteroid: Coordinate, field: AsteroidField): AsteroidField {
    val size = size(field)
    val hiddenAsteroids = (field - asteroid)
        .flatMap { ray(asteroid, it, size) }
        .toSet()
    return field - asteroid - hiddenAsteroids
}

fun optimalAsteroidForMonitoring(asteroids: AsteroidField): Pair<Coordinate, Int>? =
    asteroids
        .associateWith { visibleAsteroids(it, asteroids).size }
        .maxBy { it.value }
        ?.toPair()

fun gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)

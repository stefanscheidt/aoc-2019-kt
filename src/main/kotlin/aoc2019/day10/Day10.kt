package aoc2019.day10

import aoc2019.common.Coordinate
import aoc2019.common.c
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2

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
    c(
        field.maxByOrNull(Coordinate::x)?.x ?: 0,
        field.maxByOrNull(Coordinate::y)?.y ?: 0
    )

fun vect(a: Coordinate, b: Coordinate): Coordinate {
    val dx = b.x - a.x
    val dy = b.y - a.y
    val gcd = gcd(
        abs(dx),
        abs(dy)
    )
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
        .maxByOrNull { it.value }
        ?.toPair()

fun dist(c1: Coordinate, c2: Coordinate): Int =
    abs(c1.x - c2.x) + abs(c1.y - c2.y)

fun angle(target: Coordinate, station: Coordinate): Double {
    val vector = target - station
    return PI - atan2(
        vector.x.toDouble(),
        vector.y.toDouble()
    )
}

fun vaporizedByOneRotation(asteroids: AsteroidField, station: Coordinate): List<Coordinate> =
    (asteroids - station)
        .groupBy { angle(it, station) }
        .toSortedMap()
        .mapValues { (_, xs) -> xs.minByOrNull { x -> dist(x, station) } }
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
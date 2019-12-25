package adventofcode.day10

import adventofcode.common.c
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File


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

        val asteroids: AsteroidField =
            parseAsteroidField(map)

        assertThat(asteroids).containsExactlyInAnyOrder(
            c(1, 0),
            c(4, 0),
            c(0, 2),
            c(1, 2),
            c(2, 2),
            c(3, 2),
            c(4, 2),
            c(4, 3),
            c(3, 4),
            c(4, 4)
        )
    }

    @Test
    fun `size of a field`() {
        val map = """
            ....
            ...#
            ..#.
        """.trimIndent()

        val size =
            size(parseAsteroidField(map))

        assertThat(size).isEqualTo(c(3, 2))
    }

    @Test
    fun `ray of an asteroid`() {
        val a = c(0, 0)
        val size = c(9, 9)

        assertThat(rayBehind(a, c(3, 1), size))
            .containsExactlyInAnyOrder(
                c(6, 2),
                c(9, 3)
            )
        assertThat(rayBehind(a, c(3, 3), size))
            .containsExactlyInAnyOrder(
                c(4, 4),
                c(5, 5),
                c(6, 6),
                c(7, 7),
                c(8, 8),
                c(9, 9)
            )
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

        assertThat(optimalAsteroidForMonitoring(asteroids)).isEqualTo(
            c(
                17,
                23
            ) to 296
        )
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

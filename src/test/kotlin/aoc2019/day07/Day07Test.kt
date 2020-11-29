package aoc2019.day07

import aoc2019.computer.loadProgram
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class Day07Test {

    @Test
    fun `compute all permutations of settings`() {
        assertThat(allPermutationsOf(listOf(1))).containsExactlyInAnyOrder(
            listOf(1)
        )

        assertThat(allPermutationsOf(listOf(1, 2))).containsExactlyInAnyOrder(
            listOf(1, 2),
            listOf(2, 1)
        )

        assertThat(allPermutationsOf(listOf(1, 2, 3))).containsExactlyInAnyOrder(
            listOf(1, 2, 3),
            listOf(1, 3, 2),
            listOf(2, 1, 3),
            listOf(2, 3, 1),
            listOf(3, 1, 2),
            listOf(3, 2, 1)
        )
    }

    @Test
    fun `should compute output signal`() {
        val program = listOf(3, 15, 3, 16, 1002, 16, 10, 16, 1, 16, 15, 15, 4, 15, 99, 0, 0).map { it.toLong() }
        val settings = listOf(4, 3, 2, 1, 0).map { it.toLong() }

        assertThat(amplify(program, settings)).isEqualTo(43210)
    }

    @Test
    fun `should find optimal settings`() {
        val program = listOf(3, 15, 3, 16, 1002, 16, 10, 16, 1, 16, 15, 15, 4, 15, 99, 0, 0).map { it.toLong() }

        val optimalOutput = optimalOutput(program)

        assertThat(optimalOutput?.value).isEqualTo(43210)
        assertThat(optimalOutput?.settings).isEqualTo(listOf(4, 3, 2, 1, 0).map { it.toLong() })
    }

    @Test
    fun `part one`() {
        val optimalOutput = optimalOutput(loadProgram("./input/day07.txt"))

        assertThat(optimalOutput?.value).isEqualTo(272368L)
        assertThat(optimalOutput?.settings).isEqualTo(listOf(1, 4, 2, 0, 3).map { it.toLong() })
    }

    @Test
    fun `part two sample one`() {
        val program = listOf(
            3, 26, 1001, 26, -4, 26, 3, 27, 1002, 27, 2, 27, 1, 27, 26,
            27, 4, 27, 1001, 28, -1, 28, 1005, 28, 6, 99, 0, 0, 5
        ).map { it.toLong() }
        val settings = listOf(9, 8, 7, 6, 5).map { it.toLong() }

        assertThat(amplifyWithFeedback(program, settings)).isEqualTo(139629729L)
    }

    @Test
    fun `part two sample two`() {
        val program = listOf(
            3, 52, 1001, 52, -5, 52, 3, 53, 1, 52, 56, 54, 1007, 54, 5, 55, 1005, 55, 26, 1001, 54,
            -5, 54, 1105, 1, 12, 1, 53, 54, 53, 1008, 54, 0, 55, 1001, 55, 1, 55, 2, 53, 55, 53, 4,
            53, 1001, 56, -1, 56, 1005, 56, 6, 99, 0, 0, 0, 0, 10
        ).map { it.toLong() }
        val settings = listOf(9, 7, 8, 5, 6).map { it.toLong() }

        assertThat(amplifyWithFeedback(program, settings)).isEqualTo(18216L)
    }

    @Test
    fun `part two`() {
        val optimalOutput = optimalOutputWithFeedback(loadProgram("./input/day07.txt"))

        assertThat(optimalOutput?.value).isEqualTo(19741286L)
        assertThat(optimalOutput?.settings).isEqualTo(listOf(9, 6, 5, 8, 7).map { it.toLong() })
    }

}
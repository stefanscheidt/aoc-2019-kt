package adventofcode.day07

import adventofcode.computer.loadProgram
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class Day07Test {

    @Test
    fun `should compute output signal`() {
        val program = listOf(3, 15, 3, 16, 1002, 16, 10, 16, 1, 16, 15, 15, 4, 15, 99, 0, 0)
        val settings = listOf(4, 3, 2, 1, 0)

        assertThat(amplify(program, settings)).isEqualTo(43210)
    }

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
    fun `find optimal settings`() {
        val program = listOf(3, 15, 3, 16, 1002, 16, 10, 16, 1, 16, 15, 15, 4, 15, 99, 0, 0)

        val optimalOutput = optimalOutput(program)

        assertThat(optimalOutput?.value).isEqualTo(43210)
        assertThat(optimalOutput?.settings).isEqualTo(listOf(4, 3, 2, 1, 0))
    }

    @Test
    fun `part one`() {
        val optimalOutput = optimalOutput(loadProgram("./input/day07.txt"))

        assertThat(optimalOutput?.value).isEqualTo(272368)
        assertThat(optimalOutput?.settings).isEqualTo(listOf(1, 4, 2, 0, 3))
    }

}
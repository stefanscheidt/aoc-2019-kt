package aoc2019.day05

import aoc2019.computer.loadProgram
import aoc2019.computer.runProgramWithInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day05Test {

    @Test
    fun `part one`() {
        val program = loadProgram("./input/day05.txt")

        assertThat(runProgramWithInput(program, 1))
            .containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 5044655)

        assertThat(runProgramWithInput(program, 5))
            .containsExactly(7408802)
    }

}
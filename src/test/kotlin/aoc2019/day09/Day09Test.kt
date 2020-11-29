package aoc2019.day09

import aoc2019.computer.loadProgram
import aoc2019.computer.runProgramWithInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class Day09Test {

    @Test
    fun `part one example one`() {
        val program = listOf(109, 1, 204, -1, 1001, 100, 1, 100, 1008, 100, 16, 101, 1006, 101, 0, 99)
            .map { it.toLong() }
        val output = runProgramWithInput(program)
        assertThat(output).isEqualTo(program)
    }

    @Test
    fun `part one example two`() {
        val program = listOf(1102, 34915192, 34915192, 7, 4, 7, 99, 0).map { it.toLong() }
        val output = runProgramWithInput(program)
        assertThat(output.first()).isGreaterThanOrEqualTo((1e15).toLong())
    }

    @Test
    fun `part one example three`() {
        val program = listOf(104L, 1125899906842624L, 99L)
        val output = runProgramWithInput(program)
        assertThat(output.first()).isEqualTo(1125899906842624L)
    }

    @Test
    fun `part one`() {
        val program = loadProgram("./input/day09.txt")
        val testOutput = runProgramWithInput(program, 1L)
        assertThat(testOutput).containsExactly(2494485073L)
    }

}
package adventofcode.day05

import adventofcode.computer.Computer
import adventofcode.computer.loadProgram
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day05Test {

    @Test
    fun `part one`() {
        val program = loadProgram("./input/day05.txt")
        val computer = Computer(program, listOf(1)).apply {
            runProgramm()
        }
        assertThat(computer.output).isEqualTo(listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 5044655))
    }

}
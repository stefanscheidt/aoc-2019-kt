package adventofcode.day02

import adventofcode.computer.ListComputer
import adventofcode.computer.loadProgram
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class Day02Test {

    @Test
    fun `patch and evaluate input`() {
        val computer = ListComputer(loadProgram("./input/day02.txt"))
        computer[1] = 12
        computer[2] = 2

        assertThat(computer.runProgram()).isEqualTo(11590668)
    }

    @Test
    fun `find phrase`() {
        val program = loadProgram("./input/day02.txt")
        assertThat(findPhrase(program, 19690720)).isEqualTo("2254")
    }

}



package adventofcode.day02

import adventofcode.computer.Memory
import adventofcode.computer.loadProgram
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class Day02Test {

    @Test
    fun `patch and evaluate input`() {
        val memory = Memory(loadProgram("./input/day02.txt"))
        memory[1] = 12
        memory[2] = 2

        assertThat(memory.runProgram()).isEqualTo(11590668)
    }

    @Test
    fun `find phrase`() {
        val program = loadProgram("./input/day02.txt")
        assertThat(findPhrase(program, 19690720)).isEqualTo("2254")
    }

}



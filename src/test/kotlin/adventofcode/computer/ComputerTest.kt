package adventofcode.computer

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class ComputerTest {

    @Test
    fun `read input and write output`() {
        val program = listOf(3, 0, 4, 0, 99)
        val input = listOf(123)

        val computer = Computer(program, input).apply {
            runProgramm()
        }

        assertThat(computer.output).containsExactly(123)
    }

}
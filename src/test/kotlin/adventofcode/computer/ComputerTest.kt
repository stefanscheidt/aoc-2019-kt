package adventofcode.computer

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class ComputerTest {

    @Test
    fun `should read input and write output`() {
        val program = listOf(3, 0, 4, 0, 99)
        val input = listOf(123)

        val computer = Computer(program, input).apply {
            runProgramm()
        }

        assertThat(computer.output).containsExactly(123)
    }

    @Test
    fun `should evaluate with immediate mode`() {
        val program = listOf(1101, 1, 2, 0, 99)

        val result = Computer(program).runProgramm()

        assertThat(result).isEqualTo(1 + 2)
    }

    @Test
    fun `should halt`() {
        val result = Computer(listOf(1002, 4, 3, 4, 33)).runProgramm()

        assertThat(result).isEqualTo(1002)
    }

}

class ParameterModesTest {

    @Test
    fun `should parse parameter modes`() {
        val parameterModes = parameterModes(1011)
        assertThat(parameterModes.next()).isEqualTo(1)
        assertThat(parameterModes.next()).isEqualTo(1)
        assertThat(parameterModes.next()).isEqualTo(0)
        assertThat(parameterModes.next()).isEqualTo(1)
        assertThat(parameterModes.next()).isEqualTo(0)
        assertThat(parameterModes.next()).isEqualTo(0)
    }

}
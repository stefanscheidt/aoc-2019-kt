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

    @Test
    fun `should compare and jump`() {
        val p1 = listOf(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8)
        assertThat(runProgramWithInput(p1, 7)).containsExactly(0)
        assertThat(runProgramWithInput(p1, 8)).containsExactly(1)
        assertThat(runProgramWithInput(p1, 9)).containsExactly(0)

        val p2 = listOf(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8)
        assertThat(runProgramWithInput(p2, 7)).containsExactly(1)
        assertThat(runProgramWithInput(p2, 8)).containsExactly(0)
        assertThat(runProgramWithInput(p2, 9)).containsExactly(0)

        val p3 = listOf(3, 3, 1108, -1, 8, 3, 4, 3, 99)
        assertThat(runProgramWithInput(p3, 7)).containsExactly(0)
        assertThat(runProgramWithInput(p3, 8)).containsExactly(1)
        assertThat(runProgramWithInput(p3, 9)).containsExactly(0)

        val p4 = listOf(3, 3, 1107, -1, 8, 3, 4, 3, 99)
        assertThat(runProgramWithInput(p4, 7)).containsExactly(1)
        assertThat(runProgramWithInput(p4, 8)).containsExactly(0)
        assertThat(runProgramWithInput(p4, 9)).containsExactly(0)

        val p5 = listOf(3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9)
        assertThat(runProgramWithInput(p5, 0)).containsExactly(0)
        assertThat(runProgramWithInput(p5, 1)).containsExactly(1)

        val p6 = listOf(3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1)
        assertThat(runProgramWithInput(p6, 0)).containsExactly(0)
        assertThat(runProgramWithInput(p6, 1)).containsExactly(1)
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
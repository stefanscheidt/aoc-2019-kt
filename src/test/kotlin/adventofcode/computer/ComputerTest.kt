package adventofcode.computer

import adventofcode.computer.Operation.*
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.awaitility.kotlin.matches
import org.awaitility.kotlin.untilCallTo
import org.junit.jupiter.api.Test


class ParameterModesTest {

    @Test
    fun `should parse parameter modes`() {
        assertThat(accessModes(code = 1011, arity = 6))
            .containsExactly(1L, 1L, 0L, 1L, 0L, 0L)
    }

}

class ListComputerTest {

    @Test
    fun `get memory value`() {
        val computer = ListComputer(listOf(10, 20, 30))

        assertThat(computer[0]).isEqualTo(10)
        assertThat(computer[1]).isEqualTo(20)
        assertThat(computer[2]).isEqualTo(30)
        assertThat(computer[3]).isEqualTo(0)
    }

    @Test
    fun `one plus one`() {
        val computer = ListComputer(listOf(1, 0, 0, 0, 99)).apply { runProgram() }

        assertThat(computer.dumpMemory()).containsExactly(2, 0, 0, 0, 99)
    }

    @Test
    fun `tree times two`() {
        val computer = ListComputer(listOf(2, 3, 0, 3, 99)).apply { runProgram() }

        assertThat(computer.dumpMemory()).containsExactly(2, 3, 0, 6, 99)
    }

    @Test
    fun `99 times 99`() {
        val computer = ListComputer(listOf(2, 4, 4, 5, 99, 0)).apply { runProgram() }

        assertThat(computer.dumpMemory()).containsExactly(2, 4, 4, 5, 99, 9801)
    }

    @Test
    fun `should read input and write output`() {
        val program = listOf(3L, 0L, 4L, 0L, 99L)
        val input = listOf(123L)

        val computer = ListComputer(program, input).apply {
            runProgram()
        }

        assertThat(computer.output).containsExactly(123)
    }

    @Test
    fun `should evaluate with immediate mode`() {
        val program = listOf(1101L, 1L, 2L, 0L, 99L)

        val result = ListComputer(program).runProgram()

        assertThat(result).isEqualTo(1L + 2L)
    }

    @Test
    fun `should halt`() {
        val result = ListComputer(listOf(1002L, 4L, 3L, 4L, 33L)).runProgram()

        assertThat(result).isEqualTo(1002L)
    }

    @Test
    fun `should compare and jump`() {
        val p1 = listOf(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8).map { it.toLong() }
        assertThat(runProgramWithInput(p1, 7L)).containsExactly(0L)
        assertThat(runProgramWithInput(p1, 8L)).containsExactly(1L)
        assertThat(runProgramWithInput(p1, 9L)).containsExactly(0L)

        val p2 = listOf(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8).map { it.toLong() }
        assertThat(runProgramWithInput(p2, 7L)).containsExactly(1L)
        assertThat(runProgramWithInput(p2, 8L)).containsExactly(0L)
        assertThat(runProgramWithInput(p2, 9L)).containsExactly(0L)

        val p3 = listOf(3, 3, 1108, -1, 8, 3, 4, 3, 99).map { it.toLong() }
        assertThat(runProgramWithInput(p3, 7L)).containsExactly(0L)
        assertThat(runProgramWithInput(p3, 8L)).containsExactly(1L)
        assertThat(runProgramWithInput(p3, 9L)).containsExactly(0L)

        val p4 = listOf(3, 3, 1107, -1, 8, 3, 4, 3, 99).map { it.toLong() }
        assertThat(runProgramWithInput(p4, 7L)).containsExactly(1L)
        assertThat(runProgramWithInput(p4, 8L)).containsExactly(0L)
        assertThat(runProgramWithInput(p4, 9L)).containsExactly(0L)

        val p5 = listOf(3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9).map { it.toLong() }
        assertThat(runProgramWithInput(p5, 0)).containsExactly(0L)
        assertThat(runProgramWithInput(p5, 1)).containsExactly(1L)

        val p6 = listOf(3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1).map { it.toLong() }
        assertThat(runProgramWithInput(p6, 0L)).containsExactly(0L)
        assertThat(runProgramWithInput(p6, 1L)).containsExactly(1L)
    }

}

class QueueComputerTest {

    @Test
    fun `should run async and wait for input`() {
        val program = listOf(READ.opcode, 0, WRITE.opcode, 0, WRITE.opcode, 0, HALT.opcode)
        val computer = QueueComputer(program)

        computer
            .runAsync()
            .putInput(42)

        await() untilCallTo { computer.takeOutput() } matches { it == 42L }
        assertThat(computer.output).containsExactly(42, 42)
    }

}

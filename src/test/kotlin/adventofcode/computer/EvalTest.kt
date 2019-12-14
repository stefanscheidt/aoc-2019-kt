package adventofcode.computer

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class OperationTest {

    @Test
    fun `add two numbers`() {
        assertThat(Operation.ADD(listOf(1, 2), InputOutputDevice())).isEqualTo(3)
    }

    @Test
    fun `multiply to numbers`() {
        assertThat(Operation.MULT(listOf(2, 3), InputOutputDevice())).isEqualTo(6)
    }

    @Test
    fun `halt programm`() {
        assertThat(Operation.HALT(emptyList(), InputOutputDevice())).isNull()
    }

    @Test
    fun `too few params`() {
        assertThrows<IllegalArgumentException> {
            Operation.ADD(listOf(1), InputOutputDevice())
        }
    }

}

class MemoryTest {

    @Test
    fun `get value`() {
        val memory = Memory(listOf(10, 20, 30))
        assertThat(memory[0]).isEqualTo(10)
        assertThat(memory[1]).isEqualTo(20)
        assertThat(memory[2]).isEqualTo(30)
        assertThrows<IndexOutOfBoundsException> {
            memory[3]
        }
    }

}

class EvalTest {

    @Test
    fun `one plus one`() {
        assertThat(eval(listOf(1, 0, 0, 0, 99))).containsExactly(2, 0, 0, 0, 99)
    }

    @Test
    fun `tree times two`() {
        assertThat(eval(listOf(2, 3, 0, 3, 99))).containsExactly(2, 3, 0, 6, 99)
    }

    @Test
    fun `99 times 99`() {
        assertThat(eval(listOf(2, 4, 4, 5, 99, 0))).containsExactly(2, 4, 4, 5, 99, 9801)
    }

    @Test
    fun `some programs`() {
        assertThat(eval(listOf(1, 1, 1, 4, 99, 5, 6, 0, 99)))
            .containsExactly(30, 1, 1, 4, 2, 5, 6, 0, 99)
        assertThat(eval(listOf(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50)))
            .containsExactly(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50)
    }

}

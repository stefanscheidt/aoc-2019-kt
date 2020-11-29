package aoc2019.computer

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class OperationTest {

    @Test
    fun `add two numbers`() {
        Assertions.assertThat(Operation.ADD(listOf(1, 2), ListInputOutputDevice())).isEqualTo(OperationResult(3))
    }

    @Test
    fun `multiply to numbers`() {
        Assertions.assertThat(Operation.MULT(listOf(2, 3), ListInputOutputDevice())).isEqualTo(OperationResult(6))
    }

    @Test
    fun `halt programm`() {
        Assertions.assertThat(Operation.HALT(emptyList(), ListInputOutputDevice())).isEqualTo(OperationResult(null))
    }

    @Test
    fun `too few params`() {
        assertThrows<IllegalArgumentException> {
            Operation.ADD(listOf(1), ListInputOutputDevice())
        }
    }

}
package adventofcode.day02

import adventofcode.day02.Operation.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class Day02Test {

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

    @Test
    fun `patch and evaluate input`() {
        val memory = Memory(loadProgram("./input/day02.txt"))
        memory[1] = 12
        memory[2] = 2

        assertThat(memory.runProgram()).isEqualTo(11590668)
    }

}

class OperationTest {

    @Test
    fun `add two numbers`() {
        assertThat(Add(listOf(1, 2))).isEqualTo(3)
    }

    @Test
    fun `multiply to numbers`() {
        assertThat(Mult(listOf(2, 3))).isEqualTo(6)
    }

    @Test
    fun `halt programm`() {
        assertThat(Halt(emptyList())).isNull()
    }

    @Test
    fun `too few params`() {
        assertThrows<IllegalArgumentException> {
            Add(listOf(1))
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


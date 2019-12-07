package adventofcode.day04

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class Dat04Test {

    @Test
    fun `password meets criteria of part 1`() {
        assertThat(criteriaOfPart1("111111")).isTrue()
        assertThat(criteriaOfPart1("223450")).isFalse()
        assertThat(criteriaOfPart1("123789")).isFalse()
    }

    @Test
    fun `repetition lengths`() {
        assertThat(repetitionLenghtsOf("1223334444")).isEqualTo(listOf(2, 3, 4))
    }

    @Test
    fun `number of passwords in part 1`() {
        assertThat(numberOfPasswordsInRange("171309", "643603", ::criteriaOfPart1)).isEqualTo(1625)
    }

    @Test
    fun `number of passwords in part 2`() {
        assertThat(numberOfPasswordsInRange("171309", "643603", ::criteriaOfPart2)).isEqualTo(1111)
    }

}


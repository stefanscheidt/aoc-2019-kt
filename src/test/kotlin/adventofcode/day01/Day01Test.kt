package adventofcode.day01

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.charset.StandardCharsets.UTF_8


class Day01Test {

    @Test
    fun `fuel for module`() {
        assertThat(fuelForModuleWithMass(14)).isEqualTo(2)
        assertThat(fuelForModuleWithMass(1969)).isEqualTo(966)
        assertThat(fuelForModuleWithMass(100756)).isEqualTo(50346)
    }

    @Test
    fun `process input`() {
        val result = File("./input/day01.txt").useLines(UTF_8, ::processLines)
        assertThat(result).isEqualTo(4866824)
    }

}
package adventofcode.day01

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.charset.StandardCharsets.UTF_8


class Day01Test {

    @Test
    fun `fuel for module`() {
        assertThat(fuelForModuleWithMass(14.toMass())).isEqualTo(2.toFuel())
        assertThat(fuelForModuleWithMass(1969.toMass())).isEqualTo(966.toFuel())
        assertThat(fuelForModuleWithMass(100756.toMass())).isEqualTo(50346.toFuel())
    }

    @Test
    fun `process input`() {
        val result = File("./input/day01.txt").useLines(UTF_8, ::processLines)
        assertThat(result).isEqualTo(4866824)
    }

}
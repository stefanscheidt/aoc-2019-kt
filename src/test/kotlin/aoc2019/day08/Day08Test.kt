package aoc2019.day08

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File


class Day08Test {

    @Test
    fun `part one`() {
        val input = File("./input/day08.txt").readText()
        val image = input.toImage(25, 6)
        val layer = layerWithFewestDigit(image, 0)!!

        assertThat(digitCount(layer, 1) * digitCount(layer, 2)).isEqualTo(1820)
    }

    @Test
    fun `part two`() {
        val input = File("./input/day08.txt").readText()
        val image = input.toImage(25, 6)
        val zukcj = """
            **** *  * *  *  **    ** 
               * *  * * *  *  *    * 
              *  *  * **   *       * 
             *   *  * * *  *       * 
            *    *  * * *  *  * *  * 
            ****  **  *  *  **   **  
        """.trimIndent()

        assertThat(render(image, 25, 6)).isEqualTo(zukcj)
    }

}


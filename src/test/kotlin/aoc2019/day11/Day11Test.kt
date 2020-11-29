package aoc2019.day11

import aoc2019.computer.QueueComputer
import aoc2019.computer.loadProgram
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class Day11Test {

    private val program = loadProgram("./input/day11.txt")

    @Test
    fun `part one`() {
        val robot = Robot(QueueComputer(program), BLACK)

        robot.start()

        assertThat(robot.painting.size).isEqualTo(2322)
    }

    @Test
    fun `part two`() {
        val robot = Robot(QueueComputer(program), WHITE)
        val expectedPainting = """
               ## #  #  ##  ###  ###   ##   ##  #  #   
                # #  # #  # #  # #  # #  # #  # #  #   
                # #### #  # #  # ###  #    #    #  #   
                # #  # #### ###  #  # # ## #    #  #   
             #  # #  # #  # # #  #  # #  # #  # #  #   
              ##  #  # #  # #  # ###   ###  ##   ##    """.trimIndent()

        robot.start()

        assertThat(renderPainting(robot.painting).trimIndent())
            .isEqualTo(expectedPainting)
    }

}
package adventofcode.day09

import adventofcode.computer.loadProgram
import adventofcode.computer.runProgramWithInput


fun main() {
    val program = loadProgram("./input/day09.txt")
    println("Part 1: ${runProgramWithInput(program, 1L)}")
    println("Part 2: ${runProgramWithInput(program, 2L)}")
}
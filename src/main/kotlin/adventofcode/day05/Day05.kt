package adventofcode.day05

import adventofcode.computer.loadProgram
import adventofcode.computer.runProgramWithInput


fun main() {
    val program = loadProgram("./input/day05.txt")
    println("Part 1: ${runProgramWithInput(program, 1)}")
    println("Part 2: ${runProgramWithInput(program, 5)}")
}
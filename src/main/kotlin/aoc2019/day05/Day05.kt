package aoc2019.day05

import aoc2019.computer.loadProgram
import aoc2019.computer.runProgramWithInput


fun main() {
    val program = loadProgram("./input/day05.txt")
    println("Part 1: ${runProgramWithInput(program, 1)}")
    println("Part 2: ${runProgramWithInput(program, 5)}")
}
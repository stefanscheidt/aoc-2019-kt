package aoc2019.day09

import aoc2019.computer.loadProgram
import aoc2019.computer.runProgramWithInput


fun main() {
    val program = loadProgram("./input/day09.txt")
    println("Part 1: ${runProgramWithInput(program, 1L)}")
    println("Part 2: ${runProgramWithInput(program, 2L)}")
}
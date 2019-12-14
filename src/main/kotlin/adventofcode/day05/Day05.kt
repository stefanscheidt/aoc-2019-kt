package adventofcode.day05

import adventofcode.computer.Computer
import adventofcode.computer.loadProgram


fun main() {
    val program = loadProgram("./input/day05.txt")
    val computer = Computer(program, listOf(1)).apply {
        runProgramm()
    }
    println(computer.output)
}
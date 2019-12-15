package adventofcode.day02

import adventofcode.computer.Computer
import adventofcode.computer.Program
import adventofcode.computer.loadProgram

fun findPhrase(program: Program, goal: Int): String? {
    val args = (0..99).flatMap { p1 -> (0..99).map { p2 -> Pair(p1, p2) } }
    return args.firstOrNull { pair ->
        val computer = Computer(program).also {
            it[1] = pair.first
            it[2] = pair.second
        }
        computer.runProgram() == goal
    }?.let {
        "${100 * it.first + it.second}"
    }
}

// --- Main ---

fun main() {
    val program = loadProgram("./input/day02.txt")
    val goal = 19690720
    println(findPhrase(program, goal))
}

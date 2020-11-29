package aoc2019.day02

import aoc2019.computer.ListComputer
import aoc2019.computer.Program
import aoc2019.computer.loadProgram

fun findPhrase(program: Program, goal: Long): String? {
    val args = (0L..99L).flatMap { p1 -> (0L..99L).map { p2 -> Pair(p1, p2) } }
    return args.firstOrNull { pair ->
        val computer = ListComputer(program).also {
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
    val goal = 19690720L
    println(findPhrase(program, goal))
}

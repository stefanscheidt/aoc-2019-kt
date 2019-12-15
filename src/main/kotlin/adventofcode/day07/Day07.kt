package adventofcode.day07

import adventofcode.computer.Program
import adventofcode.computer.loadProgram
import adventofcode.computer.runProgramWithInput


fun <T> allPermutationsOf(xs: List<T>): Set<List<T>> =
    if (xs.size <= 1)
        setOf(xs)
    else
        xs.flatMap { i ->
            allPermutationsOf(xs - i).map { p ->
                p + i
            }
        }.toSet()

typealias Settings = List<Int>

fun amplify(program: Program, settings: Settings): Int {
    fun nextOutput(input: Pair<Int, Int>) = runProgramWithInput(program, input.first, input.second).first()

    val settingsIterator = (settings + 0).iterator()
    return generateSequence(settingsIterator.next() to 0) {
        if (settingsIterator.hasNext())
            settingsIterator.next() to nextOutput(it)
        else
            null
    }
        .map { it.second }
        .last()
}

data class AmplifyOutput(
    val value: Int,
    val settings: Settings
)

fun optimalOutput(program: Program): AmplifyOutput? =
    allPermutationsOf(listOf(0, 1, 2, 3, 4))
        .map { settings ->
            AmplifyOutput(amplify(program, settings), settings)
        }
        .maxBy { it.value }

fun main() {
    val optimalOutput = optimalOutput(loadProgram("./input/day07.txt"))
    println("Part 1: $optimalOutput")
}
package adventofcode.day07

import adventofcode.computer.*


fun main() {
    val program = loadProgram("./input/day07.txt")
    println("Part 1: ${optimalOutput(program)}")
    println("Part 2: ${optimalOutputWithFeedback(program)}")
}

// --- Types ---

typealias Settings = List<Int>

data class AmplifyOutput(
    val value: Int,
    val settings: Settings
)

// --- Single Pass Amplify ---

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

fun optimalOutput(program: Program): AmplifyOutput? =
    allPermutationsOf(listOf(0, 1, 2, 3, 4))
        .map { settings ->
            AmplifyOutput(amplify(program, settings), settings)
        }
        .maxBy { it.value }

// --- Amplify with Feedback Loop ---

private fun amplifyCluster(program: Program, settings: Settings): QueueComputerCluster {
    fun queueWith(input: Int, nextDevice: QueueInputOutputDevice? = null) =
        QueueInputOutputDevice(nextDevice).apply { putInput(input) }

    val linkedInputOutputDevices = settings.dropLast(1).reversed()
        .fold(listOf(queueWith(input = settings.last()))) { acc, setting ->
            acc + queueWith(input = setting, nextDevice = acc.last())
        }
        .reversed()

    val computers = linkedInputOutputDevices
        .map { QueueComputer(program, it) }

    return QueueComputerCluster(computers)
}

fun amplifyWithFeedback(program: Program, settings: Settings): Int {
    val cluster = amplifyCluster(program, settings).apply { runAsync() }

    cluster.putInput(0)
    var output = cluster.takeOutput()

    // Feedback Loop
    while (cluster.running) {
        cluster.putInput(output)
        output = cluster.takeOutput()
    }

    return output
}

fun optimalOutputWithFeedback(program: Program): AmplifyOutput? =
    allPermutationsOf(listOf(5, 6, 7, 8, 9))
        .map { settings ->
            AmplifyOutput(amplifyWithFeedback(program, settings), settings)
        }
        .maxBy { it.value }

// --- Utilities ---

internal fun <T> allPermutationsOf(xs: List<T>): Set<List<T>> =
    if (xs.size <= 1)
        setOf(xs)
    else
        xs.flatMap { i ->
            allPermutationsOf(xs - i).map { p -> p + i }
        }.toSet()

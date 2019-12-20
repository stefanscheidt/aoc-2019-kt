package adventofcode.computer

import adventofcode.computer.Operation.HALT
import adventofcode.util.log
import java.io.File
import java.util.*

// --- Computer ---

typealias Program = List<Int>

sealed class Computer(
    program: Program
) : Runnable {
    private val ram = program.toMutableList()

    var running = false
        private set

    abstract val inputOutputDevice: InputOutputDevice

    val output: List<Int>
        get() = inputOutputDevice.output

    fun runProgram(): Int {
        log(this, "run programm")
        running = true
        var ptr = 0
        do {
            val header = ram[ptr++]
            val opcode = header % 100
            val modes = parameterModes(header / 100)
            val operation = operations[opcode] ?: throw IllegalArgumentException("unknown opcode")
            val args = args(operation, modes, ptr).also { ptr += operation.arity }
            val result = operation(args, inputOutputDevice)
            if (result.instructionPointer != null)
                ptr = result.instructionPointer
            else
                result.value?.let { ram[ram[ptr++]] = it }
        } while (operation != HALT)
        log(this, "halt programm")
        running = false
        return ram[0]
    }

    override fun run() {
        runProgram()
    }

    fun dumpMemory(): List<Int> =
        ram.toList()

    operator fun get(index: Int) =
        ram[index]

    operator fun set(index: Int, value: Int) {
        ram[index] = value
    }

    private fun args(operation: Operation, parameterModes: Iterator<Int>, pointer: Int): List<Int> {
        if (operation.arity == 0) return emptyList()
        return ram.slice(pointer until pointer + operation.arity)
            .map { if (parameterModes.next() == 0) ram[it] else it }
    }

}

class ListComputer(program: Program, input: List<Int> = emptyList()) : Computer(program) {

    override val inputOutputDevice: InputOutputDevice =
        ListInputOutputDevice(input)

}

class QueueComputer(
    program: Program,
    private val queueInOut: QueueInputOutputDevice = QueueInputOutputDevice()
) : Computer(program), InputOutputQueue by queueInOut {

    override val inputOutputDevice = queueInOut

    fun runAsync(): QueueComputer {
        Thread(this, "program runner ${UUID.randomUUID()}").start()
        return this
    }

}

class QueueComputerCluster(val nodes: List<QueueComputer>) : InputOutputQueue {

    val running: Boolean
        get() = nodes.all { it.running }

    fun runAsync() {
        nodes.forEach { it.runAsync() }
    }

    override fun putInput(i: Int) {
        nodes.first().putInput(i)
    }

    override fun takeOutput(): Int =
        nodes.last().takeOutput()

}

internal fun parameterModes(pmcode: Int): Iterator<Int> =
    generateSequence(Pair(pmcode % 10, pmcode / 10)) { Pair(it.second % 10, it.second / 10) }
        .map { it.first }
        .iterator()

// --- Utilities ---

fun loadProgram(fileName: String): Program {
    return File(fileName)
        .readLines()[0]
        .split(",")
        .map { it.toInt() }
}

fun runProgramWithInput(program: Program, vararg input: Int): List<Int> =
    ListComputer(program, input.toList()).apply { runProgram() }.output


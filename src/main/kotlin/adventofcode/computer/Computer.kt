package adventofcode.computer

import adventofcode.computer.Operation.HALT
import adventofcode.util.log
import java.io.File
import java.util.*

// --- Computer ---

private const val ACCESS_MODE_POSTION = 0L
private const val ACCESS_MODE_ABSOLUTE = 1L
private const val ACCESS_MODE_RELATIVE = 2L

private const val PARAM_MODE_POSITION = 0L
private const val PARAM_MODE_ABSOLUTE = 1L
private const val PARAM_MODE_RELATIV = 2L

typealias Program = List<Long>

class Memory(program: Program) {

    private val ram = program
        .withIndex()
        .associate { it.index.toLong() to it.value }
        .toMutableMap()

    fun read(address: Long, accessMode: Long = ACCESS_MODE_ABSOLUTE, base: Long = 0L): Long {
        val position = ram[address] ?: 0L
        return when (accessMode) {
            ACCESS_MODE_ABSOLUTE -> position
            ACCESS_MODE_POSTION  -> ram[position] ?: 0L
            ACCESS_MODE_RELATIVE -> ram[base + position] ?: 0L
            else                 -> throw IllegalArgumentException("unknown access mode $accessMode")
        }
    }

    fun write(
        address: Long, value: Long,
        accessMode: Long = ACCESS_MODE_ABSOLUTE, base: Long = 0L
    ) {
        val position = ram[address] ?: 0L
        when (accessMode) {
            ACCESS_MODE_ABSOLUTE -> ram[address] = value
            ACCESS_MODE_POSTION  -> ram[position] = value
            ACCESS_MODE_RELATIVE -> ram[base + position] = value
            else                 -> throw IllegalArgumentException("unknown access mode $accessMode")
        }
    }

    fun dump(): List<Long> =
        ram.values.toList()

}

sealed class Computer(
    program: Program
) : Runnable {

    private val ram = program.withIndex().associate { it.index.toLong() to it.value }.toMutableMap()

    var running = false
        private set

    abstract val inputOutputDevice: InputOutputDevice

    val output: List<Long>
        get() = inputOutputDevice.output

    fun runProgram(): Long {
        log(this, "run programm")
        running = true
        var ptr = 0L
        var base = 0L
        do {
            val header = ram[ptr++] ?: 0 // read
            val opcode = header % 100
            val operation = operations[opcode] ?: throw IllegalArgumentException("unknown opcode")
            val arity = operation.arity
            val accessModes = accessModes(header / 100L).take(arity).toList()
            val args = args(arity, accessModes, ptr, base).also { ptr += arity }
            val result = operation(args, inputOutputDevice).apply { base += relativeBaseAdjustment }
            if (result.instructionPointer != null)
                ptr = result.instructionPointer
            else
                result.value?.let { ram[ram[ptr++] ?: 0] = it }
        } while (operation != HALT)
        log(this, "halt programm")
        running = false
        return ram[0] ?: 0
    }

    override fun run() {
        runProgram()
    }

    fun dumpMemory(): List<Long> =
        ram.values.toList()

    operator fun get(index: Long) =
        ram[index] ?: 0

    operator fun set(index: Long, value: Long) {
        ram[index] = value
    }

    private fun args(arity: Int, accessModes: List<Long>, pointer: Pointer, base: Long): List<Long> {
        if (arity == 0) return emptyList()
        val addresses = pointer until pointer + arity.toLong()
        return addresses.zip(accessModes.take(arity).toList())
            .map { (address, accessMode) ->
                val position = ram[address] ?: 0L
                when (accessMode) {
                    PARAM_MODE_ABSOLUTE -> position
                    PARAM_MODE_POSITION -> ram[position]
                    PARAM_MODE_RELATIV  -> ram[base + position]
                    else                -> throw IllegalArgumentException("unknown parameter mode $accessMode")
                } ?: 0
            }
    }

}

class ListComputer(program: Program, input: List<Long> = emptyList()) : Computer(program) {

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

    override fun putInput(i: Long) {
        nodes.first().putInput(i)
    }

    override fun takeOutput(): Long =
        nodes.last().takeOutput()

}

internal fun accessModes(pmcode: Long): Sequence<Long> =
    generateSequence(Pair(pmcode % 10, pmcode / 10)) { Pair(it.second % 10, it.second / 10) }
        .map { it.first }

// --- Utilities ---

fun loadProgram(fileName: String): Program {
    return File(fileName)
        .readLines()[0]
        .split(",")
        .map { it.toLong() }
}

fun runProgramWithInput(program: Program, vararg input: Long): List<Long> =
    ListComputer(program, input.toList()).apply { runProgram() }.output


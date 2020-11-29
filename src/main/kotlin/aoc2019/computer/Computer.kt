package aoc2019.computer

import aoc2019.computer.Operation.HALT
import aoc2019.util.log
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

// --- Computer ---

private const val ACCESS_MODE_POSTION = 0L
private const val ACCESS_MODE_ABSOLUTE = 1L
private const val ACCESS_MODE_RELATIVE = 2L

typealias Program = List<Long>

class Memory(program: Program) {

    val ram = program
        .withIndex()
        .associate { it.index.toLong() to it.value }
        .toMutableMap()

    fun read(address: Long, accessMode: Long = ACCESS_MODE_ABSOLUTE, base: Long = 0L): Long {
        val position = ram[address] ?: 0L
        return when (accessMode) {
            ACCESS_MODE_ABSOLUTE -> position
            ACCESS_MODE_POSTION -> ram[position] ?: 0L
            ACCESS_MODE_RELATIVE -> ram[base + position] ?: 0L
            else -> throw IllegalArgumentException("unknown access mode $accessMode")
        }
    }

    fun write(
        address: Long, value: Long,
        accessMode: Long = ACCESS_MODE_ABSOLUTE, base: Long = 0L
    ) {
        val position = ram[address] ?: 0L
        when (accessMode) {
            ACCESS_MODE_ABSOLUTE -> ram[address] = value
            ACCESS_MODE_POSTION -> ram[position] = value
            ACCESS_MODE_RELATIVE -> ram[base + position] = value
            else -> throw IllegalArgumentException("unknown access mode $accessMode")
        }
    }

    fun dump(): List<Long> =
        ram.values.toList()

}

sealed class Computer(
    program: Program
) : Runnable {

    private val memory = Memory(program)

    var running = false
        private set

    abstract val inputOutputDevice: InputOutputDevice

    val output: List<Long>
        get() = inputOutputDevice.output

    fun runProgram(): Long {
        running = true
        var base = 0L
        var ptr = 0L
        do {
            log(this, "B: $base IP: $ptr, Mem: ${memory.dump()}")
            val header = memory.read(ptr++)
            val opcode = header % 100
            val operation = operations[opcode] ?: throw IllegalArgumentException("unknown opcode")
            val arity = operation.arity
            val accessModes = accessModes(header / 100L, arity + 1)
            val args = args(arity, accessModes, ptr, base).also { ptr += arity }
            log(this, "Op: $operation, AM: $accessModes, Args: $args")
            val result = operation(args, inputOutputDevice).apply { base += relativeBaseAdjustment }
            if (result.instructionPointer != null) {
                ptr = result.instructionPointer
            } else {
                result.value?.let { memory.write(ptr++, it, accessModes.last(), base) }
            }
            log(this, "Res: $result")
        } while (operation != HALT)
        running = false
        return memory.read(0)
    }

    override fun run() {
        runProgram()
    }

    fun dumpMemory(): List<Long> =
        memory.dump()

    operator fun get(index: Long) =
        memory.read(index)

    operator fun set(index: Long, value: Long) {
        memory.write(index, value)
    }

    private fun args(arity: Int, accessModes: List<Long>, pointer: Pointer, base: Long): List<Long> {
        if (arity == 0) return emptyList()
        val addresses = pointer until pointer + arity.toLong()
        return addresses.zip(accessModes)
            .map { (address, accessMode) ->
                memory.read(address, accessMode, base)
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

    override fun pollOutput(timeout: Long, unit: TimeUnit): Long? =
        nodes.last().pollOutput(timeout, unit)

}

internal fun accessModes(code: Long, count: Int): List<Long> =
    generateSequence(Pair(code % 10, code / 10)) { Pair(it.second % 10, it.second / 10) }
        .map { it.first }
        .take(count).toList()

// --- Utilities ---

fun loadProgram(fileName: String): Program {
    return File(fileName)
        .readLines()[0]
        .split(",")
        .map { it.toLong() }
}

fun runProgramWithInput(program: Program, vararg input: Long): List<Long> =
    ListComputer(program, input.toList()).apply { runProgram() }.output


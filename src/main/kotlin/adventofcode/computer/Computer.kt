package adventofcode.computer

import adventofcode.computer.Operation.HALT
import adventofcode.util.log
import java.io.File
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import kotlin.collections.ArrayList

// --- Operations ---

typealias Opcode = Int

typealias Pointer = Int

data class OperationResult(val value: Int?, val instructionPointer: Pointer? = null)

enum class Operation(val opcode: Opcode, val arity: Int) {
    ADD(1, 2) {
        override fun eval(args: List<Int>, inOut: InputOutputDevice): OperationResult =
            OperationResult(args[0] + args[1])
    },
    MULT(2, 2) {
        override fun eval(args: List<Int>, inOut: InputOutputDevice): OperationResult =
            OperationResult(args[0] * args[1])
    },
    READ(3, 0) {
        override fun eval(args: List<Int>, inOut: InputOutputDevice): OperationResult =
            OperationResult(inOut.nextInt())
                .also { log(this, "read input ${it.value}") }
    },
    WRITE(4, 1) {
        override fun eval(args: List<Int>, inOut: InputOutputDevice): OperationResult {
            log(this, "write output ${args[0]}")
            inOut.writeInt(args[0])
            return OperationResult(null)
        }
    },
    JUMP_TRUE(5, 2) {
        override fun eval(args: List<Int>, inOut: InputOutputDevice): OperationResult =
            if (args[0] != 0) OperationResult(null, args[1]) else OperationResult(null)
    },
    JUMP_FALSE(6, 2) {
        override fun eval(args: List<Int>, inOut: InputOutputDevice): OperationResult =
            if (args[0] == 0) OperationResult(null, args[1]) else OperationResult(null)
    },
    LESS_THAN(7, 2) {
        override fun eval(args: List<Int>, inOut: InputOutputDevice): OperationResult =
            OperationResult(if (args[0] < args[1]) 1 else 0)
    },
    EQUALS(8, 2) {
        override fun eval(args: List<Int>, inOut: InputOutputDevice): OperationResult =
            OperationResult(if (args[0] == args[1]) 1 else 0)
    },
    HALT(99, 0) {
        override fun eval(args: List<Int>, inOut: InputOutputDevice): OperationResult =
            OperationResult(null)
    };

    operator fun invoke(args: List<Int>, inOut: InputOutputDevice): OperationResult {
        if (args.size != arity) throw IllegalArgumentException("wrong number of arguments")
        return eval(args, inOut)
    }

    protected abstract fun eval(args: List<Int>, inOut: InputOutputDevice): OperationResult

}

private val operations: Map<Opcode, Operation> =
    Operation.values().associateBy { it.opcode }

// --- Parameter Modes ---

internal fun parameterModes(pmcode: Int): Iterator<Int> =
    generateSequence(Pair(pmcode % 10, pmcode / 10)) { Pair(it.second % 10, it.second / 10) }
        .map { it.first }
        .iterator()

// --- Input Output Devices ---

interface InputOutputDevice {
    fun nextInt(): Int
    fun writeInt(i: Int)
    val output: List<Int>
}

class ListInputOutputDevice(input: List<Int> = emptyList()) : InputOutputDevice {

    private val inputStream = input.iterator()
    private val outputStream = mutableListOf<Int>()

    override val output: List<Int>
        get() = outputStream.toList()

    override fun nextInt(): Int =
        inputStream.next()

    override fun writeInt(i: Int) {
        outputStream.add(i)
    }

}

interface IntQueue {
    fun putInput(i: Int)
    fun takeOutput(): Int
}

class QueueInputOutputDevice(private val nextDevice: QueueInputOutputDevice? = null) : InputOutputDevice, IntQueue {

    private val inputQueue: BlockingQueue<Int> =
        ArrayBlockingQueue<Int>(256)
    private val outputQueue: BlockingQueue<Int> =
        ArrayBlockingQueue<Int>(256)
    private val outputStream: MutableList<Int> =
        ArrayList()

    override val output: List<Int>
        get() = outputStream.toList()

    override fun nextInt(): Int =
        inputQueue.take()
            .also { log(this, "took output $it") }

    override fun writeInt(i: Int) {
        log(this, "put input $i")
        outputQueue.put(i)
        outputStream.add(i)
        nextDevice?.putInput(i)
    }

    override fun putInput(i: Int) {
        log(this, "put input $i")
        inputQueue.put(i)
    }

    override fun takeOutput(): Int =
        outputQueue.take()
            .also { log(this, "took output $it") }

}

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
) : Computer(program), IntQueue by queueInOut {

    override val inputOutputDevice = queueInOut

    fun runAsync(): QueueComputer {
        Thread(this, "program runner ${UUID.randomUUID()}").start()
        return this
    }

}

class QueueComputerCluster(val nodes: List<QueueComputer>) : IntQueue {

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

// --- Utilities ---

fun loadProgram(fileName: String): Program {
    return File(fileName)
        .readLines()[0]
        .split(",")
        .map { it.toInt() }
}

fun runProgramWithInput(program: Program, vararg input: Int): List<Int> =
    ListComputer(program, input.toList()).apply { runProgram() }.output


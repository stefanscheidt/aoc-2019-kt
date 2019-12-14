package adventofcode.computer

import adventofcode.computer.Operation.HALT
import java.io.File

// --- Operations ---

typealias Opcode = Int

data class Result(val value: Int?, val pointer: Int? = null)

enum class Operation(val opcode: Opcode, val arity: Int) {
    ADD(1, 2) {
        override fun eval(args: List<Int>, inOut: InputOutputDevice): Result =
            Result(args[0] + args[1])
    },
    MULT(2, 2) {
        override fun eval(args: List<Int>, inOut: InputOutputDevice): Result =
            Result(args[0] * args[1])
    },
    READ(3, 0) {
        override fun eval(args: List<Int>, inOut: InputOutputDevice): Result =
            Result(inOut.nextInt())
    },
    WRITE(4, 1) {
        override fun eval(args: List<Int>, inOut: InputOutputDevice): Result {
            inOut.writeInt(args[0])
            return Result(null)
        }
    },
    JUMP_TRUE(5, 2) {
        override fun eval(args: List<Int>, inOut: InputOutputDevice): Result =
            if (args[0] != 0) Result(null, args[1]) else Result(null)
    },
    JUMP_FALSE(6, 2) {
        override fun eval(args: List<Int>, inOut: InputOutputDevice): Result =
            if (args[0] == 0) Result(null, args[1]) else Result(null)
    },
    LESS_THAN(7, 2) {
        override fun eval(args: List<Int>, inOut: InputOutputDevice): Result =
            Result(if (args[0] < args[1]) 1 else 0)
    },
    EQUALS(8, 2) {
        override fun eval(args: List<Int>, inOut: InputOutputDevice): Result =
            Result(if (args[0] == args[1]) 1 else 0)
    },
    HALT(99, 0) {
        override fun eval(args: List<Int>, inOut: InputOutputDevice): Result =
            Result(null)
    };

    operator fun invoke(args: List<Int>, inOut: InputOutputDevice): Result {
        if (args.size != arity) throw IllegalArgumentException("wrong number of arguments")
        return eval(args, inOut)
    }

    protected abstract fun eval(args: List<Int>, inOut: InputOutputDevice): Result

}

private val operations: Map<Opcode, Operation> =
    Operation.values().associateBy { it.opcode }

// --- Program and Memory ---

typealias Program = List<Int>

class Memory(program: Program) {

    private val ram = program.toMutableList()

    operator fun get(index: Int) =
        ram[index]

    operator fun set(index: Int, value: Int) {
        ram[index] = value
    }

    fun dump(): List<Int> =
        ram.toList()

    fun runProgram(inOut: InputOutputDevice = InputOutputDevice()): Int {
        var ptr = 0
        do {
            val header = ram[ptr++]
            val opcode = header % 100
            val modes = parameterModes(header / 100)
            val operation = operations[opcode] ?: throw IllegalArgumentException("unknown opcode")
            val args = args(operation, modes, ptr).also { ptr += operation.arity }
            val result = operation(args, inOut)
            if (result.pointer != null)
                ptr = result.pointer
            else
                result.value?.let { ram[ram[ptr++]] = it }
        } while (operation != HALT)
        return ram[0]
    }

    private fun args(operation: Operation, parameterModes: Iterator<Int>, pointer: Int): List<Int> {
        if (operation.arity == 0) return emptyList()
        return ram.slice(pointer until pointer + operation.arity)
            .map { if (parameterModes.next() == 0) ram[it] else it }
    }

}

internal fun parameterModes(pmcode: Int): Iterator<Int> =
    generateSequence(Pair(pmcode % 10, pmcode / 10)) { Pair(it.second % 10, it.second / 10) }
        .map { it.first }
        .iterator()

// --- Utilities ---

internal fun loadProgram(fileName: String): Program {
    return File(fileName)
        .readLines()[0]
        .split(",")
        .map { it.toInt() }
}

internal fun eval(program: Program): List<Int> =
    Computer(program).run {
        runProgramm()
        dumpMemory()
    }

// --- Computer ---

class InputOutputDevice(input: List<Int> = emptyList()) {

    private val inputStream = input.iterator()
    private val outputStream = mutableListOf<Int>()

    val output: List<Int>
        get() = outputStream.toList()

    fun nextInt(): Int =
        inputStream.next()

    fun writeInt(i: Int) {
        outputStream.add(i)
    }

}

class Computer(
    program: Program,
    input: List<Int> = emptyList()
) {
    private val memory = Memory(program)
    private val inOut = InputOutputDevice(input)

    val output: List<Int>
        get() = inOut.output

    fun runProgramm(): Int =
        memory.runProgram(inOut)

    fun dumpMemory(): List<Int> =
        memory.dump()

}

fun runProgramWithInput(program: List<Int>, input: Int): List<Int> =
    Computer(program, listOf(input)).apply { runProgramm() }.output


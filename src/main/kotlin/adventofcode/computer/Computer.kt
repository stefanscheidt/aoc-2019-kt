package adventofcode.computer

import adventofcode.computer.Operation.HALT
import java.io.File

// --- Operations ---

typealias Opcode = Int

enum class Operation(val opcode: Opcode, val arity: Int) {
    ADD(1, 2) {
        override fun eval(args: List<Int>, inOut: InputOutputDevice): Int? =
            args[0] + args[1]
    },
    MULT(2, 2) {
        override fun eval(args: List<Int>, inOut: InputOutputDevice): Int? =
            args[0] * args[1]
    },
    READ(3, 0) {
        override fun eval(args: List<Int>, inOut: InputOutputDevice): Int? =
            inOut.nextInt()
    },
    WRITE(4, 1) {
        override fun eval(args: List<Int>, inOut: InputOutputDevice): Int? {
            inOut.writeInt(args[0])
            return null
        }
    },
    HALT(99, 0) {
        override fun eval(args: List<Int>, inOut: InputOutputDevice): Int? =
            null
    };

    operator fun invoke(args: List<Int>, inOut: InputOutputDevice): Int? {
        if (args.size != arity) throw IllegalArgumentException("wrong number of arguments")
        return eval(args, inOut)
    }

    protected abstract fun eval(args: List<Int>, inOut: InputOutputDevice): Int?

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
            val op = operation(ptr++)
            val args = args(op, ptr).also { ptr += op.arity }
            op(args, inOut)?.let { ram[ram[ptr++]] = it }
        } while (op != HALT)
        return ram[0]
    }

    private fun operation(pointer: Int) =
        operations[ram[pointer]] ?: throw IllegalArgumentException("unknown opcode")

    private fun args(operation: Operation, pointer: Int): List<Int> {
        if (operation.arity == 0) return emptyList()
        return ram.slice(pointer until pointer + operation.arity).map { ram[it] }
    }

}

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

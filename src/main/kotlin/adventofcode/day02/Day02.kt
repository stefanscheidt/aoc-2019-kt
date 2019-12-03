package adventofcode.day02

import adventofcode.day02.Operation.*
import java.io.File

// --- Operations ---

typealias Opcode = Int

sealed class Operation(val opcode: Opcode, val arity: Int) {

    operator fun invoke(parameters: List<Int>): Int? {
        if (parameters.size < arity) throw IllegalArgumentException("too few parameters")
        return eval(parameters)
    }

    protected abstract fun eval(params: List<Int>): Int?

    object Add : Operation(1, 2) {
        override fun eval(params: List<Int>): Int? = params[0] + params[1]
    }

    object Mult : Operation(2, 2) {
        override fun eval(params: List<Int>): Int? = params[0] * params[1]
    }

    object Halt : Operation(99, 0) {
        override fun eval(params: List<Int>): Int? = null
    }

}

private val operations: Map<Opcode, Operation> =
    mapOf(
        Add.opcode to Add,
        Mult.opcode to Mult,
        Halt.opcode to Halt
    )

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

    fun runProgram(): Int {
        var ptr = 0
        do {
            val op = operation(ptr++)
            val params = params(op, ptr).also { ptr += op.arity }
            val result = op(params)?.let { ram[ram[ptr++]] = it }
        } while (result != null)
        return ram[0]
    }

    private fun operation(pointer: Int) =
        operations[ram[pointer]] ?: throw IllegalArgumentException("unknown opcode")

    private fun params(operation: Operation, pointer: Int): List<Int> {
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

internal fun eval(values: Program): List<Int> {
    val memory = Memory(values)
    memory.runProgram()
    return memory.dump()
}

// --- Main ---

fun main() {
    val memory = Memory(loadProgram("./input/day02.txt"))
    memory[1] = 12
    memory[2] = 2

    println(memory.runProgram())
}

package adventofcode.computer

import adventofcode.util.log

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

val operations: Map<Opcode, Operation> =
    Operation.values().associateBy { it.opcode }
package adventofcode.computer

import adventofcode.util.log

// --- Operations ---

typealias Opcode = Long

typealias Pointer = Long

data class OperationResult(
    val value: Long? = null,
    val instructionPointer: Pointer? = null,
    val relativeBaseAdjustment: Long = 0
)

enum class Operation(val opcode: Opcode, val arity: Int) {
    ADD(1, 2) {
        override fun eval(args: List<Long>, inOut: InputOutputDevice): OperationResult =
            OperationResult(value = args[0] + args[1])
    },
    MULT(2, 2) {
        override fun eval(args: List<Long>, inOut: InputOutputDevice): OperationResult =
            OperationResult(value = args[0] * args[1])
    },
    READ(3, 0) {
        override fun eval(args: List<Long>, inOut: InputOutputDevice): OperationResult =
            OperationResult(value = inOut.nextValue())
                .also { log(this, "read input ${it.value}") }
    },
    WRITE(4, 1) {
        override fun eval(args: List<Long>, inOut: InputOutputDevice): OperationResult {
            log(this, "write output ${args[0]}")
            inOut.writeValue(args[0])
            return OperationResult()
        }
    },
    JUMP_TRUE(5, 2) {
        override fun eval(args: List<Long>, inOut: InputOutputDevice): OperationResult =
            if (args[0] != 0L) OperationResult(instructionPointer = args[1]) else OperationResult()
    },
    JUMP_FALSE(6, 2) {
        override fun eval(args: List<Long>, inOut: InputOutputDevice): OperationResult =
            if (args[0] == 0L) OperationResult(instructionPointer = args[1]) else OperationResult()
    },
    LESS_THAN(7, 2) {
        override fun eval(args: List<Long>, inOut: InputOutputDevice): OperationResult =
            OperationResult(value = if (args[0] < args[1]) 1L else 0L)
    },
    EQUALS(8, 2) {
        override fun eval(args: List<Long>, inOut: InputOutputDevice): OperationResult =
            OperationResult(value = if (args[0] == args[1]) 1L else 0L)
    },
    MOVE_BASE(9, 1) {
        override fun eval(args: List<Long>, inOut: InputOutputDevice): OperationResult =
            OperationResult(relativeBaseAdjustment = args[0])
    },
    HALT(99, 0) {
        override fun eval(args: List<Long>, inOut: InputOutputDevice): OperationResult =
            OperationResult()
    };

    operator fun invoke(args: List<Long>, inOut: InputOutputDevice): OperationResult {
        if (args.size != arity) throw IllegalArgumentException("wrong number of arguments")
        return eval(args, inOut)
    }

    protected abstract fun eval(args: List<Long>, inOut: InputOutputDevice): OperationResult

}

val operations: Map<Opcode, Operation> =
    Operation.values().associateBy { it.opcode }
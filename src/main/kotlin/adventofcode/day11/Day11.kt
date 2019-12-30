package adventofcode.day11

import adventofcode.common.Coordinate
import adventofcode.common.Vector
import adventofcode.common.c
import adventofcode.common.vect
import adventofcode.computer.QueueComputer
import adventofcode.computer.loadProgram
import adventofcode.day11.Direction.NORTH
import java.util.concurrent.TimeUnit

typealias Color = Long

const val BLACK = 0L
const val WHITE = 1L

enum class Direction(val vector: Vector) {
    NORTH(vect(0, -1)),
    EAST(vect(1, 0)),
    SOUTH(vect(0, 1)),
    WEST(vect(-1, 0));

    fun turnRight(): Direction =
        when (this) {
            NORTH -> EAST
            EAST  -> SOUTH
            SOUTH -> WEST
            WEST  -> NORTH
        }

    fun turnLeft(): Direction =
        when (this) {
            NORTH -> WEST
            WEST  -> SOUTH
            SOUTH -> EAST
            EAST  -> NORTH
        }

}

typealias Painting = Map<Coordinate, Color>

class Robot(private val brain: QueueComputer, initialColor: Color) {

    private var currentPosition = c(0, 0)
    private var currentDirection = NORTH

    private val paintedFields = mutableMapOf<Coordinate, Color>().apply {
        put(currentPosition, initialColor)
    }
    val painting: Painting
        get() = paintedFields.toMap()

    private val currentColor: Color
        get() = paintedFields[currentPosition] ?: BLACK

    fun start() {
        brain.runAsync()
        while (true) {
            putInput()
            val nextColor = pollOutput() ?: return
            val nextTurn = pollOutput() ?: return
            updateState(nextColor, nextTurn)
        }
    }

    private fun putInput() = brain.putInput(currentColor)

    private fun pollOutput() = brain.pollOutput(1, TimeUnit.SECONDS)

    private fun updateState(nextColor: Long, nextTurn: Long) {
        paintedFields[currentPosition] = nextColor
        when (nextTurn) {
            0L -> currentDirection = currentDirection.turnLeft()
            1L -> currentDirection = currentDirection.turnRight()
        }
        currentPosition += currentDirection.vector
    }

}

fun renderPainting(painting: Painting): String {
    val xs = painting.keys.map { it.x }
    val minX = xs.min() ?: 0
    val maxX = xs.max() ?: 0
    val ys = painting.keys.map { it.y }
    val minY = ys.min() ?: 0
    val maxY = ys.max() ?: 0
    return (minY..maxY)
        .joinToString(separator = System.lineSeparator()) { y ->
            (minX..maxX)
                .map { x -> painting[c(x, y)] ?: BLACK }
                .joinToString(separator = "") { color ->
                    if (color == BLACK) " " else "#"
                }
        }
}

fun main() {
    val program = loadProgram("./input/day11.txt")
    val brain = QueueComputer(program)

    val robot1 = Robot(brain = brain, initialColor = BLACK).apply { start() }
    println("Part 1: ${robot1.painting.size}")

    val robot2 = Robot(brain = brain, initialColor = WHITE).apply { start() }
    println("Part 2:")
    println(renderPainting(robot2.painting))
}
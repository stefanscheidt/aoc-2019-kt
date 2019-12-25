package adventofcode.day11

import adventofcode.common.Coordinate
import adventofcode.common.Vector
import adventofcode.common.c
import adventofcode.common.vect
import adventofcode.computer.QueueComputer
import adventofcode.computer.loadProgram
import adventofcode.day11.Direction.NORTH
import adventofcode.util.log
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

class Robot(private val brain: QueueComputer, private val initialColor: Color) {

    private val paintedFields = mutableMapOf<Coordinate, Color>()
    val painting: Painting
        get() = paintedFields.toMap()

    private var currentDirection = NORTH
    private var currentPosition = c(0, 0)
    private val currentColor: Color
        get() = paintedFields[currentPosition] ?: BLACK

    fun start() {
        paintedFields[currentPosition] = initialColor
        brain.putInput(currentColor)
        brain.runAsync()
        log(this, "poll next output")
        var nextColor = brain.pollOutput(1, TimeUnit.SECONDS) ?: return
        var nextTurn = brain.pollOutput(1, TimeUnit.SECONDS) ?: return
        while (brain.running) {
            paintedFields[currentPosition] = nextColor
            when (nextTurn) {
                0L -> currentDirection = currentDirection.turnLeft()
                1L -> currentDirection = currentDirection.turnRight()
            }
            currentPosition += currentDirection.vector
            log(this, "put color $currentColor from position $currentPosition")
            brain.putInput(currentColor)
            log(this, "poll next output")
            nextColor = brain.pollOutput(1, TimeUnit.SECONDS) ?: break
            nextTurn = brain.pollOutput(1, TimeUnit.SECONDS) ?: break
        }
    }

}

fun renderPainting(painting: Painting): String {
    val minX = painting.keys.map { it.x }.min() ?: 0
    val maxX = painting.keys.map { it.x }.max() ?: 0
    val minY = painting.keys.map { it.y }.min() ?: 0
    val maxY = painting.keys.map { it.y }.max() ?: 0
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
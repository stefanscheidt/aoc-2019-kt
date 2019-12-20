package adventofcode.computer

import adventofcode.util.log
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue


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

interface InputOutputQueue {
    fun putInput(i: Int)
    fun takeOutput(): Int
}

class QueueInputOutputDevice(
    private val nextDevice: QueueInputOutputDevice? = null
) : InputOutputDevice, InputOutputQueue {

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
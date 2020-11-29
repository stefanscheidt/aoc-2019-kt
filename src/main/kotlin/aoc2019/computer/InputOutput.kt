package aoc2019.computer

import aoc2019.util.log
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.TimeUnit


interface InputOutputDevice {
    fun nextValue(): Long
    fun writeValue(value: Long)
    val output: List<Long>
}

class ListInputOutputDevice(input: List<Long> = emptyList()) : InputOutputDevice {

    private val inputStream = input.iterator()
    private val outputStream = mutableListOf<Long>()

    override val output: List<Long>
        get() = outputStream.toList()

    override fun nextValue(): Long =
        inputStream.next()

    override fun writeValue(value: Long) {
        outputStream.add(value)
    }

}

interface InputOutputQueue {
    fun putInput(i: Long)
    fun takeOutput(): Long
    fun pollOutput(timeout: Long, unit: TimeUnit): Long?
}

class QueueInputOutputDevice(
    private val nextDevice: QueueInputOutputDevice? = null
) : InputOutputDevice, InputOutputQueue {

    private val inputQueue: BlockingQueue<Long> =
        ArrayBlockingQueue<Long>(256)
    private val outputQueue: BlockingQueue<Long> =
        ArrayBlockingQueue<Long>(256)
    private val outputStream: MutableList<Long> =
        ArrayList()

    override val output: List<Long>
        get() = outputStream.toList()

    override fun nextValue(): Long =
        inputQueue.take()
            .also { log(this, "provide next input value $it") }

    override fun writeValue(value: Long) {
        log(this, "write next output value $value")
        outputQueue.put(value)
        outputStream.add(value)
        nextDevice?.putInput(value)
    }

    override fun putInput(i: Long) {
        log(this, "put input $i")
        inputQueue.put(i)
    }

    override fun takeOutput(): Long =
        outputQueue.take()
            .also { log(this, "took output $it") }

    override fun pollOutput(timeout: Long, unit: TimeUnit): Long? =
        outputQueue.poll(timeout, unit)
            .also { log(this, "poll output $it") }

}
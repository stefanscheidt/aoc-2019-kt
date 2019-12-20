package adventofcode.computer

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test


class QueueInputOutputTest {

    @Test
    fun `put input and take output`() {
        val inOut = QueueInputOutputDevice()
        inOut.putInput(42)
        inOut.writeInt(inOut.nextInt())
        Assertions.assertThat(inOut.takeOutput()).isEqualTo(42)
    }

    @Test
    fun `output is put to next device`() {
        val nextDevice = QueueInputOutputDevice()
        val inOut = QueueInputOutputDevice(nextDevice)

        inOut.writeInt(42)
        Assertions.assertThat(nextDevice.nextInt()).isEqualTo(42)
    }
}
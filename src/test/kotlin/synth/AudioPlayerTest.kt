package synth

import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import org.junit.jupiter.api.Assumptions.assumeTrue
import kotlin.test.Test
import kotlin.test.assertTrue

class AudioPlayerTest {

    private fun audioAvailable(): Boolean = try {
        AudioSystem.getSourceDataLine(AudioFormat(8000f, 16, 1, true, false))
        true
    } catch (e: Exception) {
        false
    }

    @Test
    fun `plays a signal through the sound device`() {
        assumeTrue(audioAvailable(), "no audio device on this machine")
        val silence = Signal(DoubleArray(400), 8000, intArrayOf(0)) // 50 ms of silence
        assertTrue(runCatching { AudioPlayer(PcmEncoder()).play(silence) }.isSuccess)
    }
}

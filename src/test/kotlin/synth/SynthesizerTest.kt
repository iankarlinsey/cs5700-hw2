package synth

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SynthesizerTest {

    /** Records the mixed signal instead of playing it. */
    private class RecordingAudioPlayer : AudioPlayer(PcmEncoder()) {
        var received: Signal? = null
        override fun play(signal: Signal) {
            received = signal
        }
    }

    private fun songFile(content: String): String {
        val file = File.createTempFile("song", ".txt")
        file.deleteOnExit()
        file.writeText(content)
        return file.absolutePath
    }

    @Test
    fun `parses mixes and hands the mixed signal to the player`() {
        val player = RecordingAudioPlayer()
        // 2 beats at 60 bpm and 100 Hz sample rate = 200 samples
        Synthesizer(SongParser(), Mixer(), player).run(songFile("100 4 60\nsin|C4 1 C4 1|"))
        assertEquals(200, player.received?.length())
    }

    @Test
    fun `propagates parse failures to the caller`() {
        val synthesizer = Synthesizer(SongParser(), Mixer(), RecordingAudioPlayer())
        assertFailsWith<SongFormatException> { synthesizer.run("no/such/file.txt") }
    }
}

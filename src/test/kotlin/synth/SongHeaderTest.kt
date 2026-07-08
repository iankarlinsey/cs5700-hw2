package synth

import kotlin.test.Test
import kotlin.test.assertEquals

class SongHeaderTest {

    @Test
    fun `beat duration is 60 divided by tempo`() {
        val header = SongHeader(44100, 4, 120)
        assertEquals(0.5, header.beatDurationSeconds(), 1e-9)
    }

    @Test
    fun `one beat at 120 bpm and 44100 Hz is 22050 samples`() {
        val header = SongHeader(44100, 4, 120)
        assertEquals(22050, header.beatsToSamples(1.0))
    }

    @Test
    fun `exposes the beats per measure`() {
        val header = SongHeader(44100, 4, 120)
        assertEquals(4, header.getBeatsPerMeasure())
    }

    @Test
    fun `fractional beats round to the nearest sample`() {
        val header = SongHeader(44100, 4, 120)
        assertEquals(11025, header.beatsToSamples(0.5))
    }
}

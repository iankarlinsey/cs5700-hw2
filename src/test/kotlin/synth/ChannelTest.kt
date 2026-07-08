package synth

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ChannelTest {

    private val header = SongHeader(100, 4, 60) // 1 beat = 1 second = 100 samples

    @Test
    fun `signal length is the sum of the note durations`() {
        val notes = listOf(Note(440.0, 1.0, false), Note(440.0, 0.5, false))
        assertEquals(150, Channel(SineWaveform(), notes, header).render().length())
    }

    @Test
    fun `rests produce silence`() {
        val notes = listOf(Note(0.0, 1.0, true))
        val signal = Channel(SquareWaveform(), notes, header).render()
        assertTrue((0 until signal.length()).all { signal.get(it) == 0.0 })
    }

    @Test
    fun `note onsets mark where each note starts`() {
        val notes = listOf(Note(440.0, 1.0, false), Note(220.0, 2.0, false))
        assertEquals(100, Channel(SineWaveform(), notes, header).render().getNoteOnsets()[1])
    }

    @Test
    fun `empty note list renders an empty signal`() {
        assertEquals(0, Channel(SineWaveform(), emptyList(), header).render().length())
    }

    @Test
    fun `delegates sample values to the waveform strategy`() {
        val notes = listOf(Note(1.0, 1.0, false))
        val signal = Channel(SquareWaveform(), notes, header).render()
        assertEquals(1.0, signal.get(10), 1e-9) // t=.1s, first half of a 1 Hz square period
    }
}

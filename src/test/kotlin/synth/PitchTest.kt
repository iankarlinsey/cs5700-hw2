package synth

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PitchTest {

    @Test
    fun `A4 is 440 Hz`() {
        assertEquals(440.0, Pitch.frequencyOf("A4"), 0.01)
    }

    @Test
    fun `C4 is middle C`() {
        assertEquals(261.63, Pitch.frequencyOf("C4"), 0.01)
    }

    @Test
    fun `flat lowers the pitch by a semitone`() {
        assertEquals(233.08, Pitch.frequencyOf("Bb3"), 0.01)
    }

    @Test
    fun `sharp raises the pitch by a semitone`() {
        assertEquals(277.18, Pitch.frequencyOf("C#4"), 0.01)
    }

    @Test
    fun `octave doubles the frequency`() {
        assertEquals(880.0, Pitch.frequencyOf("A5"), 0.01)
    }

    @Test
    fun `invalid letter is rejected`() {
        assertFailsWith<IllegalArgumentException> { Pitch.frequencyOf("H4") }
    }

    @Test
    fun `missing octave is rejected`() {
        assertFailsWith<IllegalArgumentException> { Pitch.frequencyOf("C") }
    }

    @Test
    fun `non numeric octave is rejected`() {
        assertFailsWith<IllegalArgumentException> { Pitch.frequencyOf("Cx") }
    }
}

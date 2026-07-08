package synth

import kotlin.test.Test
import kotlin.test.assertEquals

class AdsEffectTest {

    // sampleRate 100 -> sample i is at i/100 seconds after its note's onset.
    private fun constantSignal(size: Int, onsets: IntArray) =
        FakeSoundSource(DoubleArray(size) { 1.0 }, sampleRate = 100, noteOnsets = onsets)

    @Test
    fun `attack ramps up from zero`() {
        val effect = AdsEffect(constantSignal(100, intArrayOf(0)), 0.1, 0.2, 0.5)
        assertEquals(0.5, effect.render().get(5), 1e-9) // t=.05, halfway through attack
    }

    @Test
    fun `decay ramps down toward the sustain level`() {
        val effect = AdsEffect(constantSignal(100, intArrayOf(0)), 0.1, 0.2, 0.5)
        assertEquals(0.75, effect.render().get(15), 1e-9) // t=.15, halfway through decay
    }

    @Test
    fun `sustain level holds after the decay ends`() {
        val effect = AdsEffect(constantSignal(100, intArrayOf(0)), 0.1, 0.2, 0.5)
        assertEquals(0.5, effect.render().get(50), 1e-9)
    }

    @Test
    fun `envelope restarts at each note onset`() {
        val effect = AdsEffect(constantSignal(100, intArrayOf(0, 50)), 0.1, 0.2, 0.5)
        assertEquals(0.5, effect.render().get(55), 1e-9) // t=.05 into the second note
    }

    @Test
    fun `zero attack skips straight to the decay`() {
        val effect = AdsEffect(constantSignal(100, intArrayOf(0)), 0.0, 0.1, 0.0)
        assertEquals(0.5, effect.render().get(5), 1e-9) // t=.05, halfway through decay to 0
    }
}

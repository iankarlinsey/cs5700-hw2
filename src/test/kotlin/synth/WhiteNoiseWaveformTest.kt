package synth

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WhiteNoiseWaveformTest {

    @Test
    fun `samples stay within minus one to one`() {
        val noise = WhiteNoiseWaveform(Random(42))
        assertTrue((0 until 1000).all { noise.sample(440.0, it / 44100.0) in -1.0..1.0 })
    }

    @Test
    fun `default random source also stays within range`() {
        val noise = WhiteNoiseWaveform()
        assertTrue((0 until 100).all { noise.sample(440.0, it / 44100.0) in -1.0..1.0 })
    }

    @Test
    fun `is deterministic for a seeded random source`() {
        assertEquals(
            WhiteNoiseWaveform(Random(7)).sample(440.0, 0.0),
            WhiteNoiseWaveform(Random(7)).sample(440.0, 0.0),
            1e-9,
        )
    }
}

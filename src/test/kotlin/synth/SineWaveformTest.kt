package synth

import kotlin.test.Test
import kotlin.test.assertEquals

class SineWaveformTest {

    @Test
    fun `starts at zero`() {
        assertEquals(0.0, SineWaveform().sample(440.0, 0.0), 1e-9)
    }

    @Test
    fun `peaks at a quarter period`() {
        assertEquals(1.0, SineWaveform().sample(1.0, 0.25), 1e-9)
    }

    @Test
    fun `is negative in the second half of the period`() {
        assertEquals(-1.0, SineWaveform().sample(1.0, 0.75), 1e-9)
    }
}

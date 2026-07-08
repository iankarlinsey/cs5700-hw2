package synth

import kotlin.test.Test
import kotlin.test.assertEquals

class SawWaveformTest {

    @Test
    fun `starts each period at minus one`() {
        assertEquals(-1.0, SawWaveform().sample(1.0, 0.0), 1e-9)
    }

    @Test
    fun `crosses zero at the middle of the period`() {
        assertEquals(0.0, SawWaveform().sample(1.0, 0.5), 1e-9)
    }

    @Test
    fun `ramps linearly toward one`() {
        assertEquals(0.5, SawWaveform().sample(1.0, 0.75), 1e-9)
    }

    @Test
    fun `negative time wraps into the period`() {
        assertEquals(0.5, SawWaveform().sample(1.0, -0.25), 1e-9) // phase .75
    }
}

package synth

import kotlin.test.Test
import kotlin.test.assertEquals

class SquareWaveformTest {

    @Test
    fun `is high in the first half of the period`() {
        assertEquals(1.0, SquareWaveform().sample(1.0, 0.25), 1e-9)
    }

    @Test
    fun `is low in the second half of the period`() {
        assertEquals(-1.0, SquareWaveform().sample(1.0, 0.75), 1e-9)
    }

    @Test
    fun `repeats each period`() {
        assertEquals(1.0, SquareWaveform().sample(1.0, 1.25), 1e-9)
    }

    @Test
    fun `negative time wraps into the period`() {
        assertEquals(-1.0, SquareWaveform().sample(1.0, -0.25), 1e-9) // phase .75
    }
}

package synth

import kotlin.test.Test
import kotlin.test.assertEquals

class PcmEncoderTest {

    private fun signalOf(vararg samples: Double) = Signal(samples.copyOf(), 44100, intArrayOf(0))

    @Test
    fun `produces two bytes per sample`() {
        assertEquals(6, PcmEncoder().encode(signalOf(0.0, 0.5, 1.0)).size)
    }

    @Test
    fun `full scale maps to Short MAX_VALUE little endian low byte`() {
        assertEquals(0xFF.toByte(), PcmEncoder().encode(signalOf(1.0))[0])
    }

    @Test
    fun `full scale maps to Short MAX_VALUE little endian high byte`() {
        assertEquals(0x7F.toByte(), PcmEncoder().encode(signalOf(1.0))[1])
    }

    @Test
    fun `silence maps to zero bytes`() {
        assertEquals(0.toByte(), PcmEncoder().encode(signalOf(0.0))[0])
    }

    @Test
    fun `samples beyond full scale are clamped`() {
        assertEquals(0x7F.toByte(), PcmEncoder().encode(signalOf(2.0))[1])
    }
}

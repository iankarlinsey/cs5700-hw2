package synth

import kotlin.test.Test
import kotlin.test.assertEquals

class MixerTest {

    private val header = SongHeader(100, 4, 60)

    @Test
    fun `averages the samples of all channels`() {
        val a = FakeSoundSource(doubleArrayOf(0.2))
        val b = FakeSoundSource(doubleArrayOf(0.3))
        assertEquals(0.25, Mixer().mix(listOf(a, b), header).get(0), 1e-9)
    }

    @Test
    fun `pads shorter channels to the longest`() {
        val short = FakeSoundSource(doubleArrayOf(0.2))
        val long = FakeSoundSource(doubleArrayOf(0.1, 0.4))
        assertEquals(2, Mixer().mix(listOf(short, long), header).length())
    }

    @Test
    fun `normalizes when the sum exceeds full scale`() {
        val a = FakeSoundSource(doubleArrayOf(1.0))
        val b = FakeSoundSource(doubleArrayOf(1.0))
        assertEquals(1.0, Mixer().mix(listOf(a, b), header).get(0), 1e-9)
    }

    @Test
    fun `clamps a channel that overshoots full scale`() {
        val hot = FakeSoundSource(doubleArrayOf(1.5)) // e.g. vol$1.5
        assertEquals(1.0, Mixer().mix(listOf(hot), header).get(0), 1e-9)
    }

    @Test
    fun `clamps negative overshoot symmetrically`() {
        val hot = FakeSoundSource(doubleArrayOf(-2.0))
        assertEquals(-1.0, Mixer().mix(listOf(hot), header).get(0), 1e-9)
    }

    @Test
    fun `finds the peak when the loudest sample comes first`() {
        val fading = FakeSoundSource(doubleArrayOf(0.4, 0.1))
        assertEquals(0.4, Mixer().mix(listOf(fading), header).get(0), 1e-9)
    }

    @Test
    fun `mixing no channels produces an empty signal`() {
        assertEquals(0, Mixer().mix(emptyList(), header).length())
    }

    @Test
    fun `does not boost quiet audio to full scale`() {
        val quiet = FakeSoundSource(doubleArrayOf(0.3))
        assertEquals(0.3, Mixer().mix(listOf(quiet), header).get(0), 1e-9)
    }
}

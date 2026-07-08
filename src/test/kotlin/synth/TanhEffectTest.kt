package synth

import kotlin.math.tanh
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TanhEffectTest {

    @Test
    fun `applies tanh of drive times the sample`() {
        val effect = TanhEffect(FakeSoundSource(doubleArrayOf(0.5)), 5.0)
        assertEquals(tanh(2.5), effect.render().get(0), 1e-9)
    }

    @Test
    fun `keeps output within minus one to one`() {
        val effect = TanhEffect(FakeSoundSource(doubleArrayOf(1.0)), 100.0)
        assertTrue(effect.render().get(0) <= 1.0)
    }

    @Test
    fun `leaves zero samples at zero`() {
        val effect = TanhEffect(FakeSoundSource(doubleArrayOf(0.0)), 5.0)
        assertEquals(0.0, effect.render().get(0), 1e-9)
    }
}

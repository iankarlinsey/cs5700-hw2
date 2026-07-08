package synth

import kotlin.test.Test
import kotlin.test.assertEquals

class ClipEffectTest {

    @Test
    fun `clips samples above the threshold`() {
        val effect = ClipEffect(FakeSoundSource(doubleArrayOf(0.9)), 0.3)
        assertEquals(0.3, effect.render().get(0), 1e-9)
    }

    @Test
    fun `clips samples below the negative threshold`() {
        val effect = ClipEffect(FakeSoundSource(doubleArrayOf(-0.9)), 0.3)
        assertEquals(-0.3, effect.render().get(0), 1e-9)
    }

    @Test
    fun `leaves samples inside the threshold unchanged`() {
        val effect = ClipEffect(FakeSoundSource(doubleArrayOf(0.2)), 0.3)
        assertEquals(0.2, effect.render().get(0), 1e-9)
    }
}

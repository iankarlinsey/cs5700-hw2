package synth

import kotlin.test.Test
import kotlin.test.assertEquals

class VolumeEffectTest {

    @Test
    fun `scales samples by the level`() {
        val effect = VolumeEffect(FakeSoundSource(doubleArrayOf(0.5)), 0.4)
        assertEquals(0.2, effect.render().get(0), 1e-9)
    }

    @Test
    fun `scales negative samples symmetrically`() {
        val effect = VolumeEffect(FakeSoundSource(doubleArrayOf(-1.0)), 0.4)
        assertEquals(-0.4, effect.render().get(0), 1e-9)
    }
}

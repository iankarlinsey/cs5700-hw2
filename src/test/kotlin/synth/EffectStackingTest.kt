package synth

import kotlin.test.Test
import kotlin.test.assertEquals

class EffectStackingTest {

    @Test
    fun `decorators apply from the innermost wrap outward`() {
        // clip(vol(0.5 * 2.0)) -> clip(1.0) at threshold .8 -> .8
        val stacked = ClipEffect(VolumeEffect(FakeSoundSource(doubleArrayOf(0.5)), 2.0), 0.8)
        assertEquals(0.8, stacked.render().get(0), 1e-9)
    }

    @Test
    fun `stacking order changes the result`() {
        // vol(clip(0.5 at .8)) -> vol(0.5 * 2.0) -> 1.0
        val stacked = VolumeEffect(ClipEffect(FakeSoundSource(doubleArrayOf(0.5)), 0.8), 2.0)
        assertEquals(1.0, stacked.render().get(0), 1e-9)
    }
}

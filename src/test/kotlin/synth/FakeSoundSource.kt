package synth

/** Test double that renders a fixed set of samples. */
class FakeSoundSource(
    private val samples: DoubleArray,
    private val sampleRate: Int = 100,
    private val noteOnsets: IntArray = intArrayOf(0),
) : SoundSource {
    override fun render(): Signal = Signal(samples.copyOf(), sampleRate, noteOnsets)
}

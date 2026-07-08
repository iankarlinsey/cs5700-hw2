package synth

/**
 * Clip distortion, corresponding to the `clip$threshold` effect in the song
 * file format. Samples louder than the threshold are cut off flat, producing
 * a harsher distortion than [TanhEffect].
 *
 * @param source The sound source to decorate.
 * @param threshold The amplitude at which samples are clipped (e.g. 0.8).
 */
class ClipEffect(source: SoundSource, private val threshold: Double) : Effect(source) {

    /** Clamps every sample into -[threshold]..[threshold]. */
    override fun process(input: Signal): Signal {
        for (i in 0 until input.length()) {
            input.set(i, input.get(i).coerceIn(-threshold, threshold))
        }
        return input
    }
}

package synth

/**
 * Scales the amplitude of the wrapped source, corresponding to the
 * `vol$level` effect in the song file format.
 *
 * @param source The sound source to decorate.
 * @param level The scale factor applied to every sample (e.g. 0.8).
 */
class VolumeEffect(source: SoundSource, private val level: Double) : Effect(source) {

    /** Multiplies every sample by [level]. */
    override fun process(input: Signal): Signal {
        for (i in 0 until input.length()) {
            input.set(i, input.get(i) * level)
        }
        return input
    }
}

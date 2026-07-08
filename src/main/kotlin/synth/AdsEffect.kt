package synth

/**
 * Attack-Decay-Sustain envelope, corresponding to the
 * `ads$attackEnd$decayEnd$sustain` effect in the song file format.
 *
 * The envelope restarts at the beginning of every note (using the note onsets
 * recorded in the [Signal]): amplitude ramps from 0 to 1 during the attack,
 * falls from 1 to [sustain] during the decay, and holds at [sustain] for the
 * rest of the note.
 *
 * @param source The sound source to decorate.
 * @param attackEnd The time at which the attack ramp ends, in seconds from the note start.
 * @param decayEnd The time at which the decay ends, in seconds from the note start.
 * @param sustain The amplitude level held after the decay, in 0.0..1.0.
 */
class AdsEffect(
    source: SoundSource,
    private val attackEnd: Double,
    private val decayEnd: Double,
    private val sustain: Double,
) : Effect(source) {

    /** Applies the envelope to each note independently, restarting at every onset. */
    override fun process(input: Signal): Signal {
        val onsets = input.getNoteOnsets()
        for ((noteIndex, start) in onsets.withIndex()) {
            val end = if (noteIndex + 1 < onsets.size) onsets[noteIndex + 1] else input.length()
            for (i in start until end) {
                val timeSeconds = (i - start).toDouble() / input.getSampleRate()
                input.set(i, input.get(i) * envelope(timeSeconds))
            }
        }
        return input
    }

    /**
     * Returns the envelope amplitude at the given time after a note's onset.
     *
     * @param timeSeconds Seconds elapsed since the start of the note.
     * @return The amplitude multiplier in 0.0..1.0.
     */
    private fun envelope(timeSeconds: Double): Double = when {
        timeSeconds < attackEnd -> timeSeconds / attackEnd
        timeSeconds < decayEnd -> 1.0 - (1.0 - sustain) * (timeSeconds - attackEnd) / (decayEnd - attackEnd)
        else -> sustain
    }
}

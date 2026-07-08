package synth

/**
 * One note (or rest) in a channel: a pitch and how long to hold it.
 *
 * @param frequencyHz The pitch in hertz; ignored (and conventionally 0.0) for rests.
 * @param durationBeats How long the note is held, in beats (possibly fractional).
 * @param rest True if this is a rest — silence for the given duration.
 */
class Note(
    private val frequencyHz: Double,
    private val durationBeats: Double,
    private val rest: Boolean,
) {
    /**
     * Returns the pitch of this note.
     *
     * @return The frequency in hertz.
     */
    fun getFrequencyHz(): Double = frequencyHz

    /**
     * Returns how long this note is held.
     *
     * @return The duration in beats.
     */
    fun getDurationBeats(): Double = durationBeats

    /**
     * Returns whether this note is a rest.
     *
     * @return True if this note is silence.
     */
    fun isRest(): Boolean = rest
}

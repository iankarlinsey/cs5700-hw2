package synth

/**
 * A buffer of audio samples flowing through the synthesis pipeline, from
 * [Channel] through the [Effect] chain to the [Mixer] and [AudioPlayer].
 *
 * Alongside the raw samples it carries the sample index at which each note
 * begins. Most effects ignore the onsets; [AdsEffect] uses them to restart
 * its envelope on every note.
 *
 * @param samples The amplitudes, nominally in -1.0..1.0.
 * @param sampleRate The number of samples per second of audio.
 * @param noteOnsets The index into [samples] where each note starts, in order.
 */
class Signal(
    private val samples: DoubleArray,
    private val sampleRate: Int,
    private val noteOnsets: IntArray,
) {
    /**
     * Returns the number of samples in this signal.
     *
     * @return The sample count.
     */
    fun length(): Int = samples.size

    /**
     * Returns the number of samples per second.
     *
     * @return The sample rate in hertz.
     */
    fun getSampleRate(): Int = sampleRate

    /**
     * Returns one sample.
     *
     * @param index The sample position, in 0 until [length].
     * @return The amplitude at [index].
     */
    fun get(index: Int): Double = samples[index]

    /**
     * Overwrites one sample; used by effects to transform audio in place.
     *
     * @param index The sample position, in 0 until [length].
     * @param value The new amplitude.
     */
    fun set(index: Int, value: Double) {
        samples[index] = value
    }

    /**
     * Returns where each note starts.
     *
     * @return The sample index of each note's first sample, in note order.
     */
    fun getNoteOnsets(): IntArray = noteOnsets
}

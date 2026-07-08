package synth

import kotlin.math.roundToInt

/**
 * The song file's header line: the global settings shared by every channel.
 *
 * @param sampleRate The number of audio samples per second (e.g. 44100).
 * @param beatsPerMeasure The number of beats in one measure (e.g. 4).
 * @param tempo The tempo in beats per minute; one beat lasts 60 / [tempo] seconds.
 */
class SongHeader(
    private val sampleRate: Int,
    private val beatsPerMeasure: Int,
    private val tempo: Int,
) {
    /**
     * Returns the number of audio samples per second.
     *
     * @return The sample rate in hertz.
     */
    fun getSampleRate(): Int = sampleRate

    /**
     * Returns the number of beats in one measure.
     *
     * @return The beats per measure.
     */
    fun getBeatsPerMeasure(): Int = beatsPerMeasure

    /**
     * Returns the tempo of the song.
     *
     * @return The tempo in beats per minute.
     */
    fun getTempo(): Int = tempo

    /**
     * Returns how long one beat lasts.
     *
     * @return The duration of one beat, in seconds.
     */
    fun beatDurationSeconds(): Double = 60.0 / tempo

    /**
     * Converts a duration in beats to a duration in samples.
     *
     * @param beats The number of beats, possibly fractional.
     * @return The equivalent number of samples, rounded to the nearest whole sample.
     */
    fun beatsToSamples(beats: Double): Int = (beats * beatDurationSeconds() * sampleRate).roundToInt()
}

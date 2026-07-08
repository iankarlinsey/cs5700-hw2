package synth

/**
 * Strategy contract for generating a single audio sample of a periodic (or
 * noise) waveform.
 *
 * A [Channel] holds one [Waveform] and delegates all sample generation to it,
 * so swapping the implementation swaps the sound of the whole channel without
 * changing any other code.
 */
interface Waveform {
    /**
     * Returns the amplitude of this waveform at the given moment.
     *
     * @param frequencyHz The pitch of the note being played, in hertz.
     * @param timeSeconds The time since the start of the note, in seconds.
     * @return The amplitude in the range -1.0..1.0.
     */
    fun sample(frequencyHz: Double, timeSeconds: Double): Double
}

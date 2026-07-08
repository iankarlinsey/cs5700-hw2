package synth

/**
 * A square wave: full positive amplitude for the first half of each period
 * and full negative amplitude for the second half, giving a bright, buzzy
 * timbre rich in odd harmonics.
 */
class SquareWaveform : Waveform {
    /** Returns 1.0 in the first half of each period and -1.0 in the second. */
    override fun sample(frequencyHz: Double, timeSeconds: Double): Double {
        val phase = (frequencyHz * timeSeconds).mod(1.0)
        return if (phase < 0.5) 1.0 else -1.0
    }
}

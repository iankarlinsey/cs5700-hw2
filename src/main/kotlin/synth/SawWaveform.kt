package synth

/**
 * A sawtooth wave: ramps linearly from -1.0 to 1.0 over each period, then
 * drops instantly back, giving a harsh timbre containing every harmonic.
 */
class SawWaveform : Waveform {
    /** Returns a linear ramp from -1.0 to 1.0 across each period. */
    override fun sample(frequencyHz: Double, timeSeconds: Double): Double {
        val phase = (frequencyHz * timeSeconds).mod(1.0)
        return 2.0 * phase - 1.0
    }
}

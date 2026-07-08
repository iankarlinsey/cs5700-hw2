package synth

import kotlin.math.PI
import kotlin.math.sin

/**
 * A pure sine wave: the smoothest of the four waveforms, containing only the
 * fundamental frequency with no harmonics.
 */
class SineWaveform : Waveform {
    /** Returns sin(2π × [frequencyHz] × [timeSeconds]). */
    override fun sample(frequencyHz: Double, timeSeconds: Double): Double =
        sin(2.0 * PI * frequencyHz * timeSeconds)
}

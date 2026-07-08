package synth

import kotlin.math.tanh

/**
 * Tanh distortion, corresponding to the `tanh$drive` effect in the song file
 * format. Drives the signal into a hyperbolic-tangent curve, softly
 * saturating loud samples for a warm, overdriven sound.
 *
 * @param source The sound source to decorate.
 * @param drive The amount of drive; higher values distort more heavily.
 */
class TanhEffect(source: SoundSource, private val drive: Double) : Effect(source) {

    /** Replaces every sample x with tanh([drive] × x). */
    override fun process(input: Signal): Signal {
        for (i in 0 until input.length()) {
            input.set(i, tanh(drive * input.get(i)))
        }
        return input
    }
}

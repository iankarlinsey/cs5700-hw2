package synth

import kotlin.random.Random

/**
 * White noise: an independent random amplitude on every sample, used for
 * percussion and texture. The pitch of the note is ignored; the note only
 * determines when and for how long noise plays.
 *
 * @param random The random source to draw samples from. Injecting a seeded
 *   [Random] makes the output deterministic, which the unit tests rely on.
 */
class WhiteNoiseWaveform(private val random: Random = Random.Default) : Waveform {
    /** Returns a uniformly random amplitude in -1.0..1.0, ignoring pitch and time. */
    override fun sample(frequencyHz: Double, timeSeconds: Double): Double =
        random.nextDouble(-1.0, 1.0)
}

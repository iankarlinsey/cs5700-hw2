package synth

import kotlin.math.abs

/**
 * Combines every channel of a song into one [Signal] so that all channels
 * play simultaneously.
 */
class Mixer {

    /**
     * Renders every channel, sums them sample by sample (shorter channels are
     * padded with silence), and normalizes the result.
     *
     * @param channels The channels to combine, possibly decorated with effects.
     * @param header The song header supplying the sample rate of the mix.
     * @return The mixed signal, with amplitudes kept within -1.0..1.0.
     */
    fun mix(channels: List<SoundSource>, header: SongHeader): Signal {
        val rendered = channels.map { it.render() }
        val length = rendered.maxOfOrNull { it.length() } ?: 0
        val samples = DoubleArray(length)
        for (signal in rendered) {
            for (i in 0 until signal.length()) {
                samples[i] += signal.get(i)
            }
        }
        normalize(samples, rendered.size)
        return Signal(samples, header.getSampleRate(), IntArray(0))
    }

    /**
     * Scales the mix so it cannot clip: summing N channels can reach
     * amplitude N, so every sample is divided by the channel count, and any
     * remaining overshoot (e.g. from a volume effect above 1) is normalized
     * back to full scale.
     *
     * @param samples The summed samples, scaled in place.
     * @param channelCount How many channels were summed.
     */
    private fun normalize(samples: DoubleArray, channelCount: Int) {
        if (channelCount > 0) {
            for (i in samples.indices) {
                samples[i] /= channelCount
            }
        }
        var peak = 0.0
        for (sample in samples) {
            val magnitude = abs(sample)
            if (magnitude > peak) peak = magnitude
        }
        if (peak > 1.0) {
            for (i in samples.indices) {
                samples[i] /= peak
            }
        }
    }
}

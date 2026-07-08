package synth

import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.SourceDataLine

/**
 * Plays a [Signal] through the system's audio output.
 *
 * The class is open so tests can substitute a recording double for the real
 * sound hardware.
 *
 * @param encoder The encoder that converts samples to the PCM bytes the audio
 *   line expects.
 */
open class AudioPlayer(private val encoder: PcmEncoder) {
    private var line: SourceDataLine? = null

    /**
     * Plays the signal synchronously: opens a mono 16-bit line at the
     * signal's sample rate, streams the encoded bytes, and returns once
     * playback finishes.
     *
     * @param signal The audio to play.
     */
    open fun play(signal: Signal) {
        val format = AudioFormat(signal.getSampleRate().toFloat(), 16, 1, true, false)
        val line = AudioSystem.getSourceDataLine(format)
        this.line = line
        line.open(format)
        line.start()
        val bytes = encoder.encode(signal)
        line.write(bytes, 0, bytes.size)
        line.drain()
        line.stop()
        line.close()
    }
}

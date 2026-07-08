package synth

/**
 * Contract for anything that can produce audio: the component interface of
 * the decorator pattern.
 *
 * A bare [Channel] is a [SoundSource], and so is a channel wrapped in any
 * number of [Effect] decorators — callers cannot tell the difference, which
 * is what lets effects stack freely.
 */
interface SoundSource {
    /**
     * Produces the audio for this source.
     *
     * @return The rendered [Signal].
     */
    fun render(): Signal
}

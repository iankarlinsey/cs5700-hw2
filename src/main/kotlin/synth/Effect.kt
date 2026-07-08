package synth

/**
 * Base decorator for channel effects.
 *
 * An [Effect] both is a [SoundSource] and wraps one, so any effect can wrap a
 * bare [Channel] or another effect — effects therefore stack in any order and
 * any number. [render] is a template method: it renders the wrapped source
 * and hands the result to [process], so subclasses only implement the
 * transformation itself.
 *
 * @param source The sound source this effect decorates.
 */
abstract class Effect(private val source: SoundSource) : SoundSource {

    /**
     * Renders the wrapped source, then applies this effect to the result.
     *
     * @return The transformed [Signal].
     */
    final override fun render(): Signal = process(source.render())

    /**
     * Transforms the rendered audio of the wrapped source.
     *
     * @param input The signal produced by the wrapped source.
     * @return The transformed signal.
     */
    protected abstract fun process(input: Signal): Signal
}

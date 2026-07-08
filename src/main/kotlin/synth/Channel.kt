package synth

/**
 * One audio channel of a song: a sequence of [Note]s rendered with a single
 * [Waveform] strategy. This is the concrete component of the decorator
 * pattern — the raw sound that [Effect]s wrap.
 *
 * Note boundaries are computed from cumulative beat totals so that per-note
 * rounding never accumulates into timing drift across the song.
 *
 * @param waveform The waveform strategy used to generate every sample.
 * @param notes The notes to play, in order.
 * @param header The song header supplying the sample rate and tempo.
 */
class Channel(
    private val waveform: Waveform,
    private val notes: List<Note>,
    private val header: SongHeader,
) : SoundSource {

    /**
     * Renders every note in sequence. Rests are left silent; all other notes
     * delegate each sample to the [Waveform], with time restarting at zero at
     * the start of each note.
     *
     * @return The rendered [Signal], whose note onsets mark where each note begins.
     */
    override fun render(): Signal {
        var totalBeats = 0.0
        val onsets = IntArray(notes.size)
        val ends = IntArray(notes.size)
        for ((noteIndex, note) in notes.withIndex()) {
            onsets[noteIndex] = header.beatsToSamples(totalBeats)
            totalBeats += note.getDurationBeats()
            ends[noteIndex] = header.beatsToSamples(totalBeats)
        }

        val samples = DoubleArray(if (notes.isEmpty()) 0 else ends.last())
        for ((noteIndex, note) in notes.withIndex()) {
            if (note.isRest()) continue
            for (i in onsets[noteIndex] until ends[noteIndex]) {
                val timeSeconds = (i - onsets[noteIndex]).toDouble() / header.getSampleRate()
                samples[i] = waveform.sample(note.getFrequencyHz(), timeSeconds)
            }
        }
        return Signal(samples, header.getSampleRate(), onsets)
    }
}

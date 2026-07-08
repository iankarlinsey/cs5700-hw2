package synth

/**
 * A fully parsed song: the header plus every channel, ready to be mixed.
 *
 * Channels are held as [SoundSource]s because a channel may already be
 * wrapped in [Effect] decorators — the song neither knows nor cares.
 *
 * @param header The song's global settings.
 * @param channels The channels to play simultaneously, possibly decorated.
 */
class Song(
    private val header: SongHeader,
    private val channels: List<SoundSource>,
) {
    /**
     * Returns the song's global settings.
     *
     * @return The [SongHeader].
     */
    fun getHeader(): SongHeader = header

    /**
     * Returns the channels of this song.
     *
     * @return The channels, in file order.
     */
    fun getChannels(): List<SoundSource> = channels
}

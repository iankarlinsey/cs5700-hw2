package synth

/**
 * Coordinates the whole pipeline: parse the song file, mix its channels, and
 * play the result.
 *
 * All collaborators are injected, so each stage can be substituted — the
 * tests inject a recording player in place of real audio hardware.
 *
 * @param parser Reads and validates the song file.
 * @param mixer Combines the channels into one signal.
 * @param player Plays the mixed signal.
 */
class Synthesizer(
    private val parser: SongParser,
    private val mixer: Mixer,
    private val player: AudioPlayer,
) {
    /**
     * Parses, mixes, and plays a song.
     *
     * @param filePath The path of the song file to play.
     * @throws SongFormatException if the file is missing or malformed.
     */
    fun run(filePath: String) {
        val song = parser.parse(filePath)
        val mixed = mixer.mix(song.getChannels(), song.getHeader())
        player.play(mixed)
    }
}

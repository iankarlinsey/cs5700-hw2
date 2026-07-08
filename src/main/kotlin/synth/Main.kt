package synth

/**
 * Entry point: plays the song file named by the first argument.
 *
 * All failures are reported as friendly messages rather than stack traces —
 * a missing or malformed file, or an unavailable audio device, never crashes
 * the program.
 *
 * @param args The command-line arguments; args[0] is the song file path.
 */
fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: provide the path to a song file, e.g. songs/twinkle_twinkle.txt")
        return
    }
    val synthesizer = Synthesizer(SongParser(), Mixer(), AudioPlayer(PcmEncoder()))
    try {
        synthesizer.run(args[0])
    } catch (e: SongFormatException) {
        println("Could not play song: ${e.message}")
    } catch (e: Exception) {
        println("Unexpected error: ${e.message}")
    }
}

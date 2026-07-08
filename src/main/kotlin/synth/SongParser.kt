package synth

import java.io.File

/**
 * Reads a song file and builds the object graph that plays it.
 *
 * The format is one header line (`sampleRate beatsPerMeasure tempo`) followed
 * by one line per channel (`settings|measure|measure|...`), where the settings
 * segment names a waveform and zero or more effects. The parser selects the
 * [Waveform] strategy for each channel and wraps the channel in one [Effect]
 * decorator per effect token, in the order they appear.
 *
 * Any structural problem in the file is reported as a [SongFormatException]
 * whose message names the offending line or token.
 */
class SongParser {

    /**
     * Parses a song file.
     *
     * @param filePath The path of the song file to read.
     * @return The parsed [Song].
     * @throws SongFormatException if the file is missing, empty, or malformed.
     */
    fun parse(filePath: String): Song {
        val file = File(filePath)
        if (!file.exists()) {
            throw SongFormatException("Input file not found: $filePath")
        }
        val lines = file.readLines().filter { it.isNotBlank() }
        if (lines.isEmpty()) {
            throw SongFormatException("Input file is empty: $filePath")
        }
        val header = parseHeader(lines.first())
        val channels = lines.drop(1).map { parseChannel(it, header) }
        if (channels.isEmpty()) {
            throw SongFormatException("Song has no channels: $filePath")
        }
        return Song(header, channels)
    }

    /**
     * Parses the header line into a [SongHeader].
     *
     * @param line The first line of the file.
     * @return The parsed header.
     * @throws SongFormatException if the line is not three positive whole numbers.
     */
    private fun parseHeader(line: String): SongHeader {
        val tokens = line.trim().split(Regex("\\s+"))
        if (tokens.size != 3) {
            throw SongFormatException(
                "Header must be 'sampleRate beatsPerMeasure tempo' but was: '$line'"
            )
        }
        val values = tokens.map {
            it.toIntOrNull() ?: throw SongFormatException("Header value '$it' is not a whole number")
        }
        if (values.any { it <= 0 }) {
            throw SongFormatException("Header values must be positive: '$line'")
        }
        return SongHeader(values[0], values[1], values[2])
    }

    /**
     * Parses one channel line into a (possibly decorated) [SoundSource].
     *
     * @param line The channel line: settings, then measures, separated by '|'.
     * @param header The song header the channel plays under.
     * @return The channel, wrapped in one [Effect] per effect token.
     * @throws SongFormatException if the settings, measures, or notes are malformed.
     */
    private fun parseChannel(line: String, header: SongHeader): SoundSource {
        val segments = line.split("|")
        val settings = segments.first().trim().split(Regex("\\s+"))
        if (settings.first().isEmpty()) {
            throw SongFormatException("Channel has no settings: '$line'")
        }
        val waveform = createWaveform(settings.first())
        val notes = segments.drop(1)
            .filter { it.isNotBlank() }
            .flatMap { parseMeasure(it, header) }
        if (notes.isEmpty()) {
            throw SongFormatException("Channel has no notes: '$line'")
        }
        var source: SoundSource = Channel(waveform, notes, header)
        for (token in settings.drop(1)) {
            source = applyEffect(source, token)
        }
        return source
    }

    /**
     * Parses one measure segment into its notes.
     *
     * @param segment The measure text: alternating note and duration tokens.
     * @param header The song header (reserved for measure-level validation).
     * @return The notes of the measure, in order.
     * @throws SongFormatException if tokens do not pair up or contain invalid values.
     */
    private fun parseMeasure(segment: String, header: SongHeader): List<Note> {
        val tokens = segment.trim().split(Regex("\\s+"))
        if (tokens.size % 2 != 0) {
            throw SongFormatException(
                "Measure must be pairs of 'note duration' but was: '$segment'"
            )
        }
        return tokens.chunked(2).map { (noteToken, durationToken) ->
            val duration = durationToken.toDoubleOrNull()
                ?: throw SongFormatException("Duration '$durationToken' is not a number in measure '$segment'")
            if (duration <= 0) {
                throw SongFormatException("Duration must be positive in measure '$segment'")
            }
            if (noteToken == "-") {
                Note(0.0, duration, rest = true)
            } else {
                val frequency = try {
                    Pitch.frequencyOf(noteToken)
                } catch (e: IllegalArgumentException) {
                    throw SongFormatException("Invalid note '$noteToken' in measure '$segment'", e)
                }
                Note(frequency, duration, rest = false)
            }
        }
    }

    /**
     * Selects the [Waveform] strategy named by a settings token.
     *
     * @param token The waveform name: sin, square, saw, or whitenoise.
     * @return The corresponding waveform.
     * @throws SongFormatException if the name is not a known waveform.
     */
    private fun createWaveform(token: String): Waveform = when (token) {
        "sin" -> SineWaveform()
        "square" -> SquareWaveform()
        "saw" -> SawWaveform()
        "whitenoise" -> WhiteNoiseWaveform()
        else -> throw SongFormatException(
            "Unknown waveform '$token' (expected sin, square, saw, or whitenoise)"
        )
    }

    /**
     * Wraps a sound source in the [Effect] decorator named by an effect token
     * such as `vol$.8` or `ads$.01$.2$.1`.
     *
     * @param source The source to decorate.
     * @param token The effect token: a name and its arguments separated by '$'.
     * @return The source wrapped in the new effect.
     * @throws SongFormatException if the effect name or its arguments are invalid.
     */
    private fun applyEffect(source: SoundSource, token: String): SoundSource {
        val parts = token.split("$")
        val args = parts.drop(1).map {
            it.toDoubleOrNull() ?: throw SongFormatException("Effect argument '$it' is not a number in '$token'")
        }
        return when (parts.first()) {
            "vol" -> {
                requireArgCount(token, args, 1)
                VolumeEffect(source, args[0])
            }
            "ads" -> {
                requireArgCount(token, args, 3)
                AdsEffect(source, args[0], args[1], args[2])
            }
            "tanh" -> {
                requireArgCount(token, args, 1)
                TanhEffect(source, args[0])
            }
            "clip" -> {
                requireArgCount(token, args, 1)
                ClipEffect(source, args[0])
            }
            else -> throw SongFormatException(
                "Unknown effect '${parts.first()}' (expected vol, ads, tanh, or clip)"
            )
        }
    }

    /**
     * Validates an effect token's argument count.
     *
     * @param token The full effect token, used in the error message.
     * @param args The parsed arguments.
     * @param expected How many arguments the effect requires.
     * @throws SongFormatException if the counts do not match.
     */
    private fun requireArgCount(token: String, args: List<Double>, expected: Int) {
        if (args.size != expected) {
            throw SongFormatException("Effect '$token' expects $expected argument(s) but has ${args.size}")
        }
    }
}

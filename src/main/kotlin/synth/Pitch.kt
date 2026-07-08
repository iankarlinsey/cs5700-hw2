package synth

import kotlin.math.pow

/**
 * Converts scientific pitch notation to frequency using twelve-tone equal
 * temperament tuned to A4 = 440 Hz.
 */
object Pitch {
    private val SEMITONES = mapOf(
        'C' to 0, 'D' to 2, 'E' to 4, 'F' to 5, 'G' to 7, 'A' to 9, 'B' to 11,
    )
    private const val A4_INDEX = 57
    private const val A4_FREQUENCY = 440.0

    /**
     * Returns the frequency of a note written in scientific pitch notation:
     * a letter A–G, an optional accidental (# or b), and an octave number
     * (e.g. "C4", "Bb3", "F#5").
     *
     * @param notation The note name to convert.
     * @return The frequency in hertz.
     * @throws IllegalArgumentException if [notation] is not valid pitch notation.
     */
    fun frequencyOf(notation: String): Double {
        require(notation.length >= 2) { "Invalid note '$notation'" }
        val letter = notation[0].uppercaseChar()
        val semitone = SEMITONES[letter] ?: throw IllegalArgumentException("Invalid note letter in '$notation'")
        var rest = notation.substring(1)
        var accidental = 0
        when {
            rest.startsWith('#') -> { accidental = 1; rest = rest.substring(1) }
            rest.startsWith('b') -> { accidental = -1; rest = rest.substring(1) }
        }
        val octave = rest.toIntOrNull() ?: throw IllegalArgumentException("Invalid octave in '$notation'")
        val index = octave * 12 + semitone + accidental
        return A4_FREQUENCY * 2.0.pow((index - A4_INDEX) / 12.0)
    }
}

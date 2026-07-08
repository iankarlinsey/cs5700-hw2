package synth

/**
 * Reports a missing or malformed song file. The message always names what was
 * wrong and where, so the program can fail with a helpful explanation instead
 * of an unhandled crash.
 */
class SongFormatException : Exception {
    /**
     * @param message A description of what was wrong with the file.
     */
    constructor(message: String) : super(message)

    /**
     * @param message A description of what was wrong with the file.
     * @param cause The underlying error that revealed the problem.
     */
    constructor(message: String, cause: Throwable) : super(message, cause)
}

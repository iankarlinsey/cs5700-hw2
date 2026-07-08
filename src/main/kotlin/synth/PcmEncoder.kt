package synth

/**
 * Converts a [Signal]'s floating-point samples into the 16-bit little-endian
 * PCM byte stream that audio hardware consumes.
 */
class PcmEncoder {

    /**
     * Encodes every sample as a signed 16-bit value in little-endian byte
     * order. Samples outside -1.0..1.0 are clamped to full scale first.
     *
     * @param signal The audio to encode.
     * @return The PCM bytes, two per sample.
     */
    fun encode(signal: Signal): ByteArray {
        val bytes = ByteArray(signal.length() * 2)
        for (i in 0 until signal.length()) {
            val value = (signal.get(i).coerceIn(-1.0, 1.0) * Short.MAX_VALUE).toInt()
            bytes[i * 2] = (value and 0xFF).toByte()
            bytes[i * 2 + 1] = ((value shr 8) and 0xFF).toByte()
        }
        return bytes
    }
}

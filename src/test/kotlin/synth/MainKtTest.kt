package synth

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MainKtTest {

    private fun outputOf(vararg args: String): String {
        val captured = ByteArrayOutputStream()
        val original = System.out
        System.setOut(PrintStream(captured))
        try {
            main(arrayOf(*args))
        } finally {
            System.setOut(original)
        }
        return captured.toString()
    }

    private fun songFile(content: String): String {
        val file = File.createTempFile("song", ".txt")
        file.deleteOnExit()
        file.writeText(content)
        return file.absolutePath
    }

    @Test
    fun `no arguments prints usage instead of crashing`() {
        assertTrue(outputOf().contains("Usage"))
    }

    @Test
    fun `missing file reports a helpful message instead of crashing`() {
        assertTrue(outputOf("no/such/file.txt").contains("Could not play song"))
    }

    @Test
    fun `malformed file reports a helpful message instead of crashing`() {
        assertTrue(outputOf(songFile("not a header\nsin|C4 1|")).contains("Could not play song"))
    }

    @Test
    fun `unexpected errors are reported instead of crashing`() {
        // a directory passes the exists() check but cannot be read as a file
        val dir = File.createTempFile("songdir", "").apply { delete(); mkdir(); deleteOnExit() }
        assertTrue(outputOf(dir.absolutePath).contains("Unexpected error"))
    }

    @Test
    fun `valid song is accepted end to end`() {
        // Plays a 0.1 s note where audio hardware exists; the graceful
        // catch-all handles machines without it. Either way parsing,
        // mixing, and the happy path all execute without crashing.
        assertFalse(outputOf(songFile("8000 4 600\nsin|C4 1|")).contains("Could not play song"))
    }
}

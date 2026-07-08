package synth

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class SongParserTest {

    private fun songFile(content: String): String {
        val file = File.createTempFile("song", ".txt")
        file.deleteOnExit()
        file.writeText(content)
        return file.absolutePath
    }

    @Test
    fun `parses the header sample rate`() {
        val song = SongParser().parse(songFile("44100 4 120\nsin|C4 1|"))
        assertEquals(44100, song.getHeader().getSampleRate())
    }

    @Test
    fun `parses the header tempo`() {
        val song = SongParser().parse(songFile("44100 4 120\nsin|C4 1|"))
        assertEquals(120, song.getHeader().getTempo())
    }

    @Test
    fun `parses one channel per line`() {
        val song = SongParser().parse(songFile("44100 4 120\nsin|C4 1|\nsaw|D4 1|"))
        assertEquals(2, song.getChannels().size)
    }

    @Test
    fun `blank lines are ignored`() {
        val song = SongParser().parse(songFile("44100 4 120\n\nsin|C4 1|\n"))
        assertEquals(1, song.getChannels().size)
    }

    @Test
    fun `a channel without effects is a bare channel`() {
        val song = SongParser().parse(songFile("44100 4 120\nsin|C4 1|"))
        assertTrue(song.getChannels()[0] is Channel)
    }

    @Test
    fun `effects wrap the channel in decorators`() {
        val song = SongParser().parse(songFile("44100 4 120\nsin vol$.5 tanh$5|C4 1|"))
        assertTrue(song.getChannels()[0] is Effect)
    }

    @Test
    fun `parses the square waveform`() {
        val song = SongParser().parse(songFile("100 4 60\nsquare|C4 1|"))
        assertEquals(1.0, song.getChannels()[0].render().get(0), 1e-9)
    }

    @Test
    fun `parses the whitenoise waveform`() {
        val song = SongParser().parse(songFile("100 4 60\nwhitenoise|C4 1|"))
        val signal = song.getChannels()[0].render()
        assertTrue((0 until signal.length()).all { signal.get(it) in -1.0..1.0 })
    }

    @Test
    fun `parses the ads effect`() {
        val song = SongParser().parse(songFile("100 4 60\nsin ads$0$.5$.5|C4 4|"))
        assertTrue(song.getChannels()[0] is Effect)
    }

    @Test
    fun `parses the clip effect`() {
        val song = SongParser().parse(songFile("100 4 60\nsquare clip$.25|C4 1|"))
        assertEquals(0.25, song.getChannels()[0].render().get(0), 1e-9)
    }

    @Test
    fun `rests are parsed as silent notes`() {
        val song = SongParser().parse(songFile("44100 4 120\nsin|- 4|"))
        assertEquals(0.0, song.getChannels()[0].render().get(0), 1e-9)
    }

    @Test
    fun `note durations set the rendered length`() {
        // 2 beats at 60 bpm and 100 Hz sample rate = 200 samples
        val song = SongParser().parse(songFile("100 4 60\nsin|C4 1 D4 1|"))
        assertEquals(200, song.getChannels()[0].render().length())
    }

    @Test
    fun `missing file is reported gracefully`() {
        assertFailsWith<SongFormatException> { SongParser().parse("no/such/file.txt") }
    }

    @Test
    fun `empty file is rejected`() {
        assertFailsWith<SongFormatException> { SongParser().parse(songFile("")) }
    }

    @Test
    fun `file with only a header is rejected`() {
        assertFailsWith<SongFormatException> { SongParser().parse(songFile("44100 4 120")) }
    }

    @Test
    fun `channel with no notes is rejected`() {
        assertFailsWith<SongFormatException> { SongParser().parse(songFile("44100 4 120\nsin|")) }
    }

    @Test
    fun `non numeric effect argument is rejected`() {
        assertFailsWith<SongFormatException> { SongParser().parse(songFile("44100 4 120\nsin vol\$loud|C4 1|")) }
    }

    @Test
    fun `header with too few values is rejected`() {
        assertFailsWith<SongFormatException> { SongParser().parse(songFile("44100 4\nsin|C4 1|")) }
    }

    @Test
    fun `header with non numeric values is rejected`() {
        assertFailsWith<SongFormatException> { SongParser().parse(songFile("fast 4 120\nsin|C4 1|")) }
    }

    @Test
    fun `header with a non positive value is rejected`() {
        assertFailsWith<SongFormatException> { SongParser().parse(songFile("44100 0 120\nsin|C4 1|")) }
    }

    @Test
    fun `channel line without settings is rejected`() {
        assertFailsWith<SongFormatException> { SongParser().parse(songFile("44100 4 120\n|C4 1|")) }
    }

    @Test
    fun `zero duration is rejected`() {
        assertFailsWith<SongFormatException> { SongParser().parse(songFile("44100 4 120\nsin|C4 0|")) }
    }

    @Test
    fun `unknown waveform is rejected`() {
        assertFailsWith<SongFormatException> { SongParser().parse(songFile("44100 4 120\ntriangle|C4 1|")) }
    }

    @Test
    fun `unknown effect is rejected`() {
        assertFailsWith<SongFormatException> { SongParser().parse(songFile("44100 4 120\nsin echo$.5|C4 1|")) }
    }

    @Test
    fun `effect with wrong argument count is rejected`() {
        assertFailsWith<SongFormatException> { SongParser().parse(songFile("44100 4 120\nsin ads$.1$.2|C4 1|")) }
    }

    @Test
    fun `invalid note name is rejected`() {
        assertFailsWith<SongFormatException> { SongParser().parse(songFile("44100 4 120\nsin|X9 1|")) }
    }

    @Test
    fun `measure with a dangling note is rejected`() {
        assertFailsWith<SongFormatException> { SongParser().parse(songFile("44100 4 120\nsin|C4 1 D4|")) }
    }

    @Test
    fun `non numeric duration is rejected`() {
        assertFailsWith<SongFormatException> { SongParser().parse(songFile("44100 4 120\nsin|C4 long|")) }
    }
}

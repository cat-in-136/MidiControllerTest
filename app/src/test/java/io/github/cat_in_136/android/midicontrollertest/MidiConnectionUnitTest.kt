package io.github.cat_in_136.android.midicontrollertest

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class MidiConnectionUnitTest {
    @Test
    fun noteNumberToIndexOctave_isCorrect() {
        assertEquals(Pair("C", -1), MidiConnection.noteNumberToIndexOctave(0))
        assertEquals(Pair("G", 9), MidiConnection.noteNumberToIndexOctave(127))

        assertEquals(Pair("C", 4), MidiConnection.noteNumberToIndexOctave(60)) // midC
        assertEquals(Pair("C#", 4), MidiConnection.noteNumberToIndexOctave(61))
        assertEquals(Pair("D", 4), MidiConnection.noteNumberToIndexOctave(62))
        assertEquals(Pair("D#", 4), MidiConnection.noteNumberToIndexOctave(63))
        assertEquals(Pair("E", 4), MidiConnection.noteNumberToIndexOctave(64))
        assertEquals(Pair("F", 4), MidiConnection.noteNumberToIndexOctave(65))
        assertEquals(Pair("F#", 4), MidiConnection.noteNumberToIndexOctave(66))
        assertEquals(Pair("G", 4), MidiConnection.noteNumberToIndexOctave(67))
        assertEquals(Pair("G#", 4), MidiConnection.noteNumberToIndexOctave(68))
        assertEquals(Pair("A", 4), MidiConnection.noteNumberToIndexOctave(69)) // A440
        assertEquals(Pair("A#", 4), MidiConnection.noteNumberToIndexOctave(70))
        assertEquals(Pair("B", 4), MidiConnection.noteNumberToIndexOctave(71))
        assertEquals(Pair("C", 5), MidiConnection.noteNumberToIndexOctave(72))
        assertEquals(Pair("C#", 5), MidiConnection.noteNumberToIndexOctave(73))
    }

    @Test
    fun indexOctaveToNoteNumber_isCorrectForTwoArguments() {
        assertEquals(0, MidiConnection.indexOctaveToNoteNumber("C", -1))
        assertEquals(127, MidiConnection.indexOctaveToNoteNumber("G", 9))

        assertEquals(60, MidiConnection.indexOctaveToNoteNumber("C", 4)) // midC
        assertEquals(61, MidiConnection.indexOctaveToNoteNumber("C#", 4))
        assertEquals(62, MidiConnection.indexOctaveToNoteNumber("D", 4))
        assertEquals(63, MidiConnection.indexOctaveToNoteNumber("D#", 4))
        assertEquals(64, MidiConnection.indexOctaveToNoteNumber("E", 4))
        assertEquals(65, MidiConnection.indexOctaveToNoteNumber("F", 4))
        assertEquals(66, MidiConnection.indexOctaveToNoteNumber("F#", 4))
        assertEquals(67, MidiConnection.indexOctaveToNoteNumber("G", 4))
        assertEquals(68, MidiConnection.indexOctaveToNoteNumber("G#", 4))
        assertEquals(69, MidiConnection.indexOctaveToNoteNumber("A", 4)) // A440
        assertEquals(70, MidiConnection.indexOctaveToNoteNumber("A#", 4))
        assertEquals(71, MidiConnection.indexOctaveToNoteNumber("B", 4))
        assertEquals(72, MidiConnection.indexOctaveToNoteNumber("C", 5))
        assertEquals(73, MidiConnection.indexOctaveToNoteNumber("C#", 5))

        assertThrows(IllegalArgumentException::class.java) {
            MidiConnection.indexOctaveToNoteNumber("H", 2)
        }
    }

    @Test
    fun indexOctaveToNoteNumber_isCorrectForOneArgument() {
        assertEquals(0, MidiConnection.indexOctaveToNoteNumber("C-1"))
        assertEquals(127, MidiConnection.indexOctaveToNoteNumber("G9"))

        assertEquals(60, MidiConnection.indexOctaveToNoteNumber("C4")) // midC
        assertEquals(61, MidiConnection.indexOctaveToNoteNumber("C#4"))
        assertEquals(62, MidiConnection.indexOctaveToNoteNumber("D4"))
        assertEquals(63, MidiConnection.indexOctaveToNoteNumber("D#4"))
        assertEquals(64, MidiConnection.indexOctaveToNoteNumber("E4"))
        assertEquals(65, MidiConnection.indexOctaveToNoteNumber("F4"))
        assertEquals(66, MidiConnection.indexOctaveToNoteNumber("F#4"))
        assertEquals(67, MidiConnection.indexOctaveToNoteNumber("G4"))
        assertEquals(68, MidiConnection.indexOctaveToNoteNumber("G#4"))
        assertEquals(69, MidiConnection.indexOctaveToNoteNumber("A4")) // A440
        assertEquals(70, MidiConnection.indexOctaveToNoteNumber("A#4"))
        assertEquals(71, MidiConnection.indexOctaveToNoteNumber("B4"))
        assertEquals(72, MidiConnection.indexOctaveToNoteNumber("C5"))
        assertEquals(73, MidiConnection.indexOctaveToNoteNumber("C#5"))

        assertThrows(IllegalArgumentException::class.java) {
            MidiConnection.indexOctaveToNoteNumber("H2")
        }
        assertThrows(NumberFormatException::class.java) {
            MidiConnection.indexOctaveToNoteNumber("Bad")
        }
    }
}

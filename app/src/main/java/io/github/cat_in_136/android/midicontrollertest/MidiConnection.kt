package io.github.cat_in_136.android.midicontrollertest

import android.app.Activity
import android.content.Context
import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiInputPort
import android.media.midi.MidiManager
import android.os.Handler
import android.os.Looper

class MidiConnection(activity: Activity) {
    private val midiManager = activity.applicationContext.getSystemService(Context.MIDI_SERVICE) as MidiManager

    private var inputPort: MidiInputPort? = null

    /** List of devices */
    val deviceInfo: Array<MidiDeviceInfo>
        get() = midiManager.devices

    /** Check if the device opened */
    val deviceOpened: Boolean
        get() = inputPort != null

    /** Open the device
     *
     * @param midiDevice the midi device to be opened
     */
    fun openDevice(midiDevice: MidiDeviceInfo, listener: (() -> Unit)?) {
        midiManager.openDevice(midiDevice, { device ->
            inputPort?.close()
            inputPort = device?.openInputPort(0)
            listener?.invoke()
        }, Handler(Looper.getMainLooper()))
    }

    /** Close the device */
    fun closeDevice() {
        inputPort?.close()
        inputPort = null
    }

    /** Send a midi command with raw array.
     *
     * @param buffer    the command buffer
     * @param offset    the offset
     * @param count     the command length
     */
    fun sendMidiCommand(buffer: ByteArray, offset: Int, count: Int) {
        inputPort?.send(buffer, offset, count)
    }

    /** Convenience method to send a fixed-length midi command
     *
     * @param status    the status byte
     * @param data1     the 1st data byte or null if absent
     * @param data2     the 2nd data byte or null if absent
     */
    @OptIn(ExperimentalUnsignedTypes::class)
    fun sendFixedLengthMidiCommand(status: UByte, data1: UByte?, data2: UByte?) {
        val array = if (data1 == null) {
            arrayOf(status).toUByteArray().toByteArray()
        } else if (data2 == null) {
            arrayOf(status, data1).toUByteArray().toByteArray()
        } else {
            arrayOf(status, data1, data2).toUByteArray().toByteArray()
        }
        sendMidiCommand(array, 0, array.size)
    }

    /** Combinience method to send note-on
     *
     * @param channel       the channel number 0-15
     * @param noteNumber    the note number 0-127
     * @param velocity      the velocity 0-127
     */
    fun sendNoteOn(channel: Byte, noteNumber: Byte, velocity: Byte) {
        sendFixedLengthMidiCommand((0x90 + channel).toUByte(), noteNumber.toUByte(), velocity.toUByte())
    }

    /** Combinience method to send note-off
     *
     * @param channel       the channel number 0-15
     * @param noteNumber    the note number 0-127
     * @param velocity      the velocity 0-127
     */
    fun sendNoteOff(channel: Byte, noteNumber: Byte, velocity: Byte) {
        sendFixedLengthMidiCommand((0x80 + channel).toUByte(), noteNumber.toUByte(), velocity.toUByte())
    }

    companion object {
        private val noteString = arrayOf("C","C#","D","D#","E","F","F#","G","G#","A","A#","B")

        /** Convert function from noteNumber to index string and octave */
        fun noteNumberToIndexOctave(noteNumber: Int): Pair<String,Int> {
            assert(noteNumber >= 0)
            val octave = (noteNumber / 12) - 1
            val index = noteNumber % 12
            return Pair(noteString[index], octave)
        }

        /** Convert function from index string and octave to noteNumber */
        fun indexOctaveToNoteNumber(index: CharSequence, octave: Int): Int {
            val indexNumber = noteString.indexOf(index)
            if (indexNumber < 0) {
                throw IllegalArgumentException("Illegal note index: $index")
            }
            return (octave + 1) * 12 + indexNumber
        }

        /** Convert function from index string and octave to noteNumber */
        fun indexOctaveToNoteNumber(indexOctave: CharSequence): Int {
            val indexNumber = noteString.indexOfLast { indexOctave.startsWith(it) }
            if (indexNumber < 0) {
                throw IllegalArgumentException("Illegal note index")
            }
            val octave = indexOctave.substring(noteString[indexNumber].length).toInt()
            return (octave + 1) * 12 + indexNumber
        }

    }

}
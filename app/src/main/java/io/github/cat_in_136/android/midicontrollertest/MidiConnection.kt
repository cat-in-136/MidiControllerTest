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

    /** Combinience method to send channel voice message
     *
     * @param status    the status number
     * @param channel   the channel number 0-15
     * @param data1     the 1st data byte or null if absent
     * @param data2     the 2nd data byte or null if absent
     */
    fun sendChannelVoice(status: Int, channel: Int, data1: Int?, data2: Int?) {
        val statusByte = (status and 0xF0) or (channel and 0x0F)
        val dataByte1: UByte? = data1?.toUByte()
        val dataByte2: UByte? = data2?.toUByte()
        sendFixedLengthMidiCommand(statusByte.toUByte(), dataByte1, dataByte2)
    }

    companion object {
        /** Note off status */
        const val NOTE_OFF_STATUS: Int = 0x80
        /** Note on status */
        const val NOTE_ON_STATUS: Int = 0x90
        /** After touch status */
        const val AFTER_TOUCH_STATUS: Int = 0xA0
        /** Control change status */
        const val CONTROL_CHANGE_STATUS: Int = 0xB0
        /** Program change status */
        const val PROGRAM_CHANGE_STATUS: Int = 0xC0
        /** After touch (channel pressure) status */
        const val CHANNEL_PRESSURE_STATUS: Int = 0xD0
        /** Pitch Bend status */
        const val PITCH_BEND_STATUS: Int = 0xE0

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
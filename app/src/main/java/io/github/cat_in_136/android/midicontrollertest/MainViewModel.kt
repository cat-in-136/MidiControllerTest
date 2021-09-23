package io.github.cat_in_136.android.midicontrollertest

import android.app.Activity
import android.media.midi.MidiDeviceInfo
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var midiConnection: MidiConnection? = null
        private set

    fun connectMidi(activity: Activity) {
        midiConnection = MidiConnection(activity)

        if (midiConnection != null) {
            val textViewDevice = activity.findViewById<TextView>(R.id.text_view_device)

            val deviceInfo = midiConnection!!.deviceInfo
            if (deviceInfo.isNotEmpty()) {
                val deviceName = deviceInfo[0].properties.getString(MidiDeviceInfo.PROPERTY_NAME)
                textViewDevice.text = String.format(activity.getString(R.string.device_failed_load), deviceName);
                midiConnection!!.openDevice(deviceInfo[0]) {
                    if (midiConnection!!.deviceOpened) {
                        textViewDevice.text = deviceName
                        Toast.makeText(
                            activity, "Successfully opened $deviceName",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(activity, "No MIDI device Found", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(activity, "Failed to initialize MIDI", Toast.LENGTH_LONG).show()
        }
    }

    fun disconnectMidi() {
        midiConnection?.closeDevice()
        midiConnection = null
    }
}
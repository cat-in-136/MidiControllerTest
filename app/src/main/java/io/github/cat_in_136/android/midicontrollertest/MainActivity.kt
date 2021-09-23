package io.github.cat_in_136.android.midicontrollertest

import android.media.midi.MidiDeviceInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate


class MainActivity : AppCompatActivity() {

    var midiConnection: MidiConnection? = null
        private set;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        midiConnection = MidiConnection(this)

        if (midiConnection != null) {
            val textViewDevice = findViewById<TextView>(R.id.text_view_device)

            val deviceInfo = midiConnection!!.deviceInfo
            if (deviceInfo.isNotEmpty()) {
                val deviceName = deviceInfo[0].properties.getString(MidiDeviceInfo.PROPERTY_NAME)
                textViewDevice.text = "⁉️ $deviceName"
                midiConnection!!.openDevice(deviceInfo[0]) {
                    if (midiConnection!!.deviceOpened) {
                        textViewDevice.text = deviceName
                        Toast.makeText(
                            applicationContext, "Successfully opened $deviceName",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(applicationContext, "No MIDI device Found", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(applicationContext, "Failed to initialize MIDI", Toast.LENGTH_LONG).show()
        }

        arrayOf(
            R.id.button1,
            R.id.button2,
            R.id.button3,
            R.id.button4,
            R.id.button5,
            R.id.button6,
            R.id.button7,
            R.id.button8,
            R.id.button9,
            R.id.button10,
            R.id.button11,
            R.id.button12,
            R.id.button13,
            R.id.button14,
            R.id.button15,
            R.id.button16
        ).forEachIndexed { index, btn_id ->
            val noteNumber = 36 + index
            val btn = findViewById<Button>(btn_id)
            btn!!.setOnClickListener {
                midiConnection?.sendNoteOn(0, noteNumber.toByte(), 127)
            }
        }
    }

    override fun onStop() {
        midiConnection?.closeDevice()
        super.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_quit -> {
                finishAffinity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
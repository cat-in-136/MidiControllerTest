package io.github.cat_in_136.android.midicontrollertest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider


class PadFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pad, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val packageName = requireActivity().packageName
        for (i in 0..15) {
            val btn_id = resources.getIdentifier("pad_btn${i + 1}", "id", packageName)
            val btn = view.findViewById<Button>(btn_id)
            val label = btn!!.text
            val noteNumber = MidiConnection.indexOctaveToNoteNumber(label)

            btn.setOnClickListener {
                val viewModel : MainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
                viewModel.midiConnection?.sendChannelVoice(MidiConnection.NOTE_ON_STATUS, 0, noteNumber, 127)
            }
            btn.setOnLongClickListener {
                val viewModel : MainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
                viewModel.midiConnection?.sendChannelVoice(MidiConnection.NOTE_OFF_STATUS, 0, noteNumber, 127)
                true
            }
        }
        for (i in 0..7) {
            val seekbar_id = resources.getIdentifier("pad_seekbar${i + 1}", "id", packageName)
            val seekbar = view.findViewById<SeekBar>(seekbar_id)
            val seekbar_value_textview_id = resources.getIdentifier("pad_seekbar_value_textview${i + 1}", "id", packageName)
            val seekbar_value_textview = view.findViewById<TextView>(seekbar_value_textview_id)

            val controlNumber = i
            seekbar_value_textview.text = seekbar.progress.toString()

            seekbar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    seekbar_value_textview.text = progress.toString()

                    val viewModel : MainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
                    viewModel.midiConnection?.sendChannelVoice(MidiConnection.NOTE_OFF_STATUS, 0, controlNumber, progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                }
            })
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PadFragment().apply {
//                arguments = Bundle().apply {
//                }
            }
    }
}
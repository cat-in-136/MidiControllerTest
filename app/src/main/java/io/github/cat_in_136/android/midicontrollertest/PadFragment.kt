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


class PadFragment : Fragment(), View.OnClickListener, View.OnLongClickListener, SeekBar.OnSeekBarChangeListener {


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
            val btnId = resources.getIdentifier("pad_btn${i + 1}", "id", packageName)
            val btn = view.findViewById<Button>(btnId)
            val label = btn!!.text
            val noteNumber = MidiConnection.indexOctaveToNoteNumber(label)
            btn.tag = noteNumber

            btn.setOnClickListener(this)
            btn.setOnLongClickListener(this)
        }
        for (i in 0..7) {
            val seekbarId = resources.getIdentifier("pad_seekbar${i + 1}", "id", packageName)
            val seekbar = view.findViewById<SeekBar>(seekbarId)
            seekbar!!.tag = i

            val seekbarValueTextviewId =
                resources.getIdentifier("pad_seekbar_value_textview${i + 1}", "id", packageName)
            val seekbarValueTextview = view.findViewById<TextView>(seekbarValueTextviewId)
            seekbarValueTextview.text = seekbar.progress.toString()

            seekbar.setOnSeekBarChangeListener(this)
        }
    }

    override fun onClick(view: View) {
        (view.tag as? Int)?.also { noteNumber ->
            val viewModel: MainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
            viewModel.midiConnection?.sendChannelVoice(MidiConnection.NOTE_ON_STATUS, 0, noteNumber, 127)
        }
    }

    override fun onLongClick(view: View): Boolean {
        (view.tag as? Int)?.also { noteNumber ->
            val viewModel: MainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
            viewModel.midiConnection?.sendChannelVoice(MidiConnection.NOTE_ON_STATUS, 0, noteNumber, 127)
            return true
        }
        return false
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        (seekBar.tag as? Int)?.also { i ->
            val viewModel: MainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
            viewModel.midiConnection?.sendChannelVoice(MidiConnection.CONTROL_CHANGE_STATUS, 0, i, progress)

            val packageName = requireActivity().packageName
            val seekbarValueTextviewId =
                resources.getIdentifier("pad_seekbar_value_textview${i + 1}", "id", packageName)
            val seekbarValueTextview = requireActivity().findViewById<TextView>(seekbarValueTextviewId)
            seekbarValueTextview.text = seekBar.progress.toString()
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) = Unit

    override fun onStopTrackingTouch(seekBar: SeekBar) = Unit

    companion object {
        @JvmStatic
        fun newInstance() =
            PadFragment().apply {
//                arguments = Bundle().apply {
//                }
            }
    }
}
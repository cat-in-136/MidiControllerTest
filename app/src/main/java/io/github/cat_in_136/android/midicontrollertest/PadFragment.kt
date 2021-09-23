package io.github.cat_in_136.android.midicontrollertest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
            val btn_id = resources.getIdentifier("button${i + 1}", "id", packageName)
            val btn = view.findViewById<Button>(btn_id)
            val label = btn!!.text
            val noteNumber = MidiConnection.indexOctaveToNoteNumber(label)

            btn.setOnClickListener {
                val viewModel : MainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
                viewModel.midiConnection?.sendNoteOn(0, noteNumber.toByte(), 127)
            }
            btn.setOnLongClickListener {
                val viewModel : MainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
                viewModel.midiConnection?.sendNoteOff(0, noteNumber.toByte(), 127)
                true
            }
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
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
            val btn = view.findViewById<Button>(btn_id)
            btn!!.setOnClickListener {
                val viewModel : MainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
                viewModel.midiConnection?.sendNoteOn(0, noteNumber.toByte(), 127)
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
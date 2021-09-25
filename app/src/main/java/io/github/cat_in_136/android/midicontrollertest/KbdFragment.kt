package io.github.cat_in_136.android.midicontrollertest

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.HorizontalScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider


class KbdFragment : Fragment(), View.OnTouchListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kbd, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val packageName = requireActivity().packageName
        for (i in 0..127) {
            val btnId = resources.getIdentifier("kbd_btn${i}", "id", packageName)
            val btn = view.findViewById<Button>(btnId)
            btn!!.tag = i
            btn.text = MidiConnection.noteNumberToIndexOctave(i).let {
                "${it.first}${it.second}"
            }
            btn.setOnTouchListener(this)
        }

        val horizontalScrollView = view.findViewById<HorizontalScrollView>(R.id.kbd_horizontal_scroll_view)
        horizontalScrollView!!.post {
            val btnMidC = view.findViewById<Button>(R.id.kbd_btn60)
            horizontalScrollView.scrollTo(btnMidC!!.left, 0)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        (view.tag as? Int)?.also { noteNumber ->
            val status = when (event.action) {
                MotionEvent.ACTION_DOWN -> MidiConnection.NOTE_ON_STATUS
                MotionEvent.ACTION_UP -> MidiConnection.NOTE_OFF_STATUS
                else -> return false
            }
            val viewModel: MainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
            viewModel.midiConnection?.sendChannelVoice(status, 0, noteNumber, 127)
        }
        return false
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
package com.example.android_swiss_army_knife.ui.speedometer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android_swiss_army_knife.databinding.FragmentSpeedometerBinding

class SpeedometerFragment : Fragment() {

    private var _binding: FragmentSpeedometerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val compassViewModel =
            ViewModelProvider(this).get(SpeedometerViewModel::class.java)

        _binding = FragmentSpeedometerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSpeedometer
        compassViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
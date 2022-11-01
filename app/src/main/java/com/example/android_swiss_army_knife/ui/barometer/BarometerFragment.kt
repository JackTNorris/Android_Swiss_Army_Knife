package com.example.android_swiss_army_knife.ui.barometer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android_swiss_army_knife.databinding.FragmentBarometerBinding

class BarometerFragment : Fragment() {

    private var _binding: FragmentBarometerBinding? = null
    private lateinit var barometerViewModel: BarometerViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        barometerViewModel = ViewModelProvider(this)[BarometerViewModel::class.java]
        barometerViewModel.registerSensors()
        _binding = FragmentBarometerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textBarometer
        barometerViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        barometerViewModel.deregisterSensors()
        _binding = null
    }
}
package com.example.android_swiss_army_knife.ui.metal_detector

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ekn.gruzer.gaugelibrary.HalfGauge
import com.ekn.gruzer.gaugelibrary.Range
import com.example.android_swiss_army_knife.databinding.FragmentMetalDetectorBinding

class MetalDetectorFragment : Fragment() {

    private var _binding: FragmentMetalDetectorBinding? = null
    private lateinit var metalDetectorViewModel: MetalDetectorViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        metalDetectorViewModel = ViewModelProvider(this)[MetalDetectorViewModel::class.java]
        metalDetectorViewModel.registerSensors()
        _binding = FragmentMetalDetectorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val gauge: HalfGauge = binding.metalGauge
        metalDetectorViewModel.value.observe(viewLifecycleOwner) {
            gauge.value = it
        }

        val rangeLow = Range()
        rangeLow.color = Color.parseColor("#ce0000")
        val rangeNormal = Range()
        rangeNormal.color = Color.parseColor("#00b20b")
        gauge.addRange(rangeLow)
        gauge.addRange(rangeNormal)
        gauge.minValue = 0.0
        gauge.maxValue = 300.0
        rangeLow.from = 0.0
        rangeLow.to = 100.0
        rangeNormal.from = 100.0
        rangeNormal.to = 300.0

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // call deregister sensors
        _binding = null
    }
}
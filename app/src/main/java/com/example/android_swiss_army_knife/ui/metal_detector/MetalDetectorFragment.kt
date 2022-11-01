package com.example.android_swiss_army_knife.ui.metal_detector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

        _binding = FragmentMetalDetectorBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // call register sensors
        val textView: TextView = binding.textMetalDetector
        metalDetectorViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // call deregister sensors
        _binding = null
    }
}
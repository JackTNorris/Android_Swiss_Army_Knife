package com.example.android_swiss_army_knife.ui.light

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android_swiss_army_knife.databinding.FragmentLightBinding

class LightFragment : Fragment() {

    private var _binding: FragmentLightBinding? = null
    private lateinit var lightViewModel: LightViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lightViewModel =
            ViewModelProvider(this)[LightViewModel::class.java]

        _binding = FragmentLightBinding.inflate(inflater, container, false)
        val root: View = binding.root

        lightViewModel.registerSensors()
        val textView: TextView = binding.textLight
        lightViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val imageView: ImageView = binding.imageLight
        lightViewModel.image.observe(viewLifecycleOwner) {
            val params: LayoutParams = imageView.layoutParams as LayoutParams
            val heightWidth = (it * 1300).toInt() + 100
            params.height = heightWidth
            params.width = heightWidth
            imageView.layoutParams = params
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lightViewModel.deregisterSensors()
        _binding = null
    }
}
package com.example.android_swiss_army_knife.ui.compass

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android_swiss_army_knife.databinding.FragmentCompassBinding


class CompassFragment : Fragment() {

    private var _binding: FragmentCompassBinding? = null
    private lateinit var compassViewModel: CompassViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var previousDegree = 0.0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        compassViewModel = ViewModelProvider(this)[CompassViewModel::class.java]
        compassViewModel.registerSensors()
        _binding = FragmentCompassBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textCompass
        val compassImage: ImageView = binding.imageViewCompass
        compassViewModel.compass_rotation.observe(viewLifecycleOwner) {
            textView.text = it.toString()
            val ra = RotateAnimation(
                -previousDegree.toFloat(),
                -it.toFloat(),
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )

            ra.duration = 250

            ra.fillAfter = true
            previousDegree = it
            compassImage.startAnimation(ra)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compassViewModel.deregisterSensors()
        _binding = null
    }
}
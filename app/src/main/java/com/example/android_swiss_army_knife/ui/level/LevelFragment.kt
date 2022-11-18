package com.example.android_swiss_army_knife.ui.level

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android_swiss_army_knife.databinding.FragmentCompassBinding
import com.example.android_swiss_army_knife.databinding.FragmentLevelBinding
import com.example.android_swiss_army_knife.ui.compass.CompassViewModel

class LevelFragment : Fragment() {

    private var _binding: FragmentLevelBinding? = null
    private lateinit var levelViewModel: LevelViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        levelViewModel = ViewModelProvider(this)[LevelViewModel::class.java]
        levelViewModel.registerSensors()
        _binding = FragmentLevelBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textLevel
        levelViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        levelViewModel.deregisterSensors()
        _binding = null
    }
}
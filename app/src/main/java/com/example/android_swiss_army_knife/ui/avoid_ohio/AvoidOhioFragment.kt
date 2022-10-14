package com.example.android_swiss_army_knife.ui.avoid_ohio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android_swiss_army_knife.databinding.FragmentAvoidOhioBinding

class AvoidOhioFragment : Fragment() {

    private var _binding: FragmentAvoidOhioBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val avoidOhioViewModel =
            ViewModelProvider(this).get(AvoidOhioViewModel::class.java)

        _binding = FragmentAvoidOhioBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textAvoidOhio
        avoidOhioViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
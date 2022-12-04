package com.example.android_swiss_army_knife.ui.avoid_ohio

import android.Manifest
import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android_swiss_army_knife.databinding.FragmentAvoidOhioBinding
import com.google.android.gms.location.LocationServices

class AvoidOhioFragment : Fragment() {

    private var _binding: FragmentAvoidOhioBinding? = null
    private lateinit var avoidOhioViewModel: AvoidOhioViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        avoidOhioViewModel = ViewModelProvider(this)[AvoidOhioViewModel::class.java]
        avoidOhioViewModel.locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                //If successful, startLocationRequests
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    avoidOhioViewModel.locationPermissionEnabled = true
                    avoidOhioViewModel.startLocationRequests()
                }
                //If successful at coarse detail, we still want those
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    avoidOhioViewModel.locationPermissionEnabled = true
                    avoidOhioViewModel.startLocationRequests()
                } else -> {
                //Otherwise, send toast saying location is not enabled
                avoidOhioViewModel.locationPermissionEnabled = false
                Toast.makeText(activity?.applicationContext,"Location Not Enabled", Toast.LENGTH_LONG)
                }
            }
        }
        avoidOhioViewModel.registerSensors()
        // call register sensors
        _binding = FragmentAvoidOhioBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textAvoidOhio
        avoidOhioViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onStart() {
        super.onStart()
        avoidOhioViewModel.startLocationRequests()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        avoidOhioViewModel.deregisterSensors()
        _binding = null
    }
}
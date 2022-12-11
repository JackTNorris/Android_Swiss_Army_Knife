package com.example.android_swiss_army_knife.ui.speedometer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android_swiss_army_knife.databinding.FragmentSpeedometerBinding

class SpeedometerFragment : Fragment() {

    private var _binding: FragmentSpeedometerBinding? = null
    private lateinit var speedometerViewModel: SpeedometerViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val REQUEST_CODE_LOCATION_PERMISSION = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        speedometerViewModel = ViewModelProvider(this)[SpeedometerViewModel::class.java]
        // call register sensors
        speedometerViewModel.registerSensors()

        _binding = FragmentSpeedometerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSpeedometer
        speedometerViewModel.speed.observe(viewLifecycleOwner) {
            textView.text = it
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        speedometerViewModel.registerSensors()
    }

    override fun onPause() {
        super.onPause()
        speedometerViewModel.unregisterSensors()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bind the ViewModel to the fragment's lifecycle
        speedometerViewModel.bind(this)

        // Update the speedometer display with the current speed
        speedometerViewModel.speed.observe(viewLifecycleOwner, { speed ->
            updateSpeedometer(speed)
        })
    }

    private fun updateSpeedometer(speed: String) {
        binding.textSpeedometer.text = speed
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Check if the permission was granted
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // If the permission was granted, register the sensors
            speedometerViewModel.registerSensors()
        }
    }

}




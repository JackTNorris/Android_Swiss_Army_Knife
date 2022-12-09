package com.example.android_swiss_army_knife.ui.avoid_ohio

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android_swiss_army_knife.R
import com.example.android_swiss_army_knife.databinding.FragmentAvoidOhioBinding

class AvoidOhioFragment : Fragment() {

    private var _binding: FragmentAvoidOhioBinding? = null
    private lateinit var avoidOhioViewModel: AvoidOhioViewModel
    private lateinit var textView: TextView
    private lateinit var imageView: ImageView

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
                Toast.makeText(activity?.applicationContext,"Location Not Enabled", Toast.LENGTH_LONG).show()
                }
            }
        }
        avoidOhioViewModel.registerSensors()
        // call register sensors
        _binding = FragmentAvoidOhioBinding.inflate(inflater, container, false)
        val root: View = binding.root

        textView = binding.textAvoidOhio
        imageView = binding.imageAvoidOhio
        avoidOhioViewModel.facingOhio.observe(viewLifecycleOwner) {
            if (it) {
                facingOhio()
            } else {
                notFacingOhio()
            }
        }
        return root
    }

    @SuppressLint("SetTextI18n")
    private fun facingOhio() {
        textView.text = "ERROR: You are currently facing Ohio!"
        imageView.setImageResource(R.drawable.error_sign)
    }

    @SuppressLint("SetTextI18n")
    private fun notFacingOhio() {
        textView.text = "Congratulations, you are not facing Ohio!!"
        imageView.setImageResource(R.drawable.thumbs_up)
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
package com.example.android_swiss_army_knife.ui.barometer

import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android_swiss_army_knife.databinding.FragmentBarometerBinding
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class BarometerFragment : Fragment() {

    private var _binding: FragmentBarometerBinding? = null
    private lateinit var barometerViewModel: BarometerViewModel

    private lateinit var atmFab: ExtendedFloatingActionButton
    private lateinit var psiFab: ExtendedFloatingActionButton
    private var fabsInvisible: Boolean = true

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

        val mainFab: ExtendedFloatingActionButton = binding.hpaMainFAB
        mainFab.text = hpaUnit
        mainFab.setOnClickListener {
            if (fabsInvisible) {
                fabsInvisible = false
                mainFab.text = hpaUnit
                psiFab.text = psiUnit
                atmFab.text = atmUnit
                psiFab.show()
                atmFab.show()
            } else {
                mainFab.text = hpaUnit
                barometerViewModel.updateUnits(hpaUnit)
                hideFabs(atmFab, psiFab)
            }
        }
        psiFab = binding.psiFAB
        psiFab.setOnClickListener {
            mainFab.text = psiUnit
            barometerViewModel.updateUnits(psiUnit)
            hideFabs(psiFab, atmFab)
        }
        atmFab = binding.atmFAB
        atmFab.setOnClickListener {
            mainFab.text = atmUnit
            barometerViewModel.updateUnits(atmUnit)
            hideFabs(psiFab, atmFab)
        }
        return root
    }

    private fun hideFabs(fab1: ExtendedFloatingActionButton, fab2: ExtendedFloatingActionButton) {
        fab1.hide()
        fab2.hide()
        fabsInvisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        barometerViewModel.deregisterSensors()
        _binding = null
    }

    companion object {
        const val hpaUnit = "hPa"
        const val psiUnit = "psi"
        const val atmUnit = "atm"
    }
}
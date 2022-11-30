package com.example.android_swiss_army_knife.ui.barometer

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ekn.gruzer.gaugelibrary.HalfGauge
import com.ekn.gruzer.gaugelibrary.Range
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

        val gauge: HalfGauge = binding.pressureGauge
        barometerViewModel.value.observe(viewLifecycleOwner) {
            gauge.value = it
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

        val rangeLow = Range()
        rangeLow.color = Color.parseColor("#ce0000")
        val rangeHigh = Range()
        rangeHigh.color = Color.parseColor("#ce0000")
        val rangeNormal = Range()
        rangeNormal.color = Color.parseColor("#00b20b")
        gauge.addRange(rangeLow)
        gauge.addRange(rangeHigh)
        gauge.addRange(rangeNormal)

        val textView: TextView = binding.textBarometer
        textView.text = hpaUnit
        barometerViewModel.units.observe(viewLifecycleOwner) {
            textView.text = it
            when (it) {
                hpaUnit -> {
                    gauge.minValue = 900.0
                    gauge.maxValue = 1100.0
                    rangeLow.from = 900.0
                    rangeLow.to = 950.0
                    rangeHigh.from = 1050.0
                    rangeHigh.to = 1100.0
                    rangeNormal.from = 950.0
                    rangeNormal.to = 1050.0
                }
                psiUnit -> {
                    gauge.minValue = 13.05
                    gauge.maxValue = 15.95
                    rangeLow.from = 13.05
                    rangeLow.to = 13.775
                    rangeHigh.from = 15.225
                    rangeHigh.to = 15.95
                    rangeNormal.from = 13.775
                    rangeNormal.to = 15.225
                }
                atmUnit -> {
                    gauge.minValue = 0.8883
                    gauge.maxValue = 1.0857
                    rangeLow.from = 0.8883
                    rangeLow.to = 0.93765
                    rangeHigh.from = 1.03635
                    rangeHigh.to = 1.0857
                    rangeNormal.from = 0.93765
                    rangeNormal.to = 1.03635
                }
            }
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
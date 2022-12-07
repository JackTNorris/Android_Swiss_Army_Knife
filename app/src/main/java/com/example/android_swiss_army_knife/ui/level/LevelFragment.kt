package com.example.android_swiss_army_knife.ui.level

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android_swiss_army_knife.databinding.FragmentLevelBinding
import com.mikhaellopez.circleview.CircleView


class LevelFragment : Fragment() {

    private var _binding: FragmentLevelBinding? = null
    private lateinit var levelViewModel: LevelViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var tonePlaying = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        levelViewModel = ViewModelProvider(this)[LevelViewModel::class.java]
        levelViewModel.registerSensors()
        _binding = FragmentLevelBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val movingCircle: CircleView = binding.allMovingCircle
        val boundCircle: CircleView = binding.allCircleBound
        val mWidth = this.resources.displayMetrics.widthPixels
        val mHeight = this.resources.displayMetrics.heightPixels

        levelViewModel.orientation.observe(viewLifecycleOwner) {
            val shiftY = Math.min(it[1], 9.8f)
            val shiftX = Math.min(it[0], 9.8f)
            val scaleShiftX = (boundCircle.width - movingCircle.width) / 2 * shiftX / 9.8f
            val scaleShiftY = (boundCircle.height - movingCircle.height) / 2 * shiftY / 9.8f
            if (Math.abs(shiftX) < 1)
            {
                synchronized(tonePlaying)
                {
                    if(!tonePlaying)
                    {
                        triggerSound()
                    }
                }
            }
            else
            {
                synchronized(tonePlaying)
                {
                    tonePlaying = false
                }
            }


            movingCircle.x = mWidth/2 - movingCircle.width/2 + scaleShiftX
            movingCircle.y = mHeight/2- movingCircle.height - scaleShiftY
        }

        return root
    }


    private fun triggerSound() {
        synchronized(tonePlaying)
        {
            tonePlaying = true
        }
        val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150)
        Handler(Looper.getMainLooper()).postDelayed(
            {
                tonePlaying = false
                // This method will be executed once the timer is over
            },
            1000 // value in milliseconds
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        levelViewModel.deregisterSensors()
        _binding = null
    }
}
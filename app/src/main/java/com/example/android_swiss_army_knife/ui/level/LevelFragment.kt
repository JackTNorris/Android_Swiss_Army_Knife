package com.example.android_swiss_army_knife.ui.level

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

            movingCircle.x = mWidth/2 - movingCircle.width/2 + scaleShiftX
            movingCircle.y = mHeight/2- movingCircle.height - scaleShiftY
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        levelViewModel.deregisterSensors()
        _binding = null
    }
}
package com.mohanjp.runningtracker.feature.runtrack.presentation.tracking

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.mohanjp.runningtracker.R
import com.mohanjp.runningtracker.databinding.FragmentTrackingBinding

class TrackingFragment : Fragment(R.layout.fragment_tracking) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentTrackingBinding.bind(view)
        binding.mapView.onCreate(savedInstanceState)
    }
}
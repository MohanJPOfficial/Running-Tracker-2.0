package com.mohanjp.runningtracker.feature.runtrack.presentation.run

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mohanjp.runningtracker.R
import com.mohanjp.runningtracker.databinding.FragmentRunBinding

class RunFragment : Fragment(R.layout.fragment_run) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentRunBinding.bind(view)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        }
    }
}
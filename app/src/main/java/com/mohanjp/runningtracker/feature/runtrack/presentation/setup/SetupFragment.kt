package com.mohanjp.runningtracker.feature.runtrack.presentation.setup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mohanjp.runningtracker.R
import com.mohanjp.runningtracker.databinding.FragmentSetupBinding

class SetupFragment : Fragment(R.layout.fragment_setup) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSetupBinding.bind(view)

        binding.tvContinue.setOnClickListener {
            findNavController().navigate(R.id.action_setupFragment_to_runFragment)
        }
    }
}
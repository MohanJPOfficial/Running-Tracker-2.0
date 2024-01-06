package com.mohanjp.runningtracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.mohanjp.runningtracker.common.utils.presentation.makeGone
import com.mohanjp.runningtracker.common.utils.presentation.makeVisible
import com.mohanjp.runningtracker.databinding.ActivityCoreBinding
import dagger.hilt.android.AndroidEntryPoint

//@AndroidEntryPoint
class CoreActivity : AppCompatActivity() {

    private val navHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.fragment_container_view) as? NavHostFragment }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityCoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.setNavControllerWithBottomNavigation()
        binding.showHideBottomNavigation()
    }

    private fun ActivityCoreBinding.setNavControllerWithBottomNavigation() {
        navHostFragment?.findNavController()?.let { navController ->
            bottomNavigationView.setupWithNavController(navController)
        }
    }

    private fun ActivityCoreBinding.showHideBottomNavigation() {
        navHostFragment?.findNavController()?.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.settingsFragment, R.id.runFragment, R.id.statisticsFragment -> {
                    bottomNavigationView.makeVisible()
                }
                else -> bottomNavigationView.makeGone()
            }
        }
    }
}
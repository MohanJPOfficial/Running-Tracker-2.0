package com.mohanjp.runningtracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mohanjp.runningtracker.databinding.ActivityCoreBinding

class CoreActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCoreBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}
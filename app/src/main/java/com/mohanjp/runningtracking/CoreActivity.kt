package com.mohanjp.runningtracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mohanjp.runningtracking.databinding.ActivityCoreBinding

class CoreActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCoreBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}
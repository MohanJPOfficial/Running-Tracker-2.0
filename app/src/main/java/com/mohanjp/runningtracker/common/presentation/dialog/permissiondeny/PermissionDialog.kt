package com.mohanjp.runningtracker.common.presentation.dialog.permissiondeny

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.annotation.DrawableRes
import com.mohanjp.runningtracker.R
import com.mohanjp.runningtracker.common.utils.presentation.setOnDebounceClickListener
import com.mohanjp.runningtracker.databinding.DialogPermissionBinding

abstract class PermissionDialog(context: Context) : Dialog(context, R.style.Widget_App_Dialog) {

    abstract val description: String

    abstract val btnText: String

    @get:DrawableRes
    abstract val image: Int

    abstract fun buttonClicked()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DialogPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bindUi()
    }

    private fun DialogPermissionBinding.bindUi(){

        ivPermissionImage.setImageResource(image)

        tvDescription.text = description

        tvBtn.text = btnText

        bindClick()
    }


    private fun DialogPermissionBinding.bindClick(){
        tvBtn.setOnDebounceClickListener {
            buttonClicked()
        }

        ivClose.setOnDebounceClickListener {
            dismiss()
        }
    }
}
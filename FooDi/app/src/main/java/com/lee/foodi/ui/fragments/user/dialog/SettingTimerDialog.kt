package com.lee.foodi.ui.fragments.user.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.lee.foodi.common.Utils
import com.lee.foodi.databinding.DialogSettingTimerBinding
import com.lee.foodi.ui.fragments.user.UserFragment

private const val MIN_VALUE = 0
private const val MAX_HOUR = 12
private const val MAX_MINUTE = 59

class SettingTimerDialog(context : Context, private val caller : UserFragment) : Dialog(context) {
    private lateinit var binding : DialogSettingTimerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogSettingTimerBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        Utils.dialogResize(context , this , 0.9f , 0.5f)
        initNumberPicker()
        addListeners()
    }

    private fun initNumberPicker() {
        binding.hourPicker.run {
            minValue = MIN_VALUE
            maxValue = MAX_HOUR
        }
        binding.minutePicker.run {
            minValue = MIN_VALUE
            maxValue = MAX_MINUTE
        }
    }

    private fun addListeners() {
        with(binding){
            settingButton.setOnClickListener {
                caller.updateSettingTimer()
                dismiss()
            }
        }
    }
}
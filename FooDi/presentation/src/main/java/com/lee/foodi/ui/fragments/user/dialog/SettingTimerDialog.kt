package com.lee.foodi.ui.fragments.user.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.lee.foodi.common.FoodiNewApplication
import com.lee.foodi.common.Utils
import com.lee.foodi.common.manager.FooDiPreferenceManager
import com.lee.foodi.databinding.DialogSettingTimerBinding
import com.lee.foodi.ui.fragments.user.UserFragment
import dagger.hilt.android.qualifiers.ApplicationContext

/**
 * 식단 타이머 시간 설정하는 팝업 class
 * **/
private const val MIN_VALUE = 0
private const val MAX_HOUR = 12
private const val MAX_MINUTE = 59

class SettingTimerDialog(context : Context , private val caller : UserFragment) : Dialog(context) {
    private lateinit var binding : DialogSettingTimerBinding
    private lateinit var mPreferenceManager: FooDiPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogSettingTimerBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        mPreferenceManager = caller.getPreferenceManager()

        Utils.dialogResize(context , this , 0.9f , 0.5f)
        initNumberPicker()
        addListeners()
    }

    private fun initNumberPicker() {
        binding.hourPicker.run {
            minValue = MIN_VALUE
            maxValue = MAX_HOUR
            value = mPreferenceManager.hour
        }
        binding.minutePicker.run {
            minValue = MIN_VALUE
            maxValue = MAX_MINUTE
            value = mPreferenceManager.minute
        }
    }

    private fun addListeners() {
        with(binding){
            settingButton.setOnClickListener {
                mPreferenceManager.run {
                    hour = binding.hourPicker.value
                    minute = binding.minutePicker.value
                }
                caller.updateSettingTimer()
                dismiss()
            }
        }
    }
}
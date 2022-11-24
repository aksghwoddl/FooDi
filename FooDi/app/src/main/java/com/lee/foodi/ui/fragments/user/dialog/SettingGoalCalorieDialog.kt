package com.lee.foodi.ui.fragments.user.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.lee.foodi.common.FoodiNewApplication
import com.lee.foodi.common.Utils
import com.lee.foodi.common.manager.FooDiPreferenceManager
import com.lee.foodi.databinding.DialogSettingGoalCalorieBinding
import com.lee.foodi.ui.fragments.user.UserFragment
import java.util.regex.Pattern

private const val TAG = "SettingGoalCalorieDialog"

private const val MIN_CALORIE = 1000
private const val MAX_CALORIE = 10000

class SettingGoalCalorieDialog(context : Context , private val caller : UserFragment) : Dialog(context) {
    private lateinit var binding : DialogSettingGoalCalorieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogSettingGoalCalorieBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        addListeners()
        Utils.dialogResize(context , this , 0.8f , 0.3f)
        initNumberPicker()
    }

    private fun addListeners() {
        with(binding){
            //Confirm Button
            confirmButton.setOnClickListener {
                Log.d(TAG, "addListeners: Goal calorie is updated")
                FooDiPreferenceManager.getInstance(FoodiNewApplication.getInstance()).goalCalorie = caloriePicker.value.toString()
                caller.updateGoalCalorie()
                dismiss()
            }
        }
    }

    private fun initNumberPicker() {
        binding.caloriePicker.run {
            minValue = MIN_CALORIE
            maxValue = MAX_CALORIE
            wrapSelectorWheel = false
            value = 2000
        }
    }
}
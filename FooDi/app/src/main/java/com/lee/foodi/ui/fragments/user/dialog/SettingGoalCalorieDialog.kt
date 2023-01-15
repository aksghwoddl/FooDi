package com.lee.foodi.ui.fragments.user.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.lee.foodi.common.FoodiNewApplication
import com.lee.foodi.common.Utils
import com.lee.foodi.common.manager.FooDiPreferenceManager
import com.lee.foodi.databinding.DialogSettingGoalCalorieBinding
import com.lee.foodi.ui.fragments.user.UserFragment

private const val TAG = "SettingGoalCalorieDialog"

private const val MIN_CALORIE = 1000
private const val MAX_CALORIE = 10000

class SettingGoalCalorieDialog(context : Context , private val caller : UserFragment) : Dialog(context) {
    private lateinit var binding : DialogSettingGoalCalorieBinding
    private lateinit var mPreferenceManager: FooDiPreferenceManager
    private var mPickerValue = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogSettingGoalCalorieBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        mPreferenceManager = FooDiPreferenceManager.getInstance(FoodiNewApplication.getInstance())
        addListeners()
        Utils.dialogResize(context , this , 0.9f , 0.4f)
        initNumberPicker()
    }

    /**
     * Function for generate NumberPicker's values, it is increased by 100
     * **/
    private fun generatePickerValues() {
        var calorieValue = MIN_CALORIE
        while(calorieValue <= MAX_CALORIE){
            mPickerValue.add(calorieValue.toString())
            calorieValue += 100
        }
    }

    private fun addListeners() {
        with(binding){
            //Confirm Button
            confirmButton.setOnClickListener {
                Log.d(TAG, "addListeners: Goal calorie is updated")
                mPreferenceManager.goalCalorie = (caloriePicker.value*100 + MIN_CALORIE).toString()
                caller.updateGoalCalorie()
                dismiss()
            }
        }
    }

    private fun initNumberPicker() {
        generatePickerValues()
        binding.caloriePicker.run {
            minValue = 0
            maxValue = (MAX_CALORIE - 1000)/100
            displayedValues = mPickerValue.toTypedArray()
            wrapSelectorWheel = false
            value = (mPreferenceManager.goalCalorie!!.toInt() - 1000 ) / 100
        }
    }
}
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

class SettingGoalCalorieDialog(context : Context , private val caller : UserFragment) : Dialog(context) {
    private lateinit var binding : DialogSettingGoalCalorieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogSettingGoalCalorieBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        addListeners()
        Utils.dialogResize(context , this , 0.8f , 0.3f)
    }

    private fun addListeners() {
        with(binding){
            //Confirm Button
            confirmButton.setOnClickListener {
                Log.d(TAG, "addListeners: Goal calorie is updated")
                if(calorieInputText.text!!.isNotEmpty()){
                    val calorie = calorieInputText.text.toString()
                    FooDiPreferenceManager.getInstance(FoodiNewApplication.getInstance()).setGoaCalorie(calorie)
                    Toast.makeText(FoodiNewApplication.getInstance() , "정상적으로 업데이트 되었습니다." , Toast.LENGTH_SHORT).show()
                    caller.updateGoalCalorie()
                    dismiss()
                } else {
                    Toast.makeText(FoodiNewApplication.getInstance() , "목표 칼로리를 입력해주세요." , Toast.LENGTH_SHORT).show()
                }
            }

            // Calorie InputText
            calorieInputText.addTextChangedListener(GoalCalorieTextWatcher(binding))
            calorieInputText.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
                val numRegex = "^[0-9]*$"
                val numPattern = Pattern.compile(numRegex)
                if(numPattern.matcher(source).matches()){
                    return@InputFilter source
                }
                source.dropLast(1)
            })
        }
    }

    /**
     * Input Text Watcher for goal calorie
     * **/
    private class GoalCalorieTextWatcher(private val binding : DialogSettingGoalCalorieBinding) : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

        override fun afterTextChanged(text : Editable?) {
            val numRegex = "^[0-9]*$"
            val numPattern = Pattern.compile(numRegex)
            binding.confirmButton.isEnabled = numPattern.matcher(text.toString()).matches()
        }
    }
}
package com.lee.foodi.ui.activities.search

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.lee.foodi.R
import com.lee.foodi.common.EXTRA_SELECTED_DATE
import com.lee.foodi.common.EXTRA_SELECTED_FOOD
import com.lee.foodi.common.Utils
import com.lee.foodi.data.rest.model.FoodInfoData
import com.lee.foodi.data.room.db.DiaryDatabase
import com.lee.foodi.data.room.entity.DiaryItemEntity
import com.lee.foodi.databinding.ActivityFoodDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class FoodDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFoodDetailBinding
    private lateinit var mFoodInfoData : FoodInfoData

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        mFoodInfoData = intent?.getSerializableExtra(EXTRA_SELECTED_FOOD) as FoodInfoData
        init()
    }

    /**
     * Init Functions
     * **/
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun init() {
        with(binding){
            foodNameTextView.text = mFoodInfoData.foodName
            foodNameTextView.isSelected = true // for marquee setting
            companyNameTextView.text = mFoodInfoData.company
            updateIngredientTextView(false)
        }
        initSpinner()
        addListeners()
    }

    private fun initSpinner() {
        val unitArray = resources.getStringArray(R.array.unit_array)
        val adapter = ArrayAdapter(this , com.google.android.material.R.layout.support_simple_spinner_dropdown_item , unitArray)
        binding.unitSpinner.adapter = adapter

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addListeners() {
        with(binding){
            addButton.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val db = DiaryDatabase.getInstance()
                    val date = intent.getStringExtra(EXTRA_SELECTED_DATE)!!
                    val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH : mm"))
                    val servingSize = binding.calculateEditText.text.toString() + binding.unitSpinner.selectedItem.toString()
                    val queryFood = DiaryItemEntity(null , date , mFoodInfoData , time , servingSize)
                    db.diaryDao().addDiaryItem(queryFood)
                    CoroutineScope(Dispatchers.Main).launch {
                        Utils.toastMessage("정상적으로 추가 되었습니다.")
                    }
                }
            }
            calculateEditText.addTextChangedListener(CalculateTextChangedListener())
        }
    }

    private inner class CalculateTextChangedListener : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            mFoodInfoData.servingWeight
            if(binding.calculateTextView.text.isNotEmpty()){
                updateIngredientTextView(true)
            }
        }

        override fun afterTextChanged(p0: Editable?) { }

    }

    private fun getValueOnEditTextChanged(value : String , inputValue : String) : String {
        var ret = ""
        if(mFoodInfoData.servingWeight.toInt() != 0 && value != "N/A"){
            if(binding.calculateEditText.text.isNotEmpty()){
                val divideValue = value.toDouble()/mFoodInfoData.servingWeight.toInt()
                ret = (divideValue * inputValue.toInt()).toString()
            }
        } else {
            ret= "N/A"
        }
        return ret
    }

    private fun updateIngredientTextView(changedByEditText : Boolean){
        if(changedByEditText){
            with(binding){
                Utils.convertValueWithErrorCheck(calorieTextView ,resources.getString(R.string.calories), getValueOnEditTextChanged(mFoodInfoData.calorie , calculateEditText.text.toString()))
                Utils.convertValueWithErrorCheck(carbohydrateTextView , getString(R.string.carbohydrate) , getValueOnEditTextChanged(mFoodInfoData.carbohydrate , calculateEditText.text.toString()))
                Utils.convertValueWithErrorCheck(proteinTextView , getString(R.string.protein) , getValueOnEditTextChanged(mFoodInfoData.protein , calculateEditText.text.toString()))
                Utils.convertValueWithErrorCheck(fatTextView , getString(R.string.fat) , getValueOnEditTextChanged(mFoodInfoData.fat , calculateEditText.text.toString()))
                Utils.convertValueWithErrorCheck(sugarTextView , getString(R.string.sugar) , getValueOnEditTextChanged(mFoodInfoData.sugar , calculateEditText.text.toString()))
                Utils.convertValueWithErrorCheck(saltTextView , getString(R.string.salt) , getValueOnEditTextChanged(mFoodInfoData.salt , calculateEditText.text.toString()))
                Utils.convertValueWithErrorCheck(cholesterolTextView , getString(R.string.cholesterol) , getValueOnEditTextChanged(mFoodInfoData.cholesterol , calculateEditText.text.toString()))
                Utils.convertValueWithErrorCheck(saturatedFat , getString(R.string.saturated_fat) , getValueOnEditTextChanged(mFoodInfoData.saturatedFat , calculateEditText.text.toString()))
                Utils.convertValueWithErrorCheck(transFatTextView , getString(R.string.trans_fat) , getValueOnEditTextChanged(mFoodInfoData.transFat , calculateEditText.text.toString()))
            }
        } else {
            with(binding){
                Utils.convertValueWithErrorCheck(calorieTextView ,resources.getString(R.string.calories) ,mFoodInfoData.calorie.convertStringToInt())
                Utils.convertValueWithErrorCheck(carbohydrateTextView , getString(R.string.carbohydrate) ,mFoodInfoData.carbohydrate.convertStringToInt())
                Utils.convertValueWithErrorCheck(proteinTextView , getString(R.string.protein) ,mFoodInfoData.protein.convertStringToInt())
                Utils.convertValueWithErrorCheck(fatTextView , getString(R.string.fat) ,mFoodInfoData.fat.convertStringToInt())
                Utils.convertValueWithErrorCheck(sugarTextView , getString(R.string.sugar) ,mFoodInfoData.sugar.convertStringToInt())
                Utils.convertValueWithErrorCheck(saltTextView , getString(R.string.salt) ,mFoodInfoData.salt.convertStringToInt())
                Utils.convertValueWithErrorCheck(cholesterolTextView , getString(R.string.cholesterol) ,mFoodInfoData.cholesterol.convertStringToInt())
                Utils.convertValueWithErrorCheck(saturatedFat , getString(R.string.saturated_fat) ,mFoodInfoData.saturatedFat.convertStringToInt())
                Utils.convertValueWithErrorCheck(transFatTextView , getString(R.string.trans_fat) ,mFoodInfoData.transFat.convertStringToInt())
                calculateEditText.text = Editable.Factory.getInstance().newEditable(mFoodInfoData.servingWeight.convertStringToInt())
            }
        }
    }

    private fun String.convertStringToInt() : String {
        return if(this == "N/A"){
            this
        } else {
            this.toDouble().roundToInt().toString()
        }
    }

}
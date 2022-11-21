package com.lee.foodi.ui.fragments.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.lee.foodi.R
import com.lee.foodi.common.EXTRA_SELECTED_FOOD
import com.lee.foodi.common.Utils
import com.lee.foodi.data.model.FoodInfoData
import com.lee.foodi.databinding.ActivityFoodDetailBinding

class FoodDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFoodDetailBinding
    private lateinit var mFoodInfoData : FoodInfoData

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
    @SuppressLint("SetTextI18n")
    private fun init() {
        with(binding){
            foodNameTextView.text = mFoodInfoData.foodName
            foodNameTextView.isSelected = true // for marquee setting
            companyNameTextView.text = mFoodInfoData.company
            calorieTextView.text = getString(R.string.calories) + Utils.checkErrorValue(mFoodInfoData.calorie) + " kcal"
            carbohydrateTextView.text = getString(R.string.carbohydrate) + Utils.checkErrorValue(mFoodInfoData.carbohydrate) + " g"
            proteinTextView.text = getString(R.string.protein) + Utils.checkErrorValue(mFoodInfoData.protein) + " g"
            fatTextView.text = getString(R.string.fat) + Utils.checkErrorValue(mFoodInfoData.fat) + " g"
            sugarTextView.text = getString(R.string.sugar) + Utils.checkErrorValue(mFoodInfoData.sugar) + " g"
            saltTextView.text = getString(R.string.salt) + Utils.checkErrorValue(mFoodInfoData.salt) + " g"
            cholesterolTextView.text = getString(R.string.cholesterol) + Utils.checkErrorValue(mFoodInfoData.cholesterol) + " g"
            saturatedFat.text = getString(R.string.saturated_fat) + Utils.checkErrorValue(mFoodInfoData.saturatedFat) + " g"
            transFatTextView.text = getString(R.string.trans_fat) + Utils.checkErrorValue(mFoodInfoData.transFat) + " g"
            calculateEditText.text = Editable.Factory.getInstance().newEditable(mFoodInfoData.servingWeight)
        }
        initSpinner()
    }

    private fun initSpinner() {
        val unitArray = resources.getStringArray(R.array.unit_array)
        val adapter = ArrayAdapter(this , com.google.android.material.R.layout.support_simple_spinner_dropdown_item , unitArray)
        binding.unitSpinner.adapter = adapter
    }
}
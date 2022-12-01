package com.lee.foodi.ui.activities.search

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.lee.foodi.R
import com.lee.foodi.common.EXTRA_SELECTED_FOOD
import com.lee.foodi.common.Utils
import com.lee.foodi.data.rest.model.FoodInfoData
import com.lee.foodi.data.room.db.DiaryDatabase
import com.lee.foodi.data.room.entity.DiaryItemEntity
import com.lee.foodi.databinding.ActivityFoodDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
            Utils.convertValueWithErrorCheck(calorieTextView ,resources.getString(R.string.calories) ,mFoodInfoData.calorie)
            Utils.convertValueWithErrorCheck(carbohydrateTextView , getString(R.string.carbohydrate) ,mFoodInfoData.carbohydrate)
            Utils.convertValueWithErrorCheck(proteinTextView , getString(R.string.protein) ,mFoodInfoData.protein)
            Utils.convertValueWithErrorCheck(fatTextView , getString(R.string.fat) ,mFoodInfoData.fat)
            Utils.convertValueWithErrorCheck(sugarTextView , getString(R.string.sugar) ,mFoodInfoData.sugar)
            Utils.convertValueWithErrorCheck(saltTextView , getString(R.string.salt) ,mFoodInfoData.salt)
            Utils.convertValueWithErrorCheck(cholesterolTextView , getString(R.string.cholesterol) ,mFoodInfoData.cholesterol)
            Utils.convertValueWithErrorCheck(saturatedFat , getString(R.string.saturated_fat) ,mFoodInfoData.saturatedFat)
            Utils.convertValueWithErrorCheck(transFatTextView , getString(R.string.trans_fat) ,mFoodInfoData.transFat)
            calculateEditText.text = Editable.Factory.getInstance().newEditable(mFoodInfoData.servingWeight)
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
                    val date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년MM월dd일"))
                    val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH : mm"))
                    val servingSize = binding.calculateEditText.text.toString() + binding.unitSpinner.selectedItem.toString()
                    val queryFood = DiaryItemEntity(null , date , mFoodInfoData , time , servingSize)
                    db.diaryDao().addDiaryItem(queryFood)
                    CoroutineScope(Dispatchers.Main).launch {
                        Utils.toastMessage("정상적으로 추가 되었습니다.")
                    }
                }
            }
        }
    }
}
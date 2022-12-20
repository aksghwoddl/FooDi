package com.lee.foodi.ui.fragments.diary.detail

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding3.widget.textChanges
import com.lee.foodi.R
import com.lee.foodi.common.EXTRA_SELECTED_DIARY_ITEM
import com.lee.foodi.common.NOT_AVAILABLE
import com.lee.foodi.common.Utils
import com.lee.foodi.data.repository.FoodiRepository
import com.lee.foodi.data.room.entity.DiaryItem
import com.lee.foodi.data.room.entity.DiaryItemEntity
import com.lee.foodi.databinding.ActivityDiaryDetailBinding
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val TAG = "DiaryDetailActivity"

class DiaryDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDiaryDetailBinding
    private lateinit var mDiaryItem : DiaryItem
    private lateinit var mCompositeDisposable: CompositeDisposable

    private var mCalculatedCalorie = ""
    private var mCalculatedCarbohydrate = ""
    private var mCalculatedProtein = ""
    private var mCalculatedFat = ""
    private var mCalculatedSugar = ""
    private var mCalculatedSalt = ""
    private var mCalculatedCholesterol = ""
    private var mCalculatedSaturatedFat = ""
    private var mCalculatedTransFat = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryDetailBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        mDiaryItem = intent?.extras?.getSerializable(EXTRA_SELECTED_DIARY_ITEM) as DiaryItem
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(::mCompositeDisposable.isInitialized){ // For clear all disposables
            mCompositeDisposable.clear()
        }
    }

    /**
     * Init Functions
     * **/
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun init() {
        with(binding){
            foodNameTextView.text = mDiaryItem.food!!.foodName
            foodNameTextView.isSelected = true // for marquee setting
            if(mDiaryItem.food!!.company == "N/A"){
                companyNameTextView.visibility = View.INVISIBLE
            } else {
                companyNameTextView.text = mDiaryItem.food!!.company
            }
        }
        updateIngredientTextView(false)
        addListeners()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addListeners() {
        with(binding){
            // Modify Button
            modifyButton.setOnClickListener {
                updateFoodInfo()
                updateDiaryItem()
                Utils.toastMessage("정상적으로 수정 되었습니다.")
                finish()
            }

            // Calculate EditText
            val disposable = calculateEditText.textChanges().subscribe {
                updateIngredientTextView(true)
            }
            mCompositeDisposable = CompositeDisposable(disposable)

            // Back Button
            backButton.setOnClickListener {
                finish()
            }
        }
    }

    private fun updateDiaryItem() {
        CoroutineScope(Dispatchers.IO).launch {
            val diaryItemEntity = DiaryItemEntity(
                mDiaryItem.index
                , mDiaryItem.date
                , mDiaryItem.food
                , mDiaryItem.time ,binding.calculateEditText.text.toString() + binding.unitTextView.text)
            FoodiRepository.getInstance().updateDiaryItem(diaryItemEntity)
            Log.d(TAG, "updateDiaryItem: $diaryItemEntity")
        }
    }

   /**
    * Function for get value during EditText is changing
    * **/
    private fun getValueOnEditTextChanged(value : String , inputValue : String) : String {
        var ret = ""
        if(mDiaryItem.food!!.servingWeight.toInt() != 0 && value != NOT_AVAILABLE){
            if(binding.calculateEditText.text.isNotEmpty()){
                val divideValue = value.toDouble()/(mDiaryItem.food!!.servingWeight.toInt())
                val calculatedValue = divideValue * inputValue.toInt()
                ret = calculatedValue.roundToInt().toString()
            }
        } else {
            ret = NOT_AVAILABLE
        }
        return ret
    }

    /**
     * Function for update Textview that show food info
     * **/
    private fun updateIngredientTextView(changedByEditText : Boolean){
        if(changedByEditText){
            with(binding){
                // Calorie
                mCalculatedCalorie = getValueOnEditTextChanged(mDiaryItem.food!!.calorie , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(calorieTextView ,resources.getString(R.string.calories), mCalculatedCalorie)

                // Carbohydrate
                mCalculatedCarbohydrate = getValueOnEditTextChanged(mDiaryItem.food!!.carbohydrate , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(carbohydrateTextView , getString(R.string.carbohydrate) , mCalculatedCarbohydrate)

                // Protein
                mCalculatedProtein = getValueOnEditTextChanged(mDiaryItem.food!!.protein , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(proteinTextView , getString(R.string.protein) , mCalculatedProtein)

                // Fat
                mCalculatedFat = getValueOnEditTextChanged(mDiaryItem.food!!.fat , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(fatTextView , getString(R.string.fat) , mCalculatedFat)

                // Sugar
                mCalculatedSugar = getValueOnEditTextChanged(mDiaryItem.food!!.sugar , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(sugarTextView , getString(R.string.sugar) , mCalculatedSugar)

                // Salt
                mCalculatedSalt = getValueOnEditTextChanged(mDiaryItem.food!!.salt , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(saltTextView , getString(R.string.salt) , mCalculatedSalt)

                // Cholesterol
                mCalculatedCholesterol = getValueOnEditTextChanged(mDiaryItem.food!!.cholesterol , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(cholesterolTextView , getString(R.string.cholesterol) , mCalculatedCholesterol)

                // Saturated Fat
                mCalculatedSaturatedFat = getValueOnEditTextChanged(mDiaryItem.food!!.saturatedFat , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(saturatedFat , getString(R.string.saturated_fat) , mCalculatedSaturatedFat)

                // Trans Fat
                mCalculatedTransFat = getValueOnEditTextChanged(mDiaryItem.food!!.transFat , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(transFatTextView , getString(R.string.trans_fat) , mCalculatedTransFat)
            }
        } else {
            with(binding){
                Utils.convertValueWithErrorCheck(calorieTextView ,resources.getString(R.string.calories) ,mDiaryItem.food!!.calorie.convertStringToInt())
                Utils.convertValueWithErrorCheck(carbohydrateTextView , getString(R.string.carbohydrate) ,mDiaryItem.food!!.carbohydrate.convertStringToInt())
                Utils.convertValueWithErrorCheck(proteinTextView , getString(R.string.protein) ,mDiaryItem.food!!.protein.convertStringToInt())
                Utils.convertValueWithErrorCheck(fatTextView , getString(R.string.fat) ,mDiaryItem.food!!.fat.convertStringToInt())
                Utils.convertValueWithErrorCheck(sugarTextView , getString(R.string.sugar) ,mDiaryItem.food!!.sugar.convertStringToInt())
                Utils.convertValueWithErrorCheck(saltTextView , getString(R.string.salt) ,mDiaryItem.food!!.salt.convertStringToInt())
                Utils.convertValueWithErrorCheck(cholesterolTextView , getString(R.string.cholesterol) ,mDiaryItem.food!!.cholesterol.convertStringToInt())
                Utils.convertValueWithErrorCheck(saturatedFat , getString(R.string.saturated_fat) ,mDiaryItem.food!!.saturatedFat.convertStringToInt())
                Utils.convertValueWithErrorCheck(transFatTextView , getString(R.string.trans_fat) ,mDiaryItem.food!!.transFat.convertStringToInt())
                calculateEditText.text = Editable.Factory.getInstance().newEditable(mDiaryItem.servingSize.convertStringToInt())

                mCalculatedCalorie = mDiaryItem.food!!.calorie
                mCalculatedCarbohydrate = mDiaryItem.food!!.carbohydrate
                mCalculatedProtein = mDiaryItem.food!!.protein
                mCalculatedFat = mDiaryItem.food!!.fat
                mCalculatedSugar = mDiaryItem.food!!.sugar
                mCalculatedSalt = mDiaryItem.food!!.salt
                mCalculatedCholesterol = mDiaryItem.food!!.cholesterol
                mCalculatedSaturatedFat = mDiaryItem.food!!.saturatedFat
                mCalculatedTransFat = mDiaryItem.food!!.transFat
            }
        }
    }

    /**
     * Function for update food info when insert Room DB
     * **/
    private fun updateFoodInfo() {
        with(mDiaryItem.food!!){
            calorie = mCalculatedCalorie
            carbohydrate = mCalculatedCarbohydrate
            protein = mCalculatedProtein
            fat = mCalculatedFat
            sugar = mCalculatedSugar
            salt = mCalculatedSalt
            cholesterol = mCalculatedCholesterol
            saturatedFat = mCalculatedSaturatedFat
            transFat = mCalculatedTransFat
            servingWeight = binding.calculateEditText.text.toString()
        }
    }

    /**
     * Function for convert String that have double value to Integer
     * **/
    private fun String.convertStringToInt() : String {
        return if(this == NOT_AVAILABLE){
            this
        }else if(this.contains("g")){
            this.substring(0 , this.length -1)
        }
        else {
            this.toDouble().roundToInt().toString()
        }
    }
}
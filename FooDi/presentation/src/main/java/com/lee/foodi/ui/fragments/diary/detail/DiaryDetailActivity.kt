package com.lee.foodi.ui.fragments.diary.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding3.widget.textChanges
import com.lee.foodi.R
import com.lee.foodi.common.EXTRA_SELECTED_DIARY_ITEM
import com.lee.foodi.common.NOT_AVAILABLE
import com.lee.foodi.common.Utils
import com.lee.foodi.data.repository.FoodiRepositoryImpl
import com.lee.foodi.data.room.entity.DiaryItem
import com.lee.foodi.data.room.entity.DiaryItemEntity
import com.lee.foodi.databinding.ActivityDiaryDetailBinding
import com.lee.foodi.domain.FoodiRepository
import com.lee.foodi.ui.fragments.diary.detail.viewmodel.DiaryDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

private const val TAG = "DiaryDetailActivity"

@AndroidEntryPoint
class DiaryDetailActivity : AppCompatActivity() {
    private val viewModel : DiaryDetailViewModel by viewModels()
    private lateinit var binding : ActivityDiaryDetailBinding
    private lateinit var mDiaryItem : DiaryItem
    private lateinit var mTextChangedDisposable: Disposable

    private var mCalculatedCalorie = ""
    private var mCalculatedCarbohydrate = ""
    private var mCalculatedProtein = ""
    private var mCalculatedFat = ""
    private var mCalculatedSugar = ""
    private var mCalculatedSalt = ""
    private var mCalculatedCholesterol = ""
    private var mCalculatedSaturatedFat = ""
    private var mCalculatedTransFat = ""

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
        if(::mTextChangedDisposable.isInitialized){ // Clear disposable making by RxBinding
            mTextChangedDisposable.dispose()
        }
    }

    private fun init() {
        with(binding){
            foodNameTextView.text = mDiaryItem.food!!.foodName
            foodNameTextView.isSelected = true // marquee 동작
            if(mDiaryItem.food!!.company == NOT_AVAILABLE){
                companyNameTextView.visibility = View.INVISIBLE
            } else {
                companyNameTextView.text = mDiaryItem.food!!.company
            }
        }
        updateIngredientTextView(false)
        addListeners()
    }

    private fun addListeners() {
        with(binding){
            modifyButton.setOnClickListener { // 수정하기 버튼
                updateFoodInfo()
                val servingSize = binding.calculateEditText.text.toString() + binding.unitTextView.text
                viewModel.updateDiaryItem(mDiaryItem , servingSize)
                Utils.toastMessage(this@DiaryDetailActivity , getString(R.string.successfully_modify))
                finish()
            }

            mTextChangedDisposable = calculateEditText.textChanges().subscribe { // 1회 제공량 EditText
                updateIngredientTextView(true)
            }

            // Back Button
            backButton.setOnClickListener { // 뒤로가기 버튼
                finish()
            }
        }
    }

   /**
    * EditText가 변함에 따라 계산된 영양소를 가져오는 함수
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
     * 화면에 보여지는 영양소를 업데이트 하는 함수 (changedByEditText를 통해 EditText가 변경되어 영양소를 update하는지 구분함)
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
     * RoomDB에 데이터 추가하기전 영양소 업데이트 하는 함수
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
     * 전달받은 영양소를 N/A가 아닐 경우에 g을 붙여 반환하도록 하는 함수
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
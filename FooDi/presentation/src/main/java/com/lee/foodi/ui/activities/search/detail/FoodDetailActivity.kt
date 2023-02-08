package com.lee.foodi.ui.activities.search.detail

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jakewharton.rxbinding3.widget.textChanges
import com.lee.domain.model.remote.Food
import com.lee.foodi.R
import com.lee.foodi.common.*
import com.lee.foodi.common.manager.FooDiPreferenceManager
import com.lee.foodi.databinding.ActivityFoodDetailBinding
import com.lee.foodi.receiver.TimerReceiver
import com.lee.foodi.ui.activities.search.detail.viewmodel.FoodDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt


/**
 * 음식 상제정보 Activity
 * **/
private const val TAG = "FoodDetailActivity"
@AndroidEntryPoint
class FoodDetailActivity : AppCompatActivity() {
    private val viewModel : FoodDetailViewModel by viewModels()
    private lateinit var binding : ActivityFoodDetailBinding
    private lateinit var mFood : Food
    private lateinit var mFooDiPreferenceManager: FooDiPreferenceManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        mFood = intent?.getSerializableExtra(EXTRA_SELECTED_FOOD) as Food
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(::mCompositeDisposable.isInitialized){ // For clear all disposable
            mCompositeDisposable.clear()
        }
    }

    private fun init() {
        with(binding){
            foodNameTextView.text = mFood.foodName
            foodNameTextView.isSelected = true // for marquee setting
            if(mFood.company == NOT_AVAILABLE){
                companyNameTextView.visibility = View.INVISIBLE
            } else {
                companyNameTextView.text = mFood.company
            }
        }
        mFooDiPreferenceManager = FooDiPreferenceManager.getInstance(applicationContext)
        updateIngredientTextView(false)
        addListeners()
    }

    private fun addListeners() {
        with(binding){
            // Add Button
            addButton.setOnClickListener {
                lifecycleScope.launch {
                    updateFoodInfo()
                    val servingSize = binding.calculateEditText.text.toString() + binding.unitTextView.text
                    viewModel.addFoodIntoDatabase(
                        intent?.getStringExtra(EXTRA_SELECTED_DATE)!! ,
                        servingSize ,
                        mFood
                    )
                    if(mFooDiPreferenceManager.isTimerOn){
                        Log.d(TAG, "addListeners: click addButton , timer is enable ")
                        updateTimer()
                    } else {
                        Log.d(TAG, "addListeners: click addButton , timer is not enable ")
                    }
                    Utils.toastMessage(this@FoodDetailActivity , getString(R.string.successfully_add))
                    finish()
                }
            }

            // Calculate EditText

            val disposable = calculateEditText.textChanges().subscribe{
                updateIngredientTextView(true)
            }
            mCompositeDisposable = CompositeDisposable(disposable)

            // Back Button
            backButton.setOnClickListener {
                finish()
            }
        }
    }

    /**
     * 식단 타이머 시간 update하는 함수
     * **/
    private fun updateTimer() {
        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년MM월dd일"))
        if( intent.getStringExtra(EXTRA_SELECTED_DATE) == today){
            val intent = Intent(this@FoodDetailActivity , TimerReceiver::class.java)
            intent.putExtra(EXTRA_CODE , REQUEST_CODE)
            intent.putExtra(EXTRA_COUNT , 32)
            val pendingIntent = PendingIntent.getBroadcast(this@FoodDetailActivity
                , REQUEST_CODE
                , intent
                , 0)
            val hour = mFooDiPreferenceManager.hour *  3600000
            val minute = mFooDiPreferenceManager.minute * 60000
            if(hour == 0 && minute == 0){
                Log.d(TAG, "updateTimer: timer is enable but not setting time")
            } else {
                val alarmTime = SystemClock.elapsedRealtime() + hour + minute
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP , alarmTime , pendingIntent)
                Log.d(TAG, "updateTimer: alarmTime = $alarmTime")
            }
        } else {
            Log.d(TAG, "updateTimer: timer is enable but selected date is not today")
        }
    }

    /**
     * EditText가 변함에 따라 계산된 영양소를 가져오는 함수
     * **/
    private fun getValueOnEditTextChanged(value : String , inputValue : String) : String {
        var ret = ""
        if(mFood.servingWeight.toInt() != 0 && value != NOT_AVAILABLE){
            if(binding.calculateEditText.text.isNotEmpty()){
                val divideValue = value.toDouble()/mFood.servingWeight.toInt()
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
                mCalculatedCalorie = getValueOnEditTextChanged(mFood.calorie , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(calorieTextView ,resources.getString(R.string.calories), mCalculatedCalorie)

                // Carbohydrate
                mCalculatedCarbohydrate = getValueOnEditTextChanged(mFood.carbohydrate , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(carbohydrateTextView , getString(R.string.carbohydrate) , mCalculatedCarbohydrate)

                // Protein
                mCalculatedProtein = getValueOnEditTextChanged(mFood.protein , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(proteinTextView , getString(R.string.protein) , mCalculatedProtein)

                // Fat
                mCalculatedFat = getValueOnEditTextChanged(mFood.fat , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(fatTextView , getString(R.string.fat) , mCalculatedFat)

                // Sugar
                mCalculatedSugar = getValueOnEditTextChanged(mFood.sugar , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(sugarTextView , getString(R.string.sugar) , mCalculatedSugar)

                // Salt
                mCalculatedSalt = getValueOnEditTextChanged(mFood.salt , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(saltTextView , getString(R.string.salt) , mCalculatedSalt)

                // Cholesterol
                mCalculatedCholesterol = getValueOnEditTextChanged(mFood.cholesterol , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(cholesterolTextView , getString(R.string.cholesterol) , mCalculatedCholesterol)

                // Saturated Fat
                mCalculatedSaturatedFat = getValueOnEditTextChanged(mFood.saturatedFat , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(saturatedFat , getString(R.string.saturated_fat) , mCalculatedSaturatedFat)

                // Trans Fat
                mCalculatedTransFat = getValueOnEditTextChanged(mFood.transFat , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(transFatTextView , getString(R.string.trans_fat) , mCalculatedTransFat)
            }
        } else {
            with(binding){
                Utils.convertValueWithErrorCheck(calorieTextView ,resources.getString(R.string.calories) ,mFood.calorie.convertStringToInt())
                Utils.convertValueWithErrorCheck(carbohydrateTextView , getString(R.string.carbohydrate) ,mFood.carbohydrate.convertStringToInt())
                Utils.convertValueWithErrorCheck(proteinTextView , getString(R.string.protein) ,mFood.protein.convertStringToInt())
                Utils.convertValueWithErrorCheck(fatTextView , getString(R.string.fat) ,mFood.fat.convertStringToInt())
                Utils.convertValueWithErrorCheck(sugarTextView , getString(R.string.sugar) ,mFood.sugar.convertStringToInt())
                Utils.convertValueWithErrorCheck(saltTextView , getString(R.string.salt) ,mFood.salt.convertStringToInt())
                Utils.convertValueWithErrorCheck(cholesterolTextView , getString(R.string.cholesterol) ,mFood.cholesterol.convertStringToInt())
                Utils.convertValueWithErrorCheck(saturatedFat , getString(R.string.saturated_fat) ,mFood.saturatedFat.convertStringToInt())
                Utils.convertValueWithErrorCheck(transFatTextView , getString(R.string.trans_fat) ,mFood.transFat.convertStringToInt())
                calculateEditText.text = Editable.Factory.getInstance().newEditable(mFood.servingWeight.convertStringToInt())

                mCalculatedCalorie = mFood.calorie
                mCalculatedCarbohydrate = mFood.carbohydrate
                mCalculatedProtein = mFood.protein
                mCalculatedFat = mFood.fat
                mCalculatedSugar = mFood.sugar
                mCalculatedSalt = mFood.salt
                mCalculatedCholesterol = mFood.cholesterol
                mCalculatedSaturatedFat = mFood.saturatedFat
                mCalculatedTransFat = mFood.transFat
            }
        }
    }

    /**
     * RoomDB에 데이터 추가하기전 영양소 업데이트 하는 함수
     * **/
    private fun updateFoodInfo() {
        with(mFood){
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
        } else {
            this.toDouble().roundToInt().toString()
        }
    }

}
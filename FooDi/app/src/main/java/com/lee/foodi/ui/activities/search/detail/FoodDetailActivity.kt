package com.lee.foodi.ui.activities.search.detail

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jakewharton.rxbinding3.widget.textChanges
import com.lee.foodi.R
import com.lee.foodi.common.*
import com.lee.foodi.common.manager.FooDiPreferenceManager
import com.lee.foodi.data.repository.FoodiRepository
import com.lee.foodi.data.rest.model.Food
import com.lee.foodi.data.room.entity.DiaryItemEntity
import com.lee.foodi.databinding.ActivityFoodDetailBinding
import com.lee.foodi.receiver.TimerReceiver
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

private const val TAG = "FoodDetailActivity"

class FoodDetailActivity : AppCompatActivity() {
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

    /**
     * Init Functions
     * **/
    @SuppressLint("SetTextI18n")
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
        mFooDiPreferenceManager = FooDiPreferenceManager.getInstance(FoodiNewApplication.getInstance())
        updateIngredientTextView(false)
        addListeners()
    }

    private fun addListeners() {
        with(binding){
            // Add Button
            addButton.setOnClickListener {
                lifecycleScope.launch {
                    updateFoodInfo()
                    addFoodIntoDatabase()
                    if(mFooDiPreferenceManager.isTimerOn){
                        Log.d(TAG, "addListeners: click addButton , timer is enable ")
                        updateTimer()
                    } else {
                        Log.d(TAG, "addListeners: click addButton , timer is not enable ")
                    }
                    Utils.toastMessage(getString(R.string.successfully_add))
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
     * Function for add food into Room DB
     * **/
    private fun addFoodIntoDatabase() {
        val date = intent.getStringExtra(EXTRA_SELECTED_DATE)!!
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH : mm"))
        val servingSize = binding.calculateEditText.text.toString() + binding.unitTextView.text
        val queryFood = DiaryItemEntity(null , date , mFood , time , servingSize)
        CoroutineScope(Dispatchers.IO).launch{
            FoodiRepository.getInstance().addDiaryItem(queryFood)
        }
    }

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
     * Function for get value during EditText is changing
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
     * Function for update Textview that show food info
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
     * Function for update food info when insert Room DB
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
     * Function for convert String that have double value to Integer
     * **/
    private fun String.convertStringToInt() : String {
        return if(this == NOT_AVAILABLE){
            this
        } else {
            this.toDouble().roundToInt().toString()
        }
    }

}
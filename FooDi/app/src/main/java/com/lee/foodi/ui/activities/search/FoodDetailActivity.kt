package com.lee.foodi.ui.activities.search

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.lee.foodi.R
import com.lee.foodi.common.*
import com.lee.foodi.common.manager.FooDiPreferenceManager
import com.lee.foodi.data.rest.model.FoodInfoData
import com.lee.foodi.data.room.db.DiaryDatabase
import com.lee.foodi.data.room.entity.DiaryItemEntity
import com.lee.foodi.databinding.ActivityFoodDetailBinding
import com.lee.foodi.ui.receiver.TimerReceiver
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
    private lateinit var mFoodInfoData : FoodInfoData
    private lateinit var mFooDiPreferenceManager: FooDiPreferenceManager

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
        }
        mFooDiPreferenceManager = FooDiPreferenceManager.getInstance(FoodiNewApplication.getInstance())
        updateIngredientTextView(false)
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
                    updateFoodInfo()
                    addFoodIntoDatabase()
                    if(mFooDiPreferenceManager.isTimerOn){
                        Log.d(TAG, "addListeners: click addButton , timer is enable ")
                        updateTimer()
                    } else {
                        Log.d(TAG, "addListeners: click addButton , timer is not enable ")
                    }
                    CoroutineScope(Dispatchers.Main).launch {
                        Utils.toastMessage("정상적으로 추가 되었습니다.")
                    }
                }
            }
            calculateEditText.addTextChangedListener(CalculateTextChangedListener())
        }
    }

    /**
     * Function for add food into Room DB
     * **/
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun addFoodIntoDatabase() {
        val db = DiaryDatabase.getInstance()
        val date = intent.getStringExtra(EXTRA_SELECTED_DATE)!!
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH : mm"))
        val servingSize = binding.calculateEditText.text.toString() + binding.unitSpinner.selectedItem.toString()
        val queryFood = DiaryItemEntity(null , date , mFoodInfoData , time , servingSize)
        db.diaryDao().addDiaryItem(queryFood)
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
            val alarmTime = SystemClock.elapsedRealtime() + hour + minute


            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP , alarmTime , pendingIntent)
            Log.d(TAG, "updateTimer: alarmTime = $alarmTime")
        } else {
            Log.d(TAG, "updateTimer: timer is enable but selected date is not today")
        }
    }

    /**
     * Watching Serving Size EditText Listener
     * **/
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

    /**
     * Function for get value during EditText is changing
     * **/
    private fun getValueOnEditTextChanged(value : String , inputValue : String) : String {
        var ret = ""
        if(mFoodInfoData.servingWeight.toInt() != 0 && value != NOT_AVAILABLE){
            if(binding.calculateEditText.text.isNotEmpty()){
                val divideValue = value.toDouble()/mFoodInfoData.servingWeight.toInt()
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
                mCalculatedCalorie = getValueOnEditTextChanged(mFoodInfoData.calorie , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(calorieTextView ,resources.getString(R.string.calories), mCalculatedCalorie)

                // Carbohydrate
                mCalculatedCarbohydrate = getValueOnEditTextChanged(mFoodInfoData.carbohydrate , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(carbohydrateTextView , getString(R.string.carbohydrate) , mCalculatedCarbohydrate)

                // Protein
                mCalculatedProtein = getValueOnEditTextChanged(mFoodInfoData.protein , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(proteinTextView , getString(R.string.protein) , mCalculatedProtein)

                // Fat
                mCalculatedFat = getValueOnEditTextChanged(mFoodInfoData.fat , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(fatTextView , getString(R.string.fat) , mCalculatedFat)

                // Sugar
                mCalculatedSugar = getValueOnEditTextChanged(mFoodInfoData.sugar , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(sugarTextView , getString(R.string.sugar) , mCalculatedSugar)

                // Salt
                mCalculatedSalt = getValueOnEditTextChanged(mFoodInfoData.salt , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(saltTextView , getString(R.string.salt) , mCalculatedSalt)

                // Cholesterol
                mCalculatedCholesterol = getValueOnEditTextChanged(mFoodInfoData.cholesterol , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(cholesterolTextView , getString(R.string.cholesterol) , mCalculatedCholesterol)

                // Saturated Fat
                mCalculatedSaturatedFat = getValueOnEditTextChanged(mFoodInfoData.saturatedFat , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(saturatedFat , getString(R.string.saturated_fat) , mCalculatedSaturatedFat)

                // Trans Fat
                mCalculatedTransFat = getValueOnEditTextChanged(mFoodInfoData.transFat , calculateEditText.text.toString())
                Utils.convertValueWithErrorCheck(transFatTextView , getString(R.string.trans_fat) , mCalculatedTransFat)
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

                mCalculatedCalorie = mFoodInfoData.calorie
                mCalculatedCarbohydrate = mFoodInfoData.carbohydrate
                mCalculatedProtein = mFoodInfoData.protein
                mCalculatedFat = mFoodInfoData.fat
                mCalculatedSugar = mFoodInfoData.sugar
                mCalculatedSalt = mFoodInfoData.salt
                mCalculatedCholesterol = mFoodInfoData.cholesterol
                mCalculatedSaturatedFat = mFoodInfoData.saturatedFat
                mCalculatedTransFat = mFoodInfoData.transFat
            }
        }
    }

    /**
     * Function for update food info when insert Room DB
     * **/
    private fun updateFoodInfo() {
        with(mFoodInfoData){
            calorie = mCalculatedCalorie
            carbohydrate = mCalculatedCarbohydrate
            protein = mCalculatedProtein
            fat = mCalculatedFat
            sugar = mCalculatedSugar
            salt = mCalculatedSalt
            cholesterol = mCalculatedCholesterol
            saturatedFat = mCalculatedSaturatedFat
            transFat = mCalculatedTransFat
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
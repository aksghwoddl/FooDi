package com.lee.foodi.ui.fragments.diary.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.foodi.data.repository.FoodiRepository
import com.lee.foodi.data.room.entity.DiaryEntity
import com.lee.foodi.data.room.entity.DiaryItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val TAG = "DiaryViewModel"

class DiaryViewModel(repository: FoodiRepository) : ViewModel() {
    private var mRepository : FoodiRepository

    init {
        mRepository = repository
    }

    @RequiresApi(Build.VERSION_CODES.O)
    val date = MutableLiveData<String>(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년MM월dd일"))) // Selected date
    val diaryItems = MutableLiveData<MutableList<DiaryItem>>() // Diary items in Room
    val goalCalorie = MutableLiveData<String>("0") // Goal calorie
    val spendCalories = MutableLiveData<String>("0") // Spend Calorie
    val amountCarbon  = MutableLiveData<String>("0")
    val amountProtein  = MutableLiveData<String>("0")
    val amountFat  = MutableLiveData<String>("0")
    val calorieProgress = MutableLiveData<Double>(0.0) // Manage calorie progress bar
    val isProgress = MutableLiveData<Boolean>(false) // Manage progress bar
    val isNightMode = MutableLiveData<Boolean>(false) // Check Night mode

    /**
     * Function for get diary item as selected date
     * **/
     @RequiresApi(Build.VERSION_CODES.O)
     fun getDiaryItems(){
         CoroutineScope(Dispatchers.IO).launch {
             isProgress.postValue(true)
             val diaryItem = mRepository.getAllDiaryItems(date.value!!)
            CoroutineScope(Dispatchers.Main).launch {
                diaryItems.value = diaryItem
                isProgress.value = false
            }
        }
    }

    /**
     * Function for add Diary Summary of the day
     * **/
    @RequiresApi(Build.VERSION_CODES.O)
    fun addDiarySummary() {
        Log.d(TAG, "addDiarySummary()")
        CoroutineScope(Dispatchers.IO).launch {
            val diary = DiaryEntity(date.value!! 
                , spendCalories.value!! 
                , amountCarbon.value!! 
                , amountProtein.value!! 
                , amountFat.value!!)
            mRepository.addDiary(diary)
            Log.d(TAG, "addDiarySummary: success add diary DB!!")
        }
    }
}
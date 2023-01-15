package com.lee.foodi.ui.fragments.diary.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.foodi.R
import com.lee.foodi.common.FoodiNewApplication
import com.lee.foodi.common.Utils
import com.lee.foodi.data.repository.FoodiRepository
import com.lee.foodi.data.room.entity.DiaryEntity
import com.lee.foodi.data.room.entity.DiaryItem
import com.lee.foodi.data.room.entity.DiaryItemEntity
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val TAG = "DiaryViewModel"

class DiaryViewModel(repository: FoodiRepository) : ViewModel() {
    private var mRepository : FoodiRepository

    init {
        mRepository = repository
    }

    private lateinit var addDiaryJob : Job

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
     fun getDiaryItems(){
         viewModelScope.launch {
             val diaryItem = withContext(Dispatchers.IO){
                 isProgress.postValue(true)
                 mRepository.getAllDiaryItems(date.value!!)
             }
            diaryItems.value = diaryItem
            isProgress.value = false
        }
    }

    /**
     * Function for add Diary Summary of the day
     * **/
    fun addDiarySummary() {
        Log.d(TAG, "addDiarySummary()")
        addDiaryJob = CoroutineScope(Dispatchers.IO).launch {
            val diary = DiaryEntity(date.value!! 
                , spendCalories.value!! 
                , amountCarbon.value!! 
                , amountProtein.value!! 
                , amountFat.value!!)
            mRepository.addDiary(diary)
            Log.d(TAG, "addDiarySummary: success add diary DB!!")
        }
    }

    /**
     * Function for delete selected diary item when popup menu clicked
     * **/
    fun deleteSelectedDiaryItem(diaryItem: DiaryItem){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val selectedEntity : DiaryItemEntity
                with(diaryItem){
                    selectedEntity = DiaryItemEntity(index, date , food , time , servingSize)
                }
                val repository = FoodiRepository.getInstance()
                repository.deleteDiaryItem(selectedEntity)
            }
            getDiaryItems()
            Utils.toastMessage(FoodiNewApplication.getInstance().getString(R.string.successfully_delete))
        }
    }

    override fun onCleared() {
        if(::addDiaryJob.isInitialized){
            if(addDiaryJob.isActive){
                addDiaryJob.cancel()
            }
        }
        super.onCleared()
    }
}
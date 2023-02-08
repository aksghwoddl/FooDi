package com.lee.foodi.ui.fragments.diary.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.foodi.R
import com.lee.foodi.common.ResourceProvider
import com.lee.foodi.data.room.entity.DiaryEntity
import com.lee.foodi.data.room.entity.DiaryItem
import com.lee.foodi.data.room.entity.DiaryItemEntity
import com.lee.foodi.domain.FoodiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

private const val TAG = "DiaryViewModel"

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val repository: FoodiRepository ,
    private val resourceProvider: ResourceProvider
) : ViewModel() {
    private lateinit var addDiaryJob : Job

    private val _date = MutableLiveData<String>(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년MM월dd일"))) // Selected date
    val date : LiveData<String>
    get() = _date
    fun setDate(date : String){
        _date.value = date
    }

    private val _diaryItems = MutableLiveData<MutableList<DiaryItem>>() // Diary items in Room
    val diaryItems : LiveData<MutableList<DiaryItem>>
    get() = _diaryItems
    fun setDiaryItems(items: MutableList<DiaryItem>) {
        _diaryItems.value = items
    }

    private val _goalCalorie = MutableLiveData<String>("0") // Goal calorie
    val goalCalorie : LiveData<String>
    get() = _goalCalorie
    fun setGoalCalorie(calorie : String){
        _goalCalorie.value = calorie
    }

    private val _spendCalories = MutableLiveData<String>("0") // Spend Calorie
    val spendCalories : LiveData<String>
    get() = _spendCalories
    fun setSpendCalorie(calorie: String){
        _spendCalories.value = calorie
    }

    private val _amountCarbon  = MutableLiveData<String>("0")
    val amountCarbon : LiveData<String>
    get() = _amountCarbon
    fun setAmountCarbon(carbon : String){
        _amountCarbon.value = carbon
    }

    private val _amountProtein  = MutableLiveData<String>("0")
    val amountProtein : LiveData<String>
    get() = _amountProtein
    fun setAmountProtein(protein : String){
        _amountProtein.value = protein
    }

    private val _amountFat  = MutableLiveData<String>("0")
    val amountFat : LiveData<String>
    get() = _amountFat
    fun setAmountFat(fat : String) {
        _amountFat.value = fat
    }

    private val _calorieProgress = MutableLiveData<Double>(0.0) // Manage calorie progress bar
    val calorieProgress : LiveData<Double>
    get() = _calorieProgress
    fun setCalorieProgress(calorie: Double) {
        _calorieProgress.value = calorie
    }

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage : LiveData<String>
    get() = _toastMessage

    private val _isProgress = MutableLiveData<Boolean>()
    val isProgress : LiveData<Boolean>
    get() = _isProgress

    private val _isNightMode = MutableLiveData<Boolean>(false)
    val isNightMode : LiveData<Boolean>
    get() = _isNightMode
    fun setIsNightMode(isNightMode : Boolean) {
        _isNightMode.value = isNightMode
    }

    /**
     * Function for get diary item as selected date
     * **/
     fun getDiaryItems(){
         viewModelScope.launch {
             _isProgress.value = true
             val diaryItem = withContext(Dispatchers.IO){
                 repository.getAllDiaryItems(date.value!!)
             }
            _diaryItems.value = diaryItem
             _isProgress.value = false
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
            repository.addDiary(diary)
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
                repository.deleteDiaryItem(selectedEntity)
            }
            getDiaryItems()
            _toastMessage.value = resourceProvider.getString(R.string.successfully_delete)
        }
    }

    override fun onCleared() {
        super.onCleared()
        if(::addDiaryJob.isInitialized){
            if(addDiaryJob.isActive){
                addDiaryJob.cancel()
            }
        }
    }
}
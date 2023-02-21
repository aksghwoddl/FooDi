package com.lee.foodi.ui.fragments.diary.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.domain.model.local.Diary
import com.lee.domain.model.local.DiaryItem
import com.lee.domain.usecase.AddDiary
import com.lee.domain.usecase.DeleteDiaryItem
import com.lee.domain.usecase.GetAllDiaryItems
import com.lee.foodi.R
import com.lee.foodi.common.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * 다이어리 ViewModel
 * **/
private const val TAG = "DiaryViewModel"
@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val getAllDiaryItems: GetAllDiaryItems,
    private val addDiary: AddDiary ,
    private val deleteDiaryItem: DeleteDiaryItem ,
    private val resourceProvider: ResourceProvider
) : ViewModel() {
    private lateinit var addDiaryJob : Job

    private val _date = MutableLiveData(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년MM월dd일"))) // 선텍된 날짜
    val date : LiveData<String>
    get() = _date
    fun setDate(date : String){
        _date.value = date
    }

    private val _diaryItems = MutableLiveData<MutableList<DiaryItem>>() // 기록된 음식
    val diaryItems : LiveData<MutableList<DiaryItem>>
    get() = _diaryItems
    fun setDiaryItems(items: MutableList<DiaryItem>) {
        _diaryItems.value = items
    }

    private val _goalCalorie = MutableLiveData<String>("0") // 목표 칼로리
    val goalCalorie : LiveData<String>
    get() = _goalCalorie
    fun setGoalCalorie(calorie : String){
        _goalCalorie.value = calorie
    }

    private val _spendCalories = MutableLiveData<String>("0") // 소비 칼로리
    val spendCalories : LiveData<String>
    get() = _spendCalories
    fun setSpendCalorie(calorie: String){
        _spendCalories.value = calorie
    }

    private val _amountCarbon  = MutableLiveData<String>("0") // 총 탄수화물
    val amountCarbon : LiveData<String>
    get() = _amountCarbon
    fun setAmountCarbon(carbon : String){
        _amountCarbon.value = carbon
    }

    private val _amountProtein  = MutableLiveData<String>("0") // 총 단백질
    val amountProtein : LiveData<String>
    get() = _amountProtein
    fun setAmountProtein(protein : String){
        _amountProtein.value = protein
    }

    private val _amountFat  = MutableLiveData<String>("0") // 총 지방
    val amountFat : LiveData<String>
    get() = _amountFat
    fun setAmountFat(fat : String) {
        _amountFat.value = fat
    }

    private val _calorieProgress = MutableLiveData<Double>(0.0) // 칼로리 소비량 프로그래스
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
    fun setIsProgress(progress : Boolean){
        _isProgress.value = progress
    }

    private val _isNightMode = MutableLiveData<Boolean>(false)
    val isNightMode : LiveData<Boolean>
    get() = _isNightMode
    fun setIsNightMode(isNightMode : Boolean) {
        _isNightMode.value = isNightMode
    }

    /**
     * 섭취한 음식 불러오기
     * **/
     fun getDiaryItems(){
         viewModelScope.launch {
             _isProgress.value = true
             val diaryItem = withContext(Dispatchers.IO){
                 getAllDiaryItems.invoke(date.value!!)
             }
            _diaryItems.value = diaryItem
        }
    }

    /**
     * 다이어리 정보 DB에 기록하기
     * **/
    fun addDiarySummary() {
        addDiaryJob = CoroutineScope(Dispatchers.IO).launch {
            val diary = Diary(
                date.value!!,
                spendCalories.value!!,
                amountCarbon.value!!,
                amountProtein.value!!,
                amountFat.value!!
            )
            addDiary.invoke(diary)
            Log.d(TAG, "addDiarySummary: success add diary DB!!")
        }
    }

    /**
     * 선택된 음식 삭제 하기
     * **/
    fun deleteSelectedDiaryItem(diaryItem: DiaryItem){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                deleteDiaryItem.invoke(diaryItem)
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
package com.lee.foodi.ui.fragments.diary.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.foodi.data.repository.FoodiRepository
import com.lee.foodi.data.room.db.DiaryDatabase
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
    var date = MutableLiveData<String>(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년MM월dd일")))
    var diaryItems = MutableLiveData<MutableList<DiaryItem>>()
    var goalCalorie = MutableLiveData<String>("0")
    var spendCalories = MutableLiveData<String>("0")
    var amountCarbon  = MutableLiveData<String>("0")
    var amountProtein  = MutableLiveData<String>("0")
    var amountFat  = MutableLiveData<String>("0")
    var calorieProgress = MutableLiveData<Double>(0.0)

    suspend fun getDiaryItems(date : String) : MutableList<DiaryItem>{
        val ret = CoroutineScope(Dispatchers.IO).async {
             mRepository.getAllDiaryItems(date)
        }
        return ret.await()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun addDiarySummary() {
        Log.d(TAG, "addDiarySummary()")
        CoroutineScope(Dispatchers.IO).launch {
            val db = DiaryDatabase.getInstance()
            val diary = DiaryEntity(date.value!! 
                , spendCalories.value!! 
                , amountCarbon.value!! 
                , amountProtein.value!! 
                , amountFat.value!!)
            db.diaryDao().addDiary(diary)
            Log.d(TAG, "addDiarySummary: success add diary DB!!")
        }
    }
}
package com.lee.foodi.ui.fragments.diary.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.foodi.data.repository.FoodiRepository
import com.lee.foodi.data.room.entity.DiaryItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val TAG = "DiaryViewModel"

class DiaryViewModel(repository: FoodiRepository) : ViewModel() {
    private var mRepository : FoodiRepository

    init {
        mRepository = repository
    }

    var date = MutableLiveData<String>()
    var diaryItems = MutableLiveData<MutableList<DiaryItem>>()
    var goalCalorie = MutableLiveData<String>()
    var spendCalories = MutableLiveData<String>("0")
    var amountCarbon  = MutableLiveData<String>("0")
    var amountProtein  = MutableLiveData<String>("0")
    var amountFat  = MutableLiveData<String>("0")

    suspend fun getDiaryItems(date : String) : MutableList<DiaryItem>{
        val ret = CoroutineScope(Dispatchers.IO).async {
             mRepository.getAllDiaryItems(date)
        }
        return ret.await()
    }
}
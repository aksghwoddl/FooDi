package com.lee.foodi.ui.fragments.diary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.foodi.data.repository.FoodiRepository
import com.lee.foodi.data.room.entity.DiaryItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiaryViewModel(repository: FoodiRepository) : ViewModel() {
    private var mRepository : FoodiRepository

    init {
        mRepository = repository
    }

    var date = MutableLiveData<String>()
    var diaryItems = MutableLiveData<MutableList<DiaryItem>>()
    var goalCalorie = MutableLiveData<String>()
    var spendCalories = MutableLiveData<String>()

    suspend fun getDiaryItems(date : String){
        CoroutineScope(Dispatchers.IO).launch {
            diaryItems.postValue(mRepository.getAllDiaryItems(date))
        }
    }
}
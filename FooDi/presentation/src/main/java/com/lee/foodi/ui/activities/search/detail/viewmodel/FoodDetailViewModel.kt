package com.lee.foodi.ui.activities.search.detail.viewmodel

import androidx.lifecycle.ViewModel
import com.lee.domain.model.local.DiaryItem
import com.lee.domain.model.remote.Food
import com.lee.domain.usecase.AddDiaryItem
import com.lee.foodi.common.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * 음식 상세정보 ViewModel
 * **/
@HiltViewModel
class FoodDetailViewModel @Inject constructor(
    private val addDiaryItem: AddDiaryItem ,
    private val resourceProvider: ResourceProvider
) : ViewModel() {
    /**
     * 다이어리에 음식 추가하는 함수
     * **/
    fun addFoodIntoDatabase(date : String , servingSize : String , food : Food) {
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH : mm"))
        val queryFood = DiaryItem(null, date, food, time, servingSize)
        CoroutineScope(Dispatchers.IO).launch{
            addDiaryItem.invoke(queryFood)
        }
    }
}
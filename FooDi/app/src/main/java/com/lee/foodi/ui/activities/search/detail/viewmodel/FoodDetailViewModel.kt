package com.lee.foodi.ui.activities.search.detail.viewmodel

import androidx.lifecycle.ViewModel
import com.lee.foodi.common.EXTRA_SELECTED_DATE
import com.lee.foodi.common.ResourceProvider
import com.lee.foodi.data.rest.model.Food
import com.lee.foodi.data.room.entity.DiaryItemEntity
import com.lee.foodi.domain.FoodiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class FoodDetailViewModel @Inject constructor(
    private val repository: FoodiRepository ,
    private val resourceProvider: ResourceProvider
) : ViewModel() {
    /**
     * 다이어리에 음식 추가하는 함수
     * **/
    fun addFoodIntoDatabase(date : String , servingSize : String , food : Food) {
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH : mm"))
        val queryFood = DiaryItemEntity(null , date , food , time , servingSize)
        CoroutineScope(Dispatchers.IO).launch{
            repository.addDiaryItem(queryFood)
        }
    }
}
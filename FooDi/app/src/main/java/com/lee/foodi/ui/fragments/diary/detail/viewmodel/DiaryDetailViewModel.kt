package com.lee.foodi.ui.fragments.diary.detail.viewmodel

import androidx.lifecycle.ViewModel
import com.lee.foodi.common.ResourceProvider
import com.lee.foodi.data.room.entity.DiaryItem
import com.lee.foodi.data.room.entity.DiaryItemEntity
import com.lee.foodi.domain.FoodiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryDetailViewModel @Inject constructor(
    private val repository: FoodiRepository ,
    private val resourceProvider: ResourceProvider
) : ViewModel() {
    /**
     * 다이어리에 음식을 추가하는 함수
     * **/
    fun updateDiaryItem(diaryItem: DiaryItem , servingSize : String) {
        CoroutineScope(Dispatchers.IO).launch {
            val diaryItemEntity = DiaryItemEntity(
                diaryItem.index
                , diaryItem.date
                , diaryItem.food
                , diaryItem.time
                , servingSize
            )
            repository.updateDiaryItem(diaryItemEntity)
        }
    }

}
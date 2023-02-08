package com.lee.foodi.ui.fragments.diary.detail.viewmodel

import androidx.lifecycle.ViewModel
import com.lee.domain.model.local.DiaryItem
import com.lee.domain.model.local.DiaryItemEntity
import com.lee.foodi.common.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 다이어리 음식의 상세정보 ViewModel
 * **/
@HiltViewModel
class DiaryDetailViewModel @Inject constructor(
    private val repository: com.lee.domain.repository.FoodiRepository,
    private val resourceProvider: ResourceProvider
) : ViewModel() {
    /**
     * 다이어리에 음식을 추가하는 함수
     * **/
    fun updateDiaryItem(diaryItem: DiaryItem, servingSize : String) {
        CoroutineScope(Dispatchers.IO).launch {
            val diaryItemEntity = DiaryItemEntity(
                diaryItem.index, diaryItem.date, diaryItem.food, diaryItem.time, servingSize
            )
            repository.updateDiaryItem(diaryItemEntity)
        }
    }

}
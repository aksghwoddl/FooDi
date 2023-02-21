package com.lee.foodi.ui.fragments.diary.detail.viewmodel

import androidx.lifecycle.ViewModel
import com.lee.domain.model.local.DiaryItem
import com.lee.domain.usecase.UpdateDiaryItem
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
    private val updateDiaryItem : UpdateDiaryItem ,
    private val resourceProvider: ResourceProvider
) : ViewModel() {
    /**
     * 다이어리 음식을 업데이트 하는 함수
     * **/
    fun updateDiaryItem(diaryItem: DiaryItem, servingSize : String) {
        CoroutineScope(Dispatchers.IO).launch {
            val updateItem = diaryItem.run {
                DiaryItem(index, date, food, time , servingSize)
            }
            updateDiaryItem.invoke(updateItem)
        }
    }

}
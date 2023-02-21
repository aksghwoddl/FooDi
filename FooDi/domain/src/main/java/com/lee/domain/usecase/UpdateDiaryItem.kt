package com.lee.domain.usecase

import com.lee.domain.model.local.DiaryItem
import com.lee.domain.repository.FoodiRepository
import javax.inject.Inject

/**
 * 다이어리에 기록된 음식 업데이트하기
 * **/
class UpdateDiaryItem @Inject constructor(
    private val repository: FoodiRepository
) {
    suspend fun invoke(diaryItem: DiaryItem) = repository.updateDiaryItem(diaryItem)
}
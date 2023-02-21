package com.lee.domain.usecase

import com.lee.domain.model.local.DiaryItem
import com.lee.domain.repository.FoodiRepository
import javax.inject.Inject

/**
 * 다이러이의 음식 삭제하기
 * **/
class DeleteDiaryItem @Inject constructor(
    private val repository: FoodiRepository
) {
    suspend fun invoke(diaryItem : DiaryItem) = repository.deleteDiaryItem(diaryItem)
}
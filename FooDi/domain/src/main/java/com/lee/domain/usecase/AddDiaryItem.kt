package com.lee.domain.usecase

import com.lee.domain.model.local.DiaryItemEntity
import com.lee.domain.repository.FoodiRepository
import javax.inject.Inject

/**
 * 다이어리에 음식 추가하기
 * **/
class AddDiaryItem @Inject constructor(
    private val repository : FoodiRepository
) {
    suspend fun invoke(diaryItemEntity: DiaryItemEntity) = repository.addDiaryItem(diaryItemEntity)
}
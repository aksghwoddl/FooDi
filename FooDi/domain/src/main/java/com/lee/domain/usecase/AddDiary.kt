package com.lee.domain.usecase

import com.lee.domain.model.local.Diary
import com.lee.domain.repository.FoodiRepository
import javax.inject.Inject

/**
 * 다이어리 추가하기
 * **/
class AddDiary @Inject constructor(
    private val repository: FoodiRepository
) {
    suspend fun invoke(diary : Diary) = repository.addDiary(diary)
}
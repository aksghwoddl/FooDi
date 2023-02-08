package com.lee.domain.usecase

import com.lee.domain.model.local.DiaryItem
import com.lee.domain.repository.FoodiRepository
import javax.inject.Inject

/**
 * 날짜를 기준으로 기록한 음식 조회하기
 * **/
class GetAllDiaryItems @Inject constructor(
    private val repository: FoodiRepository
) {
    suspend fun invoke(date : String) : MutableList<DiaryItem> = repository.getAllDiaryItems(date)
}
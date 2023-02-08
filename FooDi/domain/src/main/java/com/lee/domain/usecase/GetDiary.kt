package com.lee.domain.usecase

import com.lee.domain.repository.FoodiRepository
import javax.inject.Inject

/**
 * 날짜를 기준으로 다이어리 정보 가져오기
 * **/
class GetDiary @Inject constructor(
    private val repository: FoodiRepository
) {
    suspend fun invoke(date : String) = repository.getDiaryByDate(date)
}
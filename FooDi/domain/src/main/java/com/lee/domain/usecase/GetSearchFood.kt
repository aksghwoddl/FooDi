package com.lee.domain.usecase

import com.lee.domain.repository.FoodiRepository
import javax.inject.Inject

/**
 * 음식 검색하기
 * **/
class GetSearchFood @Inject constructor(
    private val repository: FoodiRepository
) {
    suspend fun invoke(foodName : String , page : String) = repository.getSearchFood(foodName , page)
}
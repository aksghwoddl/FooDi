package com.lee.domain.usecase

import com.lee.domain.model.remote.AddingFoodRequest
import com.lee.domain.repository.FoodiRepository
import javax.inject.Inject

/**
 * 새로운 음식 추가하기
 * **/
class AddNewFood @Inject constructor(
    private val repository: FoodiRepository
) {
    suspend fun invoke(food : AddingFoodRequest) = repository.addNewFood(food)
}
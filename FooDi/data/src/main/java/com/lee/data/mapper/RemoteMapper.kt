package com.lee.data.mapper

import com.lee.data.model.remote.SearchingFoodResponse
import com.lee.domain.model.remote.Food
import com.lee.domain.model.remote.SearchingFood

/**
 * Remote data를 mapping 해주는 class (현재 프로젝트에서는 사용하지 않음)
 * **/
object RemoteMapper {
    fun mapperToSearchingFood(response : SearchingFoodResponse) : SearchingFood{
        val results = mutableListOf<Food>()
        response.results.forEach {
            val food = Food(
                it.id ,
                it.foodName ,
                it.servingWeight ,
                it.calorie ,
                it.carbohydrate ,
                it.protein ,
                it.fat ,
                it.sugar ,
                it.salt ,
                it.cholesterol ,
                it.saturatedFat ,
                it.transFat ,
                it.company
            )
            results.add(food)
        }
        return SearchingFood(
            pageNo =  response.pageNo ,
            totalCount = response.totalCount ,
            results
        )
    }
}
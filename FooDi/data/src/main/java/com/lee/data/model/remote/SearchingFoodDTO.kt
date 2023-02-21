package com.lee.data.model.remote

/**
 * 검색한 음식에 대한 응답 DTO class
 * **/
data class SearchingFoodDTO(
    val pageNo : Int ,
    val totalCount : Int ,
    val results : MutableList<FoodDTO>
)
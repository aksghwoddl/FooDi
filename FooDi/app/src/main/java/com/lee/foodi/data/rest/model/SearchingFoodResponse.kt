package com.lee.foodi.data.rest.model

data class SearchingFoodResponse(
    val pageNo : Int ,
    val totalCount : Int ,
    val results : MutableList<Food>
)
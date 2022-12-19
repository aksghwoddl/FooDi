package com.lee.foodi.data.rest.model

data class NewFoodData(
    val pageNo : Int ,
    val totalCount : Int ,
    val results : MutableList<FoodInfoData>
)
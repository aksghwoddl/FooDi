package com.lee.domain.model.remote

data class SearchingFood(
    val pageNo : Int ,
    val totalCount : Int ,
    val results : MutableList<Food>
)
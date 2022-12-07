package com.lee.foodi.data.rest.model

class FoodData {
    val header : Header? = null
    val body : Body? = null
}


data class Header(
    val resultCode : String ,
    val resultMessage : String
)

data class Body(
    val pageNo : Int ,
    val totalCount : Int ,
    val numOfRows : Int ,
    val items : MutableList<FoodInfoData>
)

data class NewFoodData( // Convert New Food InfoData
    val pageNo : Int ,
    val totalCount : Int ,
    val results : MutableList<FoodInfoData>
)
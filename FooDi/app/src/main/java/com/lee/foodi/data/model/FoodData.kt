package com.lee.foodi.data.model

class FoodData (
    val header : Header,
    val body : Body
)


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
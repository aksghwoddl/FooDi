package com.lee.foodi.data.room.entity

import com.lee.foodi.data.rest.model.FoodInfoData

data class DiaryItem(
    var index : Int? = 0 ,
    var date : String = "" ,
    var food : FoodInfoData? ,
    var time : String = "" ,
    var servingSize : String = ""
)
package com.lee.foodi.data.room

import com.google.gson.Gson
import com.lee.foodi.data.rest.model.FoodInfoData

class TypeConverter {
    @androidx.room.TypeConverter
    fun foodInfoToJson(food : FoodInfoData) : String = Gson().toJson(food)

    @androidx.room.TypeConverter
    fun jsonToFoodInfo(json : String) : FoodInfoData = Gson().fromJson(json , FoodInfoData::class.java)
}
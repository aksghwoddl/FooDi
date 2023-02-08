package com.lee.foodi.data.room

import com.google.gson.Gson
import com.lee.foodi.data.rest.model.Food

class TypeConverter {
    @androidx.room.TypeConverter
    fun foodInfoToJson(food : Food) : String = Gson().toJson(food)

    @androidx.room.TypeConverter
    fun jsonToFoodInfo(json : String) : Food = Gson().fromJson(json , Food::class.java)
}
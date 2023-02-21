package com.lee.data.model.local

import com.google.gson.Gson
import com.lee.domain.model.remote.Food

/**
 * Room의 data를 converting하기위한 TypeConverter class
 * **/
class TypeConverter {
    @androidx.room.TypeConverter
    fun foodInfoToJson(food : Food) : String = Gson().toJson(food)

    @androidx.room.TypeConverter
    fun jsonToFoodInfo(json : String) : Food = Gson().fromJson(json , Food::class.java)
}
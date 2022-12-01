package com.lee.foodi.data.repository

import com.lee.foodi.common.FoodiNewApplication
import com.lee.foodi.data.rest.RestServiceInstance
import com.lee.foodi.data.rest.model.FoodInfoData
import com.lee.foodi.data.room.dao.DiaryDAO
import com.lee.foodi.data.room.db.DiaryDatabase

class FoodiRepository {
    suspend fun getSearchFood(foodName : String , page : String) = RestServiceInstance.getInstance().getSearchFood(foodName , page)
}
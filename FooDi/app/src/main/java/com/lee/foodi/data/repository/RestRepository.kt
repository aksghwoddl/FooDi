package com.lee.foodi.data.repository

import com.lee.foodi.data.rest.RestServiceInstance

class RestRepository {
    suspend fun getSearchFood(foodName : String , page : String) = RestServiceInstance.getInstance().getSearchFood(foodName , page)
}
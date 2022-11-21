package com.lee.foodi.data.repository

import com.lee.foodi.data.rest.RestServiceInstance

class FoodiRepository {
    suspend fun getSearchFood(key : String , foodName : String , page : String) = RestServiceInstance.getInstance().getSearchFood(key , foodName , page)
}
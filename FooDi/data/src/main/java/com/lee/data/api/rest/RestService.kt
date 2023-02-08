package com.lee.data.api.rest

import com.lee.data.common.DESCRIPTION_KOREAN
import com.lee.data.common.FOOD_TARGET_URL
import com.lee.data.common.PAGE_NUMBER
import com.lee.domain.model.remote.SearchingFood
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Rest 통신을 위한 API
 * **/
interface RestService {
    @GET(FOOD_TARGET_URL)
    suspend fun getNewSearchFood(
        @Query(DESCRIPTION_KOREAN) foodName : String
        , @Query(PAGE_NUMBER) page : String
    ) : SearchingFood

    @POST(FOOD_TARGET_URL)
    suspend fun addNewFood(@Body foodData : com.lee.domain.model.remote.AddingFoodRequest) : Response<Void>
}
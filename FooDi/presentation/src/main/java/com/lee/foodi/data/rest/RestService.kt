package com.lee.foodi.data.rest

import com.lee.foodi.common.CONNECTION_TIME_OUT
import com.lee.foodi.common.DESCRIPTION_KOREAN
import com.lee.foodi.common.FOOD_TARGET_URL
import com.lee.foodi.common.PAGE_NUMBER
import com.lee.foodi.data.rest.model.AddingFood
import com.lee.foodi.data.rest.model.SearchingFoodResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface RestService {
    @GET(FOOD_TARGET_URL)
    suspend fun getNewSearchFood(
        @Query(DESCRIPTION_KOREAN) foodName : String
        , @Query(PAGE_NUMBER) page : String
    ) : Response<SearchingFoodResponse>

    @POST(FOOD_TARGET_URL)
    suspend fun addNewFood(@Body foodData : AddingFood) : Response<Void>
}
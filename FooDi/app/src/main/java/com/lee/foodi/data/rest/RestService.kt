package com.lee.foodi.data.rest

import com.lee.foodi.common.CONNECTION_TIME_OUT
import com.lee.foodi.common.FOOD_TARGET_URL
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
        @Query("desc_kor") foodName : String
        , @Query("pageNo") page : String
    ) : Response<SearchingFoodResponse>

    @POST(FOOD_TARGET_URL)
    suspend fun addNewFood(@Body foodData : AddingFood) : Response<Void>
}

class RestServiceInstance{
    companion object{
        private lateinit var restService : RestService

        fun getInstance() : RestService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level= HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(CONNECTION_TIME_OUT , TimeUnit.MILLISECONDS)
                .build()

            if(!::restService.isInitialized){
                val retrofit = Retrofit.Builder()
                    .baseUrl(FOOD_TARGET_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                restService = retrofit.create(RestService::class.java)
            }
            return restService
        }
    }
}
package com.lee.foodi.data.rest

import com.lee.foodi.common.FOOD_TARGET_URL
import com.lee.foodi.common.SERVICE_KEY
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


interface RestService {
    @GET("serviceKey=$SERVICE_KEY&desc_kor={foodName} &pageNo={page}&numOfRows=10&bgn_year=&animal_plant=&type=json")
    fun getSearchFood(@Path("foodName") foodName : String
                      , @Path("page") page : String)
}

class RestServiceInstance{
    companion object{
        private lateinit var restService : RestService

        fun getInstance() : RestService {
            if(!::restService.isInitialized){
                val retrofit = Retrofit.Builder()
                    .baseUrl(FOOD_TARGET_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                restService = retrofit.create(RestService::class.java)
            }
            return restService
        }
    }
}
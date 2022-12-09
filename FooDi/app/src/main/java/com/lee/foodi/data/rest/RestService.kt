package com.lee.foodi.data.rest

import com.lee.foodi.common.FOOD_TARGET_URL
import com.lee.foodi.common.NEW_FOOD_TARGET_URL
import com.lee.foodi.data.rest.model.NewFoodData
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//private const val SERVICE_KEY = "kSh3QnPHhrIKWvXEVaiTxPZRMErAsaXXy7Xszy%2FCBI7YSBsbd4S0Vfrf5KRnoZhT9GhcQ9L9fYMbbaBEG33dXA%3D%3D"
interface RestService {
   /* @GET("getFoodNtrItdntList1?serviceKey=$SERVICE_KEY&numOfRows=10&bgn_year=&animal_plant=&type=json")
    suspend fun getSearchFood(
        @Query("desc_kor") foodName : String
        , @Query("pageNo") page : String
        ) : Response<FoodData>*/ // Convert New Food InfoData

    @GET(NEW_FOOD_TARGET_URL)
    suspend fun getNewSearchFood(
        @Query("desc_kor") foodName : String
        , @Query("pageNo") page : String
    ) : Response<NewFoodData>
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
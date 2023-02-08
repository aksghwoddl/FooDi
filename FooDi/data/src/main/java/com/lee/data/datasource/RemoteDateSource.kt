package com.lee.data.datasource

import com.lee.data.api.rest.RestService
import com.lee.domain.model.remote.SearchingFood
import retrofit2.Response
import javax.inject.Inject

interface RemoteDateSource {
    suspend fun getSearchFood(foodName : String , pageNo : String) : SearchingFood

    suspend fun addNewFood(addingFood: com.lee.domain.model.remote.AddingFoodRequest) : Response<Void>
}

class RemoteDataSourceImpl @Inject constructor(
    private val restService: RestService
) : RemoteDateSource {
    override suspend fun getSearchFood(foodName: String, pageNo: String) = restService.getNewSearchFood(foodName , pageNo)

    override suspend fun addNewFood(addingFood: com.lee.domain.model.remote.AddingFoodRequest) = restService.addNewFood(addingFood)
}
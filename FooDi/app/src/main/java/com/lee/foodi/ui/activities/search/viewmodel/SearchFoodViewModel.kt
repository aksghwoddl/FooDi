package com.lee.foodi.ui.activities.search.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.foodi.data.rest.model.FoodData
import com.lee.foodi.data.rest.model.FoodInfoData
import com.lee.foodi.data.repository.FoodiRepository
import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import kotlin.math.ceil

class SearchFoodViewModel(private val repository: FoodiRepository) : ViewModel() {
    private val TAG = "FoodInfoViewModel"

    private var mJob : Job? = null
    var foodResponse : FoodData? = null
    var foodList = MutableLiveData<MutableList<FoodInfoData>>() // Food List that searched
    var errorMessage = MutableLiveData<String>() // Management about error
    var isProgressVisible = MutableLiveData<Boolean>() // Management Progressbar state
    var addFoodLayoutVisible = MutableLiveData<Boolean>() // Manage status that no food
    var isNextEnable = MutableLiveData<Boolean>()
    var isPreviousEnable = MutableLiveData<Boolean>()

    /**
     * For Get FoodList that searched from repository
     * **/

    fun getSearchFoodList(foodName : String , page : String) {
        mJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                isProgressVisible.postValue(true)
                if(foodName.isNotEmpty()){
                    val response = repository.getSearchFood(foodName , page)
                    foodResponse = response.body()
                    withContext(Dispatchers.Main) {
                        if(response.isSuccessful){
                            foodList.value = foodResponse?.body?.items
                            isProgressVisible.postValue(false)
                            val totalCount = foodResponse?.body?.totalCount!!
                            isNextEnable.value = totalCount >= 10 && ceil(totalCount.toDouble())/10 > page.toInt()
                        } else {
                            Log.d(TAG, "getSearchFoodList: ${response.message()}")
                            errorMessage.postValue("서버에서 정상적으로 값을 받아오지 못했습니다!!")
                            isProgressVisible.postValue(false)
                        }
                    }
                } else {
                    errorMessage.postValue("검색어가 입력되지 않았습니다!")
                    isProgressVisible.postValue(false)
                }
            }catch (socketTimeOutException : SocketTimeoutException){
                errorMessage.postValue("서버와 통신시간이 초과하였습니다!! 다시 시도해주세요.")
                isProgressVisible.postValue(false)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mJob?.cancel()
    }
}
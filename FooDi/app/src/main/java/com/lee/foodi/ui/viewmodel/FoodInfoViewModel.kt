package com.lee.foodi.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.foodi.data.model.FoodInfoData
import com.lee.foodi.data.repository.RestRepository
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class FoodInfoViewModel(private val repository: RestRepository) : ViewModel() {
    private var mJob : Job? = null
    var foodList = MutableLiveData<MutableList<FoodInfoData>>()
    var errorMessage = MutableLiveData<String>()
    var isProgressVisible = MutableLiveData<Boolean>()
    var addFoodLayoutVisible = MutableLiveData<Boolean>()

    fun getSearchFoodList(foodName : String , page : String) {
        mJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                isProgressVisible.postValue(true)
                if(foodName.isNotEmpty()){
                    val response = repository.getSearchFood(foodName , page)
                    withContext(Dispatchers.Main) {
                        if(response.isSuccessful){
                            foodList.postValue(response.body()?.body?.items)
                            isProgressVisible.postValue(false)
                        } else {
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
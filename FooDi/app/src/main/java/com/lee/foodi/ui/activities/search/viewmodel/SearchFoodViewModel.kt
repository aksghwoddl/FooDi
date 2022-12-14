package com.lee.foodi.ui.activities.search.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.foodi.data.repository.FoodiRepository
import com.lee.foodi.data.rest.model.FoodInfoData
import com.lee.foodi.data.rest.model.NewFoodData
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

private const val TAG = "FoodInfoViewModel"

class SearchFoodViewModel(private val repository: FoodiRepository) : ViewModel() {
    private var mJob : Job? = null
    private var foodResponse : NewFoodData? = null
    val foodList = MutableLiveData<MutableList<FoodInfoData>>() // Food List that searched
    val errorMessage = MutableLiveData<String>() // Management about error
    val isProgressVisible = MutableLiveData<Boolean>() // Management Progressbar state
    val addFoodLayoutVisible = MutableLiveData<Boolean>() // Manage status that no food
    val isNextEnable = MutableLiveData<Boolean>() // Manage next button enable
    val isPreviousEnable = MutableLiveData<Boolean>() // Manage previous button enable
    val isNightMode = MutableLiveData<Boolean>(false) // check night mode

    /**
     * For Get FoodList that searched from repository
     * **/
    fun getSearchFoodList(foodName : String , page : String) {
        mJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                isProgressVisible.postValue(true)
                if(foodName.isNotEmpty()){
                    val response = repository.getNewSearchFood(foodName , page)
                    foodResponse = response.body()
                    withContext(Dispatchers.Main) {
                        if(response.isSuccessful){
                            foodList.value = foodResponse?.results
                            isProgressVisible.postValue(false)
                            val totalCount = foodResponse?.totalCount!!
                            isNextEnable.value = totalCount >= 1 && foodResponse!!.totalCount > page.toInt()
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
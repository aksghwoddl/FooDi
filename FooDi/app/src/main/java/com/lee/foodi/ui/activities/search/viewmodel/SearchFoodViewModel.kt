package com.lee.foodi.ui.activities.search.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.foodi.R
import com.lee.foodi.common.FoodiNewApplication
import com.lee.foodi.data.repository.FoodiRepository
import com.lee.foodi.data.rest.model.Food
import com.lee.foodi.data.rest.model.SearchingFoodResponse
import kotlinx.coroutines.*
import java.net.ConnectException
import java.net.SocketTimeoutException

private const val TAG = "FoodInfoViewModel"

class SearchFoodViewModel(private val repository: FoodiRepository) : ViewModel() {
    private var mJob : Job? = null
    private var foodResponse : SearchingFoodResponse? = null
    val foodList = MutableLiveData<MutableList<Food>>() // Food List that searched
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
                            errorMessage.postValue(FoodiNewApplication.getInstance().getString(R.string.response_fail))
                            isProgressVisible.postValue(false)
                        }
                    }
                } else {
                    errorMessage.postValue(FoodiNewApplication.getInstance().getString(R.string.empty_search))
                    isProgressVisible.postValue(false)
                }
            } catch (socketTimeOutException : SocketTimeoutException){
                errorMessage.postValue(FoodiNewApplication.getInstance().getString(R.string.check_socket_timeout))
                isProgressVisible.postValue(false)
            } catch(connectException : ConnectException){
                errorMessage.postValue(FoodiNewApplication.getInstance().getString(R.string.check_server_connection))
                isProgressVisible.postValue(false)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mJob?.cancel()
    }
}
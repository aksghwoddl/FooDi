package com.lee.foodi.ui.activities.search.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.foodi.R
import com.lee.foodi.common.FoodiNewApplication
import com.lee.foodi.data.repository.FoodiRepository
import com.lee.foodi.data.rest.model.Food
import com.lee.foodi.data.rest.model.SearchingFoodResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException

private const val TAG = "FoodInfoViewModel"

class SearchFoodViewModel(private val repository: FoodiRepository) : ViewModel() {
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
        Log.d(TAG, "getSearchFoodList()")
            if(foodName.isNotEmpty()){
                try{
                    var response : Response<SearchingFoodResponse>? = null
                    viewModelScope.launch {
                        try{
                            response = withContext(Dispatchers.IO){
                                isProgressVisible.postValue(true)
                                repository.getNewSearchFood(foodName , page)
                            }
                        } catch(connectException : ConnectException) { // 서버가 켜져 있지 않을때
                            errorMessage.postValue(FoodiNewApplication.getInstance().getString(R.string.check_server_connection))
                            isProgressVisible.postValue(false)
                        }
                        response?.let { // 서버가 켜져 있고 Response를 전달 받았을때
                            if(it.isSuccessful){
                                it.body()?.let { searchResponse ->
                                    foodList.value = searchResponse.results
                                    isProgressVisible.value = false
                                    val totalCount = it.body()?.totalCount!!
                                    isNextEnable.value = totalCount >= 1 && searchResponse.totalCount > page.toInt()
                                }
                            } else {
                                Log.d(TAG, "getSearchFoodList: ${it.code()}")
                                errorMessage.value = FoodiNewApplication.getInstance().getString(R.string.response_fail)
                                isProgressVisible.value = false
                            }
                        }
                    }
                }  catch (socketTimeOutException : SocketTimeoutException){ // 통신시간 초과
                    errorMessage.postValue(FoodiNewApplication.getInstance().getString(R.string.check_socket_timeout))
                    isProgressVisible.postValue(false)
                }
            } else {
                errorMessage.postValue(FoodiNewApplication.getInstance().getString(R.string.empty_search))
                isProgressVisible.value = false
            }
    }
}
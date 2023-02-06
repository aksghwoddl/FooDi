package com.lee.foodi.ui.activities.search.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.foodi.R
import com.lee.foodi.common.ResourceProvider
import com.lee.foodi.data.rest.model.Food
import com.lee.foodi.data.rest.model.SearchingFoodResponse
import com.lee.foodi.domain.FoodiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

private const val TAG = "FoodInfoViewModel"

@HiltViewModel
class SearchFoodViewModel @Inject constructor(
    private val repository: FoodiRepository ,
    private val resourceProvider: ResourceProvider
) : ViewModel() {
    private val _foodList = MutableLiveData<MutableList<Food>>() // Food List that searched
    val foodList : LiveData<MutableList<Food>>
    get() = _foodList
    fun setFoodList(list : MutableList<Food>) {
        _foodList.value = list
    }

    private val _addFoodLayoutVisible = MutableLiveData<Boolean>() // Manage status that no food
    val addFoodLayoutVisible : LiveData<Boolean>
    get() = _addFoodLayoutVisible
    fun setAddFoodLayoutVisible(visible: Boolean){
        _addFoodLayoutVisible.value = visible
    }

    private val _isNextEnable = MutableLiveData<Boolean>() // Manage next button enable
    val isNextEnable : LiveData<Boolean>
    get() = _isNextEnable
    fun setNextEnable(enable : Boolean){
        _isNextEnable.value = enable
    }

    private val _isPreviousEnable = MutableLiveData<Boolean>() // Manage previous button enable
    val isPreviousEnable : LiveData<Boolean>
    get() = _isPreviousEnable
    fun setPreviousEnable(enable : Boolean) {
        _isPreviousEnable.value = enable
    }

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage : LiveData<String>
        get() = _toastMessage
    fun setToastMessage(message : String){
        _toastMessage.value = message
    }

    private val _isProgress = MutableLiveData<Boolean>()
    val isProgress : LiveData<Boolean>
        get() = _isProgress
    fun setIsProgress(progress : Boolean){
        _isProgress.value = progress
    }

    private val _isNightMode = MutableLiveData<Boolean>(false)
    val isNightMode : LiveData<Boolean>
        get() = _isNightMode
    fun setIsNightMode(isNightMode : Boolean) {
        _isNightMode.value = isNightMode
    }

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
                            setIsProgress(true)
                            response = withContext(Dispatchers.IO){
                                repository.getNewSearchFood(foodName , page)
                            }
                        } catch(connectException : ConnectException) { // 서버가 켜져 있지 않을때
                            setToastMessage(resourceProvider.getString(R.string.check_server_connection))
                            setIsProgress(false)
                        }
                        response?.let { // 서버가 켜져 있고 Response를 전달 받았을때
                            if(it.isSuccessful){
                                it.body()?.let { searchResponse ->
                                    _foodList.value = searchResponse.results
                                    setIsProgress(false)
                                    val totalCount = it.body()?.totalCount!!
                                    _isNextEnable.value = totalCount >= 1 && searchResponse.totalCount > page.toInt()
                                }
                            } else {
                                Log.d(TAG, "getSearchFoodList: ${it.code()}")
                                setToastMessage(resourceProvider.getString(R.string.response_fail))
                                setIsProgress(false)
                            }
                        }
                    }
                }  catch (socketTimeOutException : SocketTimeoutException){ // 통신시간 초과
                    setToastMessage(resourceProvider.getString(R.string.check_socket_timeout))
                    setIsProgress(false)
                }
            } else {
                setToastMessage(resourceProvider.getString(R.string.empty_search))
                setIsProgress(false)
            }
    }
}
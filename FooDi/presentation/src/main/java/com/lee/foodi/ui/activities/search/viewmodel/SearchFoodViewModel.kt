package com.lee.foodi.ui.activities.search.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.domain.model.remote.Food
import com.lee.domain.usecase.GetSearchFood
import com.lee.foodi.R
import com.lee.foodi.common.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * 음식 검색 ViewModel
 * **/
private const val TAG = "SearchFoodViewModel"
@HiltViewModel
class SearchFoodViewModel @Inject constructor(
    private val getSearchFood: GetSearchFood ,
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
                    viewModelScope.launch {
                        setIsProgress(true)
                        val response = withContext(Dispatchers.IO){
                            getSearchFood.invoke(foodName , page)
                        }
                        _foodList.value = response.results
                        val totalCount = response.totalCount
                        _isNextEnable.value = totalCount >= 1 && response.totalCount > page.toInt()
                        setIsProgress(false)
                    }
                } catch (socketTimeOutException : SocketTimeoutException){ // 통신시간 초과
                    setToastMessage(resourceProvider.getString(R.string.check_socket_timeout))
                    setIsProgress(false)
                } catch(connectException : ConnectException) { // 서버가 켜져 있지 않을때
                    setToastMessage(resourceProvider.getString(R.string.check_server_connection))
                    setIsProgress(false)
                }
            } else {
                setToastMessage(resourceProvider.getString(R.string.empty_search))
                setIsProgress(false)
            }
    }
}
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
    val searchFoodName = MutableLiveData<String>()

    private val _foodList = MutableLiveData<MutableList<Food>>() // 검색된 음식 목록
    val foodList : LiveData<MutableList<Food>>
    get() = _foodList
    fun setFoodList(list : MutableList<Food>) {
        _foodList.value = list
    }

    private val _page = MutableLiveData<Int>()
    val page : LiveData<Int>
    get() = _page
    fun setPage(page : Int){
        _page.value = page
    }

    private val _addFoodLayoutVisible = MutableLiveData<Boolean>() // 음식추가 레이아웃 표시여부
    val addFoodLayoutVisible : LiveData<Boolean>
    get() = _addFoodLayoutVisible
    fun setAddFoodLayoutVisible(visible: Boolean){
        _addFoodLayoutVisible.value = visible
    }

    private val _isNextEnable = MutableLiveData<Boolean>() // 다음버튼 활성화 여부
    val isNextEnable : LiveData<Boolean>
    get() = _isNextEnable
    fun setNextEnable(enable : Boolean){
        _isNextEnable.value = enable
    }

    private val _isPreviousEnable = MutableLiveData<Boolean>() // 이전버튼 활성화 여부
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
     * 음식을 검색하는 함수
     * **/
    fun getSearchFoodList() {
        Log.d(TAG, "getSearchFoodList()")
        searchFoodName.value?.let {
            if(it.isNotEmpty()){
                try{
                    viewModelScope.launch {
                        setIsProgress(true)
                        val response = withContext(Dispatchers.IO){
                            getSearchFood.invoke(it , page.value!!.toString())
                        }
                        _foodList.value = response.results
                        val totalCount = response.totalCount
                        _isNextEnable.value = totalCount >= 1 && response.totalCount > page.value!!
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
}
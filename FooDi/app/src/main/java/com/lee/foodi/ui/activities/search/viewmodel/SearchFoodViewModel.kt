package com.lee.foodi.ui.activities.search.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
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
    private val _foodList = MutableLiveData<MutableList<Food>>() // Food List that searched
    val foodList : LiveData<MutableList<Food>>
    get() = _foodList

    fun setFoodList(list : MutableList<Food>) {
        _foodList.value = list
    }

    private val _errorMessage = MutableLiveData<String>() // Management about error
    val errorMessage : LiveData<String>
    get() = _errorMessage

    fun setErrorMessage(message : String){
        _errorMessage.value = message
    }

    private val _isProgressVisible = MutableLiveData<Boolean>() // Management Progressbar state
    val isProgressVisible : LiveData<Boolean>
    get() = _isProgressVisible

    fun setProgressVisible(visible : Boolean) {
        _isProgressVisible.value = visible
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

    private val _isNightMode = MutableLiveData<Boolean>(false) // check night mode
    val isNightMode : LiveData<Boolean>
    get() = _isNightMode

    fun setIsNightMode(isNightMode : Boolean) {
        _isNextEnable.value = isNightMode
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
                            response = withContext(Dispatchers.IO){
                                _isProgressVisible.postValue(true)
                                repository.getNewSearchFood(foodName , page)
                            }
                        } catch(connectException : ConnectException) { // 서버가 켜져 있지 않을때
                            _errorMessage.postValue(FoodiNewApplication.getInstance().getString(R.string.check_server_connection))
                            _isProgressVisible.postValue(false)
                        }
                        response?.let { // 서버가 켜져 있고 Response를 전달 받았을때
                            if(it.isSuccessful){
                                it.body()?.let { searchResponse ->
                                    _foodList.value = searchResponse.results
                                    _isProgressVisible.value = false
                                    val totalCount = it.body()?.totalCount!!
                                    _isNextEnable.value = totalCount >= 1 && searchResponse.totalCount > page.toInt()
                                }
                            } else {
                                Log.d(TAG, "getSearchFoodList: ${it.code()}")
                                _errorMessage.value = FoodiNewApplication.getInstance().getString(R.string.response_fail)
                                _isProgressVisible.value = false
                            }
                        }
                    }
                }  catch (socketTimeOutException : SocketTimeoutException){ // 통신시간 초과
                    _errorMessage.postValue(FoodiNewApplication.getInstance().getString(R.string.check_socket_timeout))
                    _isProgressVisible.postValue(false)
                }
            } else {
                _errorMessage.postValue(FoodiNewApplication.getInstance().getString(R.string.empty_search))
                _isProgressVisible.value = false
            }
    }
}
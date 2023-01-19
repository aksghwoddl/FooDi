package com.lee.foodi.ui.activities.add.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lee.foodi.R
import com.lee.foodi.common.FoodiNewApplication
import com.lee.foodi.common.Utils
import com.lee.foodi.data.repository.FoodiRepository
import com.lee.foodi.data.rest.model.AddingFood
import com.lee.foodi.ui.activities.add.AddFoodActivity
import com.lee.foodi.ui.factory.FoodiViewModelFactory
import kotlinx.coroutines.*
import java.net.ConnectException

/**
 * Singleton Pattern
 * Because of using DataBinding at AddFoodActivity
 * **/
class AddFoodViewModel(private val repository: FoodiRepository) : ViewModel() {

    companion object{
        private lateinit var instance : AddFoodViewModel

        /**
         * Function for get Instance that already created
         * **/
        fun getInstance() : AddFoodViewModel? {
            return if(::instance.isInitialized){
                instance
            } else {
                null
            }
        }
        /**
         * Function for create new Instance
         * **/
        fun newInstance(owner : AddFoodActivity) {
            (!::instance.isInitialized).let {
                instance = ViewModelProvider(owner , FoodiViewModelFactory(
                    FoodiRepository.getInstance()
                ))[AddFoodViewModel::class.java]
            }
        }
    }
    private val _headerTitle = MutableLiveData<String>()
    val headerTitle : LiveData<String>
    get() = _headerTitle

    fun setHeaderTitle(title : String) {
        _headerTitle.value = title
    }

    private val _progress = MutableLiveData<Int>(1)
    val progress : LiveData<Int>
    get() = _progress

    fun setProgress(progress : Int){
        _progress.value = progress
    }

    private val _buttonText = MutableLiveData<String>()
    val buttonText : LiveData<String>
    get() =  _buttonText

    fun setButtonText(text : String){
        _buttonText.value = text
    }

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage : LiveData<String>
    get() = _errorMessage

    fun setErrorMessage(message : String){
        _errorMessage.value = message
    }

    private val _isProgressShowing = MutableLiveData<Boolean>(false)
    val isProgressShowing : LiveData<Boolean>
    get() = _isProgressShowing

    fun setIsProgress(isProgress : Boolean){
        _isProgressShowing.value = isProgress
    }

    private val _isNightMode = MutableLiveData<Boolean>(false)
    val isNightMode : LiveData<Boolean>
    get() = _isNightMode

    fun setIsNightMode(isNightMode : Boolean) {
        _isNightMode.value = isNightMode
    }

    /**
     * Ingredients for add food
     * **/

    val foodName = MutableLiveData<String>()
    val servingSize = MutableLiveData<String>()
    val calorie = MutableLiveData<String>()
    val carbohydrate = MutableLiveData<String>()
    val protein = MutableLiveData<String>()
    val fat = MutableLiveData<String>()
    val sugar = MutableLiveData<String>()
    val salt = MutableLiveData<String>()
    val saturatedFat = MutableLiveData<String>()
    val transFat = MutableLiveData<String>()
    val cholesterol = MutableLiveData<String>()
    val companyName = MutableLiveData<String>()

    suspend fun postRequestAddFood(addingFood: AddingFood){
        try{
            _isProgressShowing.postValue(true)
            val response = repository.addNewFood(addingFood)
            if(response.isSuccessful){
                withContext(Dispatchers.Main) {
                    Utils.toastMessage(FoodiNewApplication.getInstance().getString(R.string.successfully_add))
                    _isProgressShowing.value = false
                }
            } else {
                _errorMessage.postValue(FoodiNewApplication.getInstance().getString(R.string.response_fail))
                _isProgressShowing.postValue(false)
            }
        } catch (connectException : ConnectException){
            _errorMessage.postValue(FoodiNewApplication.getInstance().getString(R.string.check_server_connection))
            _isProgressShowing.value = false
        }
    }
}
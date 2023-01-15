package com.lee.foodi.ui.activities.add.viewmodel

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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.ConnectException

private const val TAG = "AddFoodViewModel"

/**
 * Singleton Pattern
 * Because of using DataBinding at AddFoodActivity
 * **/
class AddFoodViewModel(private val repository: FoodiRepository) : ViewModel() {
    private lateinit var postAddFoodJob : Job

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
    val headTitle = MutableLiveData<String>()
    val progress = MutableLiveData<Int>(1)
    val buttonText = MutableLiveData<String>()
    val errorMessage = MutableLiveData<String>()
    val isProgressShowing = MutableLiveData<Boolean>(false)
    val isNightMode = MutableLiveData<Boolean>(false)

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

    fun postRequestAddFood(addingFood: AddingFood){
        try{
           postAddFoodJob = CoroutineScope(Dispatchers.IO).launch {
                isProgressShowing.postValue(true)
                val response = repository.addNewFood(addingFood)
                if(response.isSuccessful){
                    CoroutineScope(Dispatchers.Main).launch {
                        Utils.toastMessage(FoodiNewApplication.getInstance().getString(R.string.successfully_add))
                        isProgressShowing.value = false
                    }
                } else {
                    errorMessage.postValue(FoodiNewApplication.getInstance().getString(R.string.response_fail))
                    isProgressShowing.postValue(false)
                }
            }
        } catch (connectException : ConnectException){
            errorMessage.postValue(FoodiNewApplication.getInstance().getString(R.string.check_server_connection))
            isProgressShowing.value = false
        }
    }

    override fun onCleared() {
        if(::postAddFoodJob.isInitialized){
            if(postAddFoodJob.isActive){
                postAddFoodJob.cancel()
            }
        }
        super.onCleared()
    }
}
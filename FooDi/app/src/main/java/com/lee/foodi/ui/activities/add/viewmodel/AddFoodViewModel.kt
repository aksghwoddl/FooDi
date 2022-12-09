package com.lee.foodi.ui.activities.add.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lee.foodi.common.Utils
import com.lee.foodi.data.repository.FoodiRepository
import com.lee.foodi.data.rest.model.AddFoodData
import com.lee.foodi.ui.activities.add.AddFoodActivity
import com.lee.foodi.ui.factory.FoodiViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "AddFoodViewModel"

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
                    FoodiRepository()
                ))[AddFoodViewModel::class.java]
            }
        }
    }
    val headTitle = MutableLiveData<String>()
    val progress = MutableLiveData<Int>(1)
    val buttonText = MutableLiveData<String>()
    val errorMessage = MutableLiveData<String>()
    val isProgressShowing = MutableLiveData<Boolean>(false)

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

    suspend fun postRequestAddFood(addFoodData: AddFoodData){ 
        CoroutineScope(Dispatchers.IO).launch {
            isProgressShowing.postValue(true)
            val response = repository.addNewFood(addFoodData)
            if(response.isSuccessful){
                    CoroutineScope(Dispatchers.Main).launch { 
                        Utils.toastMessage("성공적으로 저장하였습니다.")
                        isProgressShowing.value = false
                    }
            } else {
                errorMessage.postValue("서버에 전송 실패하였습니다.")
                isProgressShowing.postValue(false)
            }
        }
    }
}
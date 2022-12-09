package com.lee.foodi.ui.activities.add.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lee.foodi.data.repository.FoodiRepository
import com.lee.foodi.ui.activities.add.AddFoodActivity
import com.lee.foodi.ui.factory.FoodiViewModelFactory

private const val TAG = "AddFoodViewModel"


/**
 * Singleton Pattern
 * Because of using DataBinding at AddFoodActivity
 * **/
class AddFoodViewModel : ViewModel() {
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

    /**
     * Ingredients for add food
     * **/
    val foodName = MutableLiveData<String>("")
    val servingSize = MutableLiveData<String>("")
    val calorie = MutableLiveData<String>("")
    val carbohydrate = MutableLiveData<String>("")
    val protein = MutableLiveData<String>("")
    val fat = MutableLiveData<String>("")
    val sugar = MutableLiveData<String>("")
    val salt = MutableLiveData<String>("")
    val saturatedFat = MutableLiveData<String>("")
    val transFat = MutableLiveData<String>("")
    val cholesterol = MutableLiveData<String>("")
    val companyName = MutableLiveData<String>("")

}
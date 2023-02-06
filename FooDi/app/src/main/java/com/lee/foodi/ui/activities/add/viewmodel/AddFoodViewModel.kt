package com.lee.foodi.ui.activities.add.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.foodi.R
import com.lee.foodi.common.NOT_AVAILABLE
import com.lee.foodi.common.ResourceProvider
import com.lee.foodi.data.rest.model.AddingFood
import com.lee.foodi.domain.FoodiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ConnectException
import javax.inject.Inject

private const val TAG = "AddFoodViewModel"

@HiltViewModel
class AddFoodViewModel @Inject constructor(
    private val repository: FoodiRepository,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

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

    private val _activityFinish = MutableLiveData<Boolean>()
    val activityFinish : LiveData<Boolean>
    get() = _activityFinish

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage : LiveData<String>
        get() = _toastMessage
    fun setToastMessage(message : String){
        _toastMessage.value = message
    }

    private val _isProgress = MutableLiveData<Boolean>()
    val isProgress : LiveData<Boolean>
        get() = _isProgress

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

    fun postRequestAddFood(){
        try{
            _isProgress.value = true
            CoroutineScope(Dispatchers.IO).launch {
                val addingFood = AddingFood(
                    foodName.value?.toString() ?: NOT_AVAILABLE ,
                    servingSize.value?.toString() ?: NOT_AVAILABLE ,
                    calorie.value?.toString() ?: NOT_AVAILABLE ,
                    carbohydrate.value?.toString() ?: NOT_AVAILABLE ,
                    protein.value?.toString() ?: NOT_AVAILABLE ,
                    fat.value?.toString() ?: NOT_AVAILABLE ,
                    sugar.value?.toString() ?: NOT_AVAILABLE ,
                    salt.value?.toString() ?: NOT_AVAILABLE ,
                    cholesterol.value?.toString() ?: NOT_AVAILABLE ,
                    saturatedFat.value?.toString() ?: NOT_AVAILABLE ,
                    transFat.value?.toString() ?: NOT_AVAILABLE ,
                    companyName.value?.toString() ?: NOT_AVAILABLE ,
                )
                val response = repository.addNewFood(addingFood)
                if(response.isSuccessful){
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "postRequestAddFood: $addingFood")
                        setToastMessage(resourceProvider.getString(R.string.successfully_add))
                        _isProgress.value = false
                        _activityFinish.value = true
                    }
                } else {
                    setToastMessage(resourceProvider.getString(R.string.response_fail))
                    _isProgress.value = false
                }
            }
        } catch (connectException : ConnectException){
            setToastMessage(resourceProvider.getString(R.string.check_server_connection))
            _isProgress.value = false
        }
    }
}
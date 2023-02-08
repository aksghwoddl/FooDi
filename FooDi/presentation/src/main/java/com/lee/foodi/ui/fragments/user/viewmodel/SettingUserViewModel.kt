package com.lee.foodi.ui.fragments.user.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.foodi.R
import com.lee.foodi.common.*
import com.lee.foodi.common.manager.FooDiPreferenceManager
import com.lee.foodi.domain.FoodiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.roundToInt

private const val TAG = "SettingUserViewModel"

@HiltViewModel
class SettingUserViewModel @Inject constructor(
    private val repository: FoodiRepository ,
    private val resourceProvider: ResourceProvider
) : ViewModel() {
    var weight = 0
    var age = 0
    var gender = ""

    private val _maintenanceCalorie = MutableLiveData<String>() // Maintenance Calorie
    val maintenanceCalorie : LiveData<String>
    get() = _maintenanceCalorie
    fun setMaintenanceCalorie(calorie : String){
        _maintenanceCalorie.value = calorie
    }

    private val _genderButtonToggled = MutableLiveData<Boolean>() // Gender
    val genderButtonToggled : LiveData<Boolean>
    get() =  _genderButtonToggled
    fun setGenderButtonToggle(toggle : Boolean) {
        _genderButtonToggled.value = toggle
    }

    private val _isOnSettingTimer = MutableLiveData<Boolean>() // Check setting timer
    val isOnSettingTimer : LiveData<Boolean>
    get() = _isOnSettingTimer
    fun setTimerOn(on : Boolean){
        _isOnSettingTimer.value = on
    }

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage : LiveData<String>
        get() = _toastMessage
    fun setToastMessage(message : String){
        _toastMessage.value = message
    }

    private val _isNightMode = MutableLiveData<Boolean>(false)
    val isNightMode : LiveData<Boolean>
        get() = _isNightMode
    fun setIsNightMode(isNightMode : Boolean) {
        _isNightMode.value = isNightMode
    }

    /**
     * 사용자의 정보를 업데이트 하는 함수
     * **/
    fun updateAllUserInfo(preferenceManager: FooDiPreferenceManager) {
        with(preferenceManager){
            gender = genderButtonToggled.value!!
            settingAge = age
            settingWeight = weight
        }
    }

    /**
     * 유지 칼로리를 계산 하고 업데이트 하는 함수
     * **/
    fun updateMaintenanceCalorie(preferenceManager: FooDiPreferenceManager) {
        Log.d(TAG , "updateMaintenanceCalorie()")
        var calorie = 0
        when(gender){
            MALE -> {
                when{
                    age in 18..29  ->{
                        calorie = (15.1* weight + 692).roundToInt()
                        _maintenanceCalorie.value = calorie.toString()
                    }
                    age in 30 .. 59 ->{
                        calorie = (11.5* weight + 873).roundToInt()
                        _maintenanceCalorie.value = calorie.toString()
                    }
                    age > 60 -> {
                        calorie = (11.7* weight + 588).roundToInt()
                        _maintenanceCalorie.value = calorie.toString()
                    }
                }
            }
            FEMALE -> {
                when{
                    age in 18..29  ->{
                        calorie = (14.8* weight + 487).roundToInt()
                        _maintenanceCalorie.value = calorie.toString()
                    }
                    age in 30 .. 59 ->{
                        calorie = (8.1* weight + 846).roundToInt()
                        _maintenanceCalorie.value = calorie.toString()
                    }
                    age > 60 -> {
                        calorie = (9.1* weight + 659).roundToInt()
                        _maintenanceCalorie.value = calorie.toString()
                    }
                }
            }
        }
        preferenceManager.maintenanceCalorie = calorie.toString()
        setToastMessage(resourceProvider.getString(R.string.successfully_modify))
    }
}
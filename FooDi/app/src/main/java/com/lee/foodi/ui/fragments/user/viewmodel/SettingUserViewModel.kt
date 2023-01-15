package com.lee.foodi.ui.fragments.user.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.foodi.R
import com.lee.foodi.common.FEMALE
import com.lee.foodi.common.FoodiNewApplication
import com.lee.foodi.common.MALE
import com.lee.foodi.common.Utils
import com.lee.foodi.common.manager.FooDiPreferenceManager
import kotlin.math.roundToInt

private const val TAG = "SettingUserViewModel"

class SettingUserViewModel : ViewModel() {
    var weight = 0
    var age = 0
    var gender = ""

    private val _maintenanceCalorie = MutableLiveData<String>() // Maintenance Calorie
    val maintenanceCalorie : LiveData<String>
    get() = _maintenanceCalorie

    private val _genderButtonToggled = MutableLiveData<Boolean>() // Gender
    val genderButtonToggled : LiveData<Boolean>
    get() =  _genderButtonToggled

    private val _isOnSettingTimer = MutableLiveData<Boolean>() // Check setting timer
    val isOnSettingTimer : LiveData<Boolean>
    get() = _isOnSettingTimer

    private val _isNightMode = MutableLiveData<Boolean>(false) // Check Night Mode
    val isNightMode : LiveData<Boolean>
    get() = _isNightMode

    fun setMaintenanceCalorie(calorie : String){
        _maintenanceCalorie.value = calorie
    }

    fun setGenderButtonToggle(toggle : Boolean) {
        _genderButtonToggled.value = toggle
    }

    fun setTimerOn(on : Boolean){
        _isOnSettingTimer.value = on
    }

    fun setIsNightMode(isNight : Boolean) {
        _isNightMode.value =  isNight
    }

    /**
     * Function for update user info
     * **/
    fun updateAllUserInfo(preferenceManager: FooDiPreferenceManager) {
        with(preferenceManager){
            gender = genderButtonToggled.value!!
            settingAge = age
            settingWeight = weight
        }
    }

    /**
     * Function for calculate maintenance calorie
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
        Utils.toastMessage(FoodiNewApplication.getInstance().getString(R.string.successfully_modify))
    }
}
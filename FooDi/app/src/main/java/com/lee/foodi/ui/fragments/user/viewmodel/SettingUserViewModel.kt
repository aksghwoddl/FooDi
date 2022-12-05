package com.lee.foodi.ui.fragments.user.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.foodi.common.FEMALE
import com.lee.foodi.common.MALE
import com.lee.foodi.common.Utils
import com.lee.foodi.common.manager.FooDiPreferenceManager
import kotlin.math.roundToInt

private const val TAG = "SettingUserViewModel"

class SettingUserViewModel : ViewModel() {
    var weight = 0
    var age = 0
    var gender = ""

    var maintenanceCalorie = MutableLiveData<String>()
    var genderButtonToggled = MutableLiveData<Boolean>()
    var isOnSettingTimer = MutableLiveData<Boolean>()

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
                        maintenanceCalorie.postValue(calorie.toString())
                    }
                    age in 30 .. 59 ->{
                        calorie = (11.5* weight + 873).roundToInt()
                        maintenanceCalorie.postValue(calorie.toString())
                    }
                    age > 60 -> {
                        calorie = (11.7* weight + 588).roundToInt()
                        maintenanceCalorie.postValue(calorie.toString())
                    }
                }
            }
            FEMALE -> {
                when{
                    age in 18..29  ->{
                        calorie = (14.8* weight + 487).roundToInt()
                        maintenanceCalorie.postValue(calorie.toString())
                    }
                    age in 30 .. 59 ->{
                        calorie = (8.1* weight + 846).roundToInt()
                        maintenanceCalorie.postValue(calorie.toString())
                    }
                    age > 60 -> {
                        calorie = (9.1* weight + 659).roundToInt()
                        maintenanceCalorie.postValue(calorie.toString())
                    }
                }
            }
        }
        preferenceManager.maintenanceCalorie = calorie.toString()
        Utils.toastMessage("정보를 성공적으로 업데이트 했습니다.")
    }
}
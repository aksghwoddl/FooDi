package com.lee.foodi.common.manager

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val NETWORK_PERMISSION = "network_permission"
private const val GOAL_CALORIE = "goal_calorie"
private const val MAINTENANCE_CALORIE = "maintenance_calorie"
private const val GENDER = "gender"
private const val AGE = "age"
private const val WEIGHT = "weight"
private const val HOUR = "hour"
private const val MINUTE = "minute"
private const val SWITCH_ON_OFF = "timer_on"

class FooDiPreferenceManager {
    companion object{
        private lateinit var instance : FooDiPreferenceManager
        private lateinit var sharedPreference : SharedPreferences
        private lateinit var sharedPreferenceEditor: SharedPreferences.Editor

        fun getInstance(context : Context) : FooDiPreferenceManager{
            if(!::instance.isInitialized){
                instance = FooDiPreferenceManager()
                sharedPreference = PreferenceManager.getDefaultSharedPreferences(context)
                sharedPreferenceEditor = sharedPreference.edit()
            }
            return instance
        }
    }

    var goalCalorie : String?
        get() = sharedPreference.getString(GOAL_CALORIE , "0")
        set(calorie : String?){
            with(sharedPreferenceEditor){
                putString(GOAL_CALORIE , calorie).apply()
            }
        }

    var maintenanceCalorie : String?
    get() = sharedPreference.getString(MAINTENANCE_CALORIE , "")
    set(calorie : String?) {
        with(sharedPreferenceEditor){
            putString(MAINTENANCE_CALORIE , calorie).apply()
        }
    }

    var gender : Boolean
    get() = sharedPreference.getBoolean(GENDER , false)
    set(gender){
        with(sharedPreferenceEditor){
            putBoolean(GENDER , gender).apply()
        }
    }

    var settingAge : Int
    get() = sharedPreference.getInt(AGE , 18)
    set(age) {
        with(sharedPreferenceEditor){
            putInt(AGE , age).apply()
        }
    }

    var settingWeight : Int
    get() = sharedPreference.getInt(WEIGHT , 60)
    set(weight){
        with(sharedPreferenceEditor){
            putInt(WEIGHT , weight).apply()
        }
    }

    var hour : Int
        get() = sharedPreference.getInt(HOUR , 0)
        set(hour){
            with(sharedPreferenceEditor){
                putInt(HOUR , hour).apply()
            }
        }

    var minute : Int
        get() = sharedPreference.getInt(MINUTE , 0)
        set(minute){
            with(sharedPreferenceEditor){
                putInt(MINUTE , minute).apply()
            }
        }

    var isTimerOn : Boolean
        get() = sharedPreference.getBoolean(SWITCH_ON_OFF , false)
        set(on){
            with(sharedPreferenceEditor){
                putBoolean(SWITCH_ON_OFF , on).apply()
            }
        }
}
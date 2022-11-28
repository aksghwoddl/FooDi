package com.lee.foodi.common.manager

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

private const val NETWORK_PERMISSION = "network_permission"
private const val GOAL_CALORIE = "goal_calorie"
private const val MAINTENANCE_CALORIE = "maintenance_calorie"
private const val GENDER = "gender"
private const val AGE = "age"
private const val WEIGHT = "weight"

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
    var isPermission : Boolean
        get() = sharedPreference.getBoolean(NETWORK_PERMISSION , false)
        set(isChecked: Boolean) {
            with(sharedPreferenceEditor) {
            putBoolean(NETWORK_PERMISSION , isChecked).apply()
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
            putBoolean(GENDER , gender)
        }
    }

    var age : Int
    get() = sharedPreference.getInt(AGE , 18)
    set(age) {
        with(sharedPreferenceEditor){
            putInt(AGE , age)
        }
    }

    var weight : Int
    get() = sharedPreference.getInt(WEIGHT , 60)
    set(weight){
        with(sharedPreferenceEditor){
            putInt(WEIGHT , weight)
        }
    }
}
package com.lee.foodi.common.manager

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

private const val NETWORK_PERMISSION = "network_permission"
private const val GOAL_CALORIE = "goal_calorie"

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
    private var isPermission : Boolean = false
    private var goalCalorie : String = ""

    fun getGoalCalorie() = sharedPreference.getString(GOAL_CALORIE , "")
    fun setGoaCalorie(calorie : String){
        with(sharedPreferenceEditor){
            putString(GOAL_CALORIE , calorie).apply()
        }
    }

    fun getPermission() = sharedPreference.getBoolean(NETWORK_PERMISSION , false)
    fun setPermission(isChecked: Boolean) {
        with(sharedPreferenceEditor) {
            putBoolean(NETWORK_PERMISSION , isChecked).apply()
        }
    }
}
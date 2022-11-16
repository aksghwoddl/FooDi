package com.lee.foodi.common.manager

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

private const val NETWORK_PERMISSION = "network_permission"

class FooDiPreferenceManager {
    companion object{
        private lateinit var instance : FooDiPreferenceManager
        private lateinit var sharedPreference : SharedPreferences
        private lateinit var sharedPreferenceEditor: SharedPreferences.Editor

        private var isPermission : Boolean
        get() = sharedPreference.getBoolean(NETWORK_PERMISSION , false)
        set(isChecked) {
            with(sharedPreferenceEditor) {
                putBoolean(NETWORK_PERMISSION , isChecked).apply()
            }
        }

        fun getInstance(context : Context) : FooDiPreferenceManager{
            if(!::instance.isInitialized){
                instance = FooDiPreferenceManager()
                sharedPreference = PreferenceManager.getDefaultSharedPreferences(context)
                sharedPreferenceEditor = sharedPreference.edit()
            }
            return instance
        }
    }
}
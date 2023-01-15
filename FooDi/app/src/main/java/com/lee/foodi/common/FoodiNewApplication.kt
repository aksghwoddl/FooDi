package com.lee.foodi.common

import android.app.Application

/**
 * First generated code when touch launcher icon
 * it can access every kotlin code when app is alive
 * **/

class FoodiNewApplication : Application() {
    companion object{
        private lateinit var appInstance : FoodiNewApplication
        fun getInstance() = appInstance
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }
}
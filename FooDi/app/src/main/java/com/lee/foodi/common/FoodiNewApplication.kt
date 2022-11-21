package com.lee.foodi.common

import android.app.Activity
import android.app.Application
import android.os.Bundle

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
        settingScreenPortrait()
    }

    fun settingScreenPortrait() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks{
            override fun onActivityCreated(activity: Activity, bundle : Bundle?) { }

            override fun onActivityStarted(activity: Activity) { }

            override fun onActivityResumed(activity: Activity) { }

            override fun onActivityPaused(activity: Activity) { }

            override fun onActivityStopped(activity: Activity) { }

            override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) { }

            override fun onActivityDestroyed(activity : Activity) { }
        })
    }
}
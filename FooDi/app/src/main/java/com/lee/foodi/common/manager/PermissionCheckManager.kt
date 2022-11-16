package com.lee.foodi.common.manager

import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.Manifest.permission.INTERNET
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


private const val RETURN_VALUE = 1004

class PermissionCheckManager(private val context : Context, private val owner : Activity) {

    private val mPermissions = arrayOf(
        ACCESS_NETWORK_STATE ,
        INTERNET
    )

    private val mDeniedPermissionList = mutableListOf<String>()

    fun checkCurrentAppPermission() : Boolean {
        mPermissions.forEach {
            if(ContextCompat.checkSelfPermission(context , it) != PackageManager.PERMISSION_GRANTED){
                mDeniedPermissionList.add(it)
            }
        }
        if(mDeniedPermissionList.isNotEmpty()){
            return false
        }
        return true
    }

    fun currentAppRequestPermission() {
        ActivityCompat.requestPermissions(owner , mDeniedPermissionList.toTypedArray() , RETURN_VALUE)
    }

    fun currentAppPermissionResult(requestCode : Int , grantResult : IntArray) : Boolean {
        if(requestCode == RETURN_VALUE && grantResult.isNotEmpty()){
            grantResult.forEach {
                if(it == -1){
                    return false
                }
            }
        } else {
            return false
        }
        return true
    }
}
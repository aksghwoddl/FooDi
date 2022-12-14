package com.lee.foodi.ui.activities.splash

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.gun0912.tedpermission.normal.TedPermission
import com.lee.foodi.common.Utils
import com.lee.foodi.databinding.ActivitySplashBinding
import com.lee.foodi.ui.activities.ContainerActivity
import com.lee.foodi.ui.activities.splash.dialog.NetworkDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "SplashActivity"

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding
    private lateinit var mDialog : NetworkDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        checkPermission()
    }

    private fun checkPermission() {
        Log.d(TAG, "checkPermission: build version is ${Build.VERSION.SDK_INT}")
        if(Build.VERSION.SDK_INT >=  Build.VERSION_CODES.TIRAMISU){
            val tedPermission = TedPermission.create()
            with(tedPermission){
                setPermissionListener(PermissionListener())
                setDeniedMessage("푸시 알림 권한을 허용 해주세요.")
                setPermissions(POST_NOTIFICATIONS)
            }
            tedPermission.check()
        } else {
            checkNetworkState()
        }
    }

    private fun checkNetworkState() {
        if(Utils.checkNetworkConnection(this) != "null"){
            CoroutineScope(Dispatchers.Main).launch {
                delay(1500)
                with(Intent(this@SplashActivity , ContainerActivity::class.java)){
                    startActivity(this)
                    finish()
                }
            }
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                delay(1500)
                mDialog = NetworkDialog(this@SplashActivity)
                mDialog.show()
            }
        }
    }

    fun dialogButtonCallBack() {
        mDialog.dismiss()
        checkNetworkState()
    }

    private inner class PermissionListener : com.gun0912.tedpermission.PermissionListener{
        override fun onPermissionGranted() {
            checkNetworkState()
        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            Utils.toastMessage("권한을 허용해야 앱 이용이 가능합니다.")
        }

    }
}
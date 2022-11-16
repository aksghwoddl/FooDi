package com.lee.foodi.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lee.foodi.databinding.ActivitySplashBinding
import com.lee.foodi.ui.ContainerActivity
import com.lee.foodi.ui.splash.dialog.NetworkDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding
    private lateinit var mDialog : NetworkDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        checkNetworkState()
    }

    private fun checkNetworkState() {
        if(checkNetworkConnection() != "null"){
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

    private fun checkNetworkConnection() : String {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetwork
        return networkInfo.toString()
    }

    fun dialogButtonCallBack() {
        mDialog.dismiss()
        checkNetworkState()
    }
}
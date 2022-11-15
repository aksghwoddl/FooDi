package com.lee.foodi.ui.splash

import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.Manifest.permission.INTERNET
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lee.foodi.databinding.ActivitySplashBinding
import com.lee.foodi.ui.ContainerActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        val permissions = mutableMapOf<String , String>()
        permissions["networkState"] = ACCESS_NETWORK_STATE
        permissions["internet"] = INTERNET

        permissions.forEach{
            if(checkSelfPermission(it.value) == PackageManager.PERMISSION_GRANTED){

            } else {
                Toast.makeText(this@SplashActivity , "앱에서 요청하는 필수 권한이 허용되지 않았습니다." , Toast.LENGTH_SHORT).show()
                finish()
            }
        }


        if(checkNetworkState() != "null"){
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
                Toast.makeText(this@SplashActivity , "인터넷 연결 상태를 확인해주세요" , Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun checkNetworkState() : String {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetwork
        return networkInfo.toString()
    }
}
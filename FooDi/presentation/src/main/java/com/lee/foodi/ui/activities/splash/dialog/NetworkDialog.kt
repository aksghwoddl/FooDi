package com.lee.foodi.ui.activities.splash.dialog

import android.app.Dialog
import android.os.Bundle
import com.lee.foodi.common.Utils
import com.lee.foodi.databinding.DialogNetworkCheckBinding
import com.lee.foodi.ui.activities.splash.SplashActivity

/**
 * 네트워크 연결 유도 팝업 class
 * **/
class NetworkDialog(private val owner : SplashActivity) : Dialog(owner) {
    private lateinit var binding : DialogNetworkCheckBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogNetworkCheckBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        addListeners()
        Utils.dialogResize(owner ,this@NetworkDialog , 0.9f , 0.4f)
    }
    private fun addListeners(){
        binding.confirmButton.setOnClickListener {
            owner.dialogButtonCallBack()
        }
    }
}
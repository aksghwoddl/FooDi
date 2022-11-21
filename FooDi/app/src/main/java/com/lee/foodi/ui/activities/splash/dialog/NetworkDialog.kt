package com.lee.foodi.ui.activities.splash.dialog

import android.app.Dialog
import android.os.Bundle
import com.lee.foodi.common.Utils
import com.lee.foodi.databinding.DialogNetworkCheckBinding
import com.lee.foodi.ui.activities.splash.SplashActivity

class NetworkDialog(private val owner : SplashActivity) : Dialog(owner) {
    private lateinit var binding : DialogNetworkCheckBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogNetworkCheckBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        addListeners()
        Utils.dialogResize(owner ,this@NetworkDialog , 0.8f , 0.3f)
    }
    private fun addListeners(){
        binding.confirmButton.setOnClickListener {
            owner.dialogButtonCallBack()
        }
    }
}
package com.lee.foodi.ui.fragments.user.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lee.foodi.databinding.DialogSettingGoalCalorieBinding
import com.lee.foodi.ui.fragments.user.UserFragment

class SettingGoalCalorieDialog(private val owner : UserFragment) : Dialog(owner.context as Context) {
    private lateinit var binding : DialogSettingGoalCalorieBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogSettingGoalCalorieBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
    }

    fun addListeners() {
        with(binding){
            confirmButton.setOnClickListener {
                
            }
        }
    }
}
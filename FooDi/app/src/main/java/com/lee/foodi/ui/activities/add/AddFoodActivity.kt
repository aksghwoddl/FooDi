package com.lee.foodi.ui.activities.add

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lee.foodi.databinding.ActivityAddFoodBinding

class AddFoodActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddFoodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFoodBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
    }
}
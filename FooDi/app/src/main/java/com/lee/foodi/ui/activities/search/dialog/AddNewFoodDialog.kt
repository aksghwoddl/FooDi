package com.lee.foodi.ui.activities.search.dialog

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lee.foodi.databinding.DialogAddNewFoodBinding
import com.lee.foodi.ui.activities.search.SearchActivity

class AddNewFoodDialog(private val owner : SearchActivity) : BottomSheetDialog(owner) {
    private lateinit var binding : DialogAddNewFoodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogAddNewFoodBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        addListeners()
    }

    private fun addListeners() {
        with(binding){
            addButton.setOnClickListener {
                dismiss()
                owner.moveToAddNewFood()
            }
        }
    }
}
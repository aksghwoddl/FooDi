package com.lee.foodi.ui.activities.add.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lee.foodi.R
import com.lee.foodi.databinding.FragmentAdditionalInfoBinding
import com.lee.foodi.ui.activities.add.viewmodel.AddFoodViewModel
import com.lee.foodi.ui.fragments.BaseFragment

class AdditionalInfoFragment : BaseFragment<FragmentAdditionalInfoBinding>(R.layout.fragment_additional_info) {
    private lateinit var mViewModel : AddFoodViewModel
    companion object{
        fun newInstance() = AdditionalInfoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        mViewModel = AddFoodViewModel.getInstance()!!
        binding.additionalViewModel = mViewModel
        binding.lifecycleOwner = this
        return view
    }
}
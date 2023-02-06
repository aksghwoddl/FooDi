package com.lee.foodi.ui.activities.add.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.lee.foodi.R
import com.lee.foodi.databinding.FragmentAdditionalInfoBinding
import com.lee.foodi.ui.activities.add.viewmodel.AddFoodViewModel
import com.lee.foodi.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdditionalInfoFragment : BaseFragment<FragmentAdditionalInfoBinding>(R.layout.fragment_additional_info) {
    private val mViewModel : AddFoodViewModel by activityViewModels()
    companion object{
        fun newInstance() = AdditionalInfoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.additionalViewModel = mViewModel
        return view
    }
}
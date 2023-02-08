package com.lee.foodi.ui.activities.add.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.lee.foodi.R
import com.lee.foodi.common.Utils
import com.lee.foodi.databinding.FragmentNecessaryInfoBinding
import com.lee.foodi.ui.activities.add.viewmodel.AddFoodViewModel
import com.lee.foodi.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * 필수 음식 정보 Fragment class
 * **/
@AndroidEntryPoint
class NecessaryInfoFragment : BaseFragment<FragmentNecessaryInfoBinding>(R.layout.fragment_necessary_info){
    private val mViewModel : AddFoodViewModel by activityViewModels()
    companion object{
      fun newInstance() = NecessaryInfoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.necesarryViewModel = mViewModel
        return view
    }

    fun checkIsEmptyStatus() : Boolean {
        with(binding){
            if(foodNameEdiText.text.isEmpty()){
                Utils.toastMessage(requireContext() , getString(R.string.input_food_name))
                foodNameEdiText.requestFocus()
                return false
            }
            if(servingSizeEditText.text.isEmpty()){
                Utils.toastMessage(requireContext() , getString(R.string.input_serving_size))
                servingSizeEditText.requestFocus()
                return false
            }
            if(calorieEditText.text.isEmpty()){
                Utils.toastMessage(requireContext() , getString(R.string.input_calorie))
                calorieEditText.requestFocus()
                return false
            }
            if(carbohydrateEditText.text.isEmpty()){
                Utils.toastMessage(requireContext() , getString(R.string.input_carbon))
                carbohydrateEditText.requestFocus()
                return false
            }
            if(proteinEditText.text.isEmpty()){
                Utils.toastMessage(requireContext() , getString(R.string.input_protein))
                proteinEditText.requestFocus()
                return false
            }
            if(fatEditText.text.isEmpty()){
                Utils.toastMessage(requireContext() , getString(R.string.input_fat))
                fatEditText.requestFocus()
                return false
            }
        }
        return true
    }
}
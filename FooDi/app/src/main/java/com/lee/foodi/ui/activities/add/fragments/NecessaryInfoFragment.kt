package com.lee.foodi.ui.activities.add.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.lee.foodi.R
import com.lee.foodi.common.Utils
import com.lee.foodi.databinding.FragmentNecessaryInfoBinding
import com.lee.foodi.ui.activities.add.viewmodel.AddFoodViewModel

class NecessaryInfoFragment : Fragment(){
    private lateinit var binding : FragmentNecessaryInfoBinding
    private lateinit var mViewModel : AddFoodViewModel
    companion object{
      fun newInstance() = NecessaryInfoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_necessary_info , container ,false)
        mViewModel = AddFoodViewModel.getInstance()!!
        binding.necesarryViewModel = mViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    fun checkIsEmptyStatus() : Boolean {
        with(binding){
            if(foodNameEdiText.text.isEmpty()){
                Utils.toastMessage(getString(R.string.input_food_name))
                foodNameEdiText.requestFocus()
                return false
            }
            if(servingSizeEditText.text.isEmpty()){
                Utils.toastMessage(getString(R.string.input_serving_size))
                servingSizeEditText.requestFocus()
                return false
            }
            if(calorieEditText.text.isEmpty()){
                Utils.toastMessage(getString(R.string.input_calorie))
                calorieEditText.requestFocus()
                return false
            }
            if(carbohydrateEditText.text.isEmpty()){
                Utils.toastMessage(getString(R.string.input_carbon))
                carbohydrateEditText.requestFocus()
                return false
            }
            if(proteinEditText.text.isEmpty()){
                Utils.toastMessage(getString(R.string.input_protein))
                proteinEditText.requestFocus()
                return false
            }
            if(fatEditText.text.isEmpty()){
                Utils.toastMessage(getString(R.string.input_fat))
                fatEditText.requestFocus()
                return false
            }
        }
        return true
    }
}
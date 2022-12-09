package com.lee.foodi.ui.activities.add.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lee.foodi.R
import com.lee.foodi.data.repository.FoodiRepository
import com.lee.foodi.databinding.FragmentNecessaryInfoBinding
import com.lee.foodi.ui.activities.add.viewmodel.AddFoodViewModel
import com.lee.foodi.ui.activities.add.viewmodel.NecessaryInfoViewModel
import com.lee.foodi.ui.factory.FoodiViewModelFactory


private const val TAG = "NecessaryInfoFragment"

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
}
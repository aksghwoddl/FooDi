package com.lee.foodi.ui.activities.add.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lee.foodi.databinding.FragmentAdditionalInfoBinding

class AdditionalInfoFragment : Fragment() {

    private lateinit var binding : FragmentAdditionalInfoBinding
    companion object{
        fun newInstance() = AdditionalInfoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdditionalInfoBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
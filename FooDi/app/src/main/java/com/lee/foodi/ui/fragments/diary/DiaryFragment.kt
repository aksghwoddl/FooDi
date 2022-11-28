package com.lee.foodi.ui.fragments.diary

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lee.foodi.R
import com.lee.foodi.common.FoodiNewApplication
import com.lee.foodi.common.Utils
import com.lee.foodi.common.manager.FooDiPreferenceManager
import com.lee.foodi.databinding.FragmentDiaryBinding
import com.lee.foodi.ui.activities.search.SearchActivity

class DiaryFragment : Fragment() {
    private lateinit var binding : FragmentDiaryBinding
    private lateinit var mPreferenceManager: FooDiPreferenceManager

    companion object{
        fun newInstance() = DiaryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiaryBinding.inflate(inflater , container , false)
        mPreferenceManager = FooDiPreferenceManager.getInstance(FoodiNewApplication.getInstance())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        addListeners()
    }

    override fun onResume() {
        super.onResume()
        updateCalories()
    }

    private fun init() {
        updateCalories()
        binding.spendCalorieProgressBar.progress = 70
    }

    private fun updateCalories() {
        mPreferenceManager.goalCalorie?.let {
            Utils.convertValueWithErrorCheck(binding.goalCalorieTextView
                , getString(R.string.goal_calorie) ,
                mPreferenceManager.goalCalorie!!
            )
        }
        Utils.convertValueWithErrorCheck(binding.spendCalorieTextView , getString(R.string.spend_calorie) , "0")
    }

    private fun addListeners() {
        with(binding){
            searchButton.setOnClickListener {
                with(Intent(FoodiNewApplication.getInstance() , SearchActivity::class.java)){
                    startActivity(this)
                }
            }

            addButton.setOnClickListener {
                with(Intent(FoodiNewApplication.getInstance() , SearchActivity::class.java)){
                    startActivity(this)
                }
            }
        }
    }
}
package com.lee.foodi.ui.fragments.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lee.foodi.R
import com.lee.foodi.common.FoodiNewApplication
import com.lee.foodi.common.manager.FooDiPreferenceManager
import com.lee.foodi.databinding.FragmentUserBinding
import com.lee.foodi.ui.fragments.user.dialog.SettingGoalCalorieDialog

class UserFragment : Fragment() {
    private lateinit var binding : FragmentUserBinding
    private lateinit var mGoalCalorieDialog: SettingGoalCalorieDialog
    private lateinit var mPreferenceManager: FooDiPreferenceManager

    companion object{
        fun newInstance() = UserFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(inflater , container , false)
        mPreferenceManager = FooDiPreferenceManager.getInstance(FoodiNewApplication.getInstance())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addListeners()
    }

    override fun onResume() {
        super.onResume()
        updateGoalCalorie()
    }

    private fun addListeners() {
        with(binding){
            goalCalorieLayout.setOnClickListener {
                mGoalCalorieDialog = SettingGoalCalorieDialog(requireContext() , this@UserFragment)
                mGoalCalorieDialog.show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun updateGoalCalorie() {
        if(mPreferenceManager.getGoalCalorie()!!.isNotEmpty()){ // Update when goal calorie is not empty
            binding.goalCalorieTextView.text = resources.getString(R.string.goal_calorie) + mPreferenceManager.getGoalCalorie()
        }
    }
}
package com.lee.foodi.ui.fragments.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lee.foodi.R
import com.lee.foodi.common.FoodiNewApplication
import com.lee.foodi.common.Utils
import com.lee.foodi.common.manager.FooDiPreferenceManager
import com.lee.foodi.data.repository.FoodiRepository
import com.lee.foodi.databinding.FragmentUserBinding
import com.lee.foodi.ui.factory.FoodiViewModelFactory
import com.lee.foodi.ui.fragments.user.dialog.SettingGoalCalorieDialog
import com.lee.foodi.ui.fragments.user.viewmodel.SettingUserViewModel
import kotlin.math.roundToInt

private const val TAG = "UserFragment"

private const val MIN_WEIGHT_PICKER_VALUE = 0
private const val MIN_AGE_PICKER_VALUE = 18
private const val MAX_AGE_VALUE = 99
private const val MAX_WEIGHT_VALUE = 200
private const val MALE = "남"
private const val FEMALE = "여"

class UserFragment : Fragment() {
    private lateinit var binding : FragmentUserBinding
    private lateinit var mGoalCalorieDialog: SettingGoalCalorieDialog
    private lateinit var mPreferenceManager: FooDiPreferenceManager
    private lateinit var mViewModel : SettingUserViewModel

    private var isToggled = false

    companion object{
        fun newInstance() = UserFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView()")

        binding = FragmentUserBinding.inflate(inflater , container , false)
        mPreferenceManager = FooDiPreferenceManager.getInstance(FoodiNewApplication.getInstance())
        mViewModel = ViewModelProvider(this , FoodiViewModelFactory(FoodiRepository()))[SettingUserViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated()")
        isToggled = mPreferenceManager.gender
        Log.d(TAG, "onViewCreated: gender is $isToggled")

        with(mViewModel){
            maintenanceCalorie.postValue(mPreferenceManager.maintenanceCalorie) // Set maintenance calorie if it is in the SharedPreference
            genderButtonToggled.postValue(isToggled)
        }

        binding.goalCalorieTextView.text = String.format(getString(R.string.goal_calorie) , mPreferenceManager.goalCalorie)
        addListeners()
        observeDate()
        initNumberPicker()
    }

    private fun initNumberPicker() {
        with(binding) {
           agePicker.run {
               minValue = MIN_AGE_PICKER_VALUE
               maxValue = MAX_AGE_VALUE
               value = mPreferenceManager.age
               mViewModel.age = value
               wrapSelectorWheel = false
               descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
           }
           weightPicker.run {
               minValue = MIN_WEIGHT_PICKER_VALUE
               maxValue = MAX_WEIGHT_VALUE
               value = mPreferenceManager.weight
               mViewModel.weight = value
               wrapSelectorWheel = false
               descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            }
        }
    }

    private fun addListeners() {
        with(binding){
            // Goal calorie layout
            goalCalorieLayout.setOnClickListener {
                mGoalCalorieDialog = SettingGoalCalorieDialog(requireContext() , this@UserFragment)
                mGoalCalorieDialog.show()
            }

            // Gender Button
            genderToggleButton.setOnClickListener {
                isToggled = !isToggled
                mViewModel.genderButtonToggled.postValue(isToggled)
            }

            // Age NumberPicker
            agePicker.setOnValueChangedListener { _, _ , value ->
                mViewModel.age = value
            }

            // Weight NumberPicker
            weightPicker.setOnValueChangedListener { _, _ , value  ->
                mViewModel.weight = value
            }

            //Confirm Button
            confirmButton.setOnClickListener {
                with(mPreferenceManager){
                    gender = isToggled
                    age = mViewModel.age
                    weight = mViewModel.weight
                }
                updateMaintenanceCalorie(mViewModel.age , mViewModel.weight , mViewModel.gender)
                if(mPreferenceManager.gender != isToggled){
                    Log.d(TAG, "addListeners: Did not update preference update one more")
                    mPreferenceManager.gender = isToggled
                }
                Log.d(TAG, "addListeners: isToggled = $isToggled")
            }
        }
    }

    private fun observeDate() {
        with(mViewModel){
            maintenanceCalorie.observe(viewLifecycleOwner){
                binding.maintenanceCalorie.text = String.format(getString(R.string.maintenance_calorie) , it)
            }
            genderButtonToggled.observe(viewLifecycleOwner){
                if(it){
                    with(binding.genderToggleButton){
                        setBackgroundColor(resources.getColor(R.color.toggle_female))
                        text = resources.getString(R.string.female)
                    }
                } else {
                    with(binding.genderToggleButton){
                        setBackgroundColor(resources.getColor(R.color.toggle_male))
                        text = resources.getString(R.string.male)
                    }
                }
                gender = binding.genderToggleButton.text.toString()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun updateGoalCalorie() {
        if(mPreferenceManager.goalCalorie!!.isNotEmpty()){ // Update when goal calorie is not empty
            Utils.convertValueWithErrorCheck(binding.goalCalorieTextView
                , resources.getString(R.string.goal_calorie)
                , mPreferenceManager.goalCalorie!!)
            Toast.makeText(FoodiNewApplication.getInstance() , "정상적으로 업데이트 되었습니다." , Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Function For calculate maintenance calorie
     * **/

    private fun updateMaintenanceCalorie(age : Int , weight : Int , gender : String) {
        Log.d(TAG, "updateMaintenanceCalorie()")
        var calorie = 0
        when(gender){
            MALE -> {
                when{
                    age in 18..29  ->{
                         calorie = (15.1* weight + 692).roundToInt()
                        mViewModel.maintenanceCalorie.postValue(calorie.toString())
                    }
                    age in 30 .. 59 ->{
                         calorie = (11.5* weight + 873).roundToInt()
                        mViewModel.maintenanceCalorie.postValue(calorie.toString())
                    }
                    age > 60 -> {
                         calorie = (11.7* weight + 588).roundToInt()
                        mViewModel.maintenanceCalorie.postValue(calorie.toString())
                    }
                }
            }
            FEMALE -> {
                when{
                    age in 18..29  ->{
                         calorie = (14.8* weight + 487).roundToInt()
                        mViewModel.maintenanceCalorie.postValue(calorie.toString())
                    }
                    age in 30 .. 59 ->{
                         calorie = (8.1* weight + 846).roundToInt()
                        mViewModel.maintenanceCalorie.postValue(calorie.toString())
                    }
                    age > 60 -> {
                         calorie = (9.1* weight + 659).roundToInt()
                        mViewModel.maintenanceCalorie.postValue(calorie.toString())
                    }
                }
            }
        }
        mPreferenceManager.maintenanceCalorie = calorie.toString()
        Toast.makeText(FoodiNewApplication.getInstance() , "정보를 성공적으로 업데이트 했습니다." , Toast.LENGTH_SHORT).show()
    }
}
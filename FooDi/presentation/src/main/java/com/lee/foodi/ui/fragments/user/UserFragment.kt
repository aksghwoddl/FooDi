package com.lee.foodi.ui.fragments.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.lee.foodi.R
import com.lee.foodi.common.Utils
import com.lee.foodi.common.manager.FooDiPreferenceManager
import com.lee.foodi.databinding.FragmentUserBinding
import com.lee.foodi.ui.base.BaseFragment
import com.lee.foodi.ui.fragments.user.dialog.SettingGoalCalorieDialog
import com.lee.foodi.ui.fragments.user.dialog.SettingTimerDialog
import com.lee.foodi.ui.fragments.user.viewmodel.SettingUserViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * 사용자 Setting Fragment class
 * **/
private const val TAG = "UserFragment"
private const val MIN_WEIGHT_PICKER_VALUE = 0
private const val MIN_AGE_PICKER_VALUE = 18
private const val MAX_AGE_VALUE = 99
private const val MAX_WEIGHT_VALUE = 200

@AndroidEntryPoint
class UserFragment : BaseFragment<FragmentUserBinding>(R.layout.fragment_user) {
    private lateinit var mGoalCalorieDialog: SettingGoalCalorieDialog
    private lateinit var mSettingTimerDialog : SettingTimerDialog
    private lateinit var mPreferenceManager: FooDiPreferenceManager
    private val mViewModel : SettingUserViewModel by viewModels()

    private var isToggled = false

    companion object{
        fun newInstance() = UserFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mPreferenceManager = FooDiPreferenceManager.getInstance(requireContext())
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated()")
        isToggled = mPreferenceManager.gender

        with(mViewModel){
            setMaintenanceCalorie(mPreferenceManager.maintenanceCalorie.toString()) // 유지 칼로리 세팅
            setGenderButtonToggle(isToggled) // 성별 버튼 이전 성별로 세팅
            setTimerOn(mPreferenceManager.isTimerOn)
        }

        binding.goalCalorieTextView.text = String.format(getString(R.string.goal_calorie) , mPreferenceManager.goalCalorie)
        addListeners()
        observeData()
        initNumberPicker()
        mViewModel.setIsNightMode(Utils.checkNightMode(requireContext()))
    }

    /**
     * NumberPicker 초기 작업하는 함수
     * **/
    private fun initNumberPicker() {
        with(binding) {
           agePicker.run {
               minValue = MIN_AGE_PICKER_VALUE
               maxValue = MAX_AGE_VALUE
               value = mPreferenceManager.settingAge
               wrapSelectorWheel = false
               descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
           }
           weightPicker.run {
               minValue = MIN_WEIGHT_PICKER_VALUE
               maxValue = MAX_WEIGHT_VALUE
               value = mPreferenceManager.settingWeight
               wrapSelectorWheel = false
               descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            }
        }
    }

    private fun addListeners() {
        with(binding){
            goalCalorieLayout.setOnClickListener { // 목표 칼로리 레이아웃 클릭시
                mGoalCalorieDialog = SettingGoalCalorieDialog(requireContext() , this@UserFragment)
                mGoalCalorieDialog.show()
            }

            genderToggleButton.setOnClickListener { // 성별 버튼
                isToggled = !isToggled
                mViewModel.setGenderButtonToggle(isToggled)
            }

            agePicker.setOnValueChangedListener { _, _ , value -> // 나이 NumberPicker
                mViewModel.age = value
            }

            weightPicker.setOnValueChangedListener { _, _ , value  -> // 몸무게 NumberPicker
                mViewModel.weight = value
            }

            confirmButton.setOnClickListener { // 설정 변경하기 버튼
                mViewModel.updateAllUserInfo(mPreferenceManager)
                mViewModel.updateMaintenanceCalorie(mPreferenceManager)
            }

            settingTimerButton.setOnClickListener { // 타이머 스위치 Listener
                mSettingTimerDialog = SettingTimerDialog(requireContext() , this@UserFragment)
                mSettingTimerDialog.show()
            }

            settingTimerSwitch.setOnCheckedChangeListener { _ , isOn -> // 타이머 스위치 설정
                mViewModel.setTimerOn(isOn)
            }
        }
    }

    private fun observeData() {
        with(mViewModel){
            maintenanceCalorie.observe(viewLifecycleOwner){ // 유지 칼로리
                binding.maintenanceCalorie.text = String.format(getString(R.string.maintenance_calorie) , it)
            }

            genderButtonToggled.observe(viewLifecycleOwner){ // 성별
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

            isOnSettingTimer.observe(viewLifecycleOwner){ // 타이머 설정 여부
                mPreferenceManager.isTimerOn = it
                with(binding){
                    settingTimerSwitch.isChecked = it
                    settingTimerButton.isEnabled = it
                }
            }

            toastMessage.observe(viewLifecycleOwner){ // Toast Message
                Utils.toastMessage(requireContext() ,it)
            }

            isNightMode.observe(viewLifecycleOwner){ // 다크모드인지 판단
                if(it){
                    Glide.with(requireContext()).load(R.drawable.ic_baseline_create_24_night).into(binding.editGoalCalorieImage)
                } else {
                    Glide.with(requireContext()).load(R.drawable.ic_baseline_create_24).into(binding.editGoalCalorieImage)
                }
            }
        }
    }

    fun updateGoalCalorie() {
        if(mPreferenceManager.goalCalorie!!.isNotEmpty()){ // 목표칼로리 setting
            val updateString = resources.getString(R.string.goal_calorie)
            Utils.convertValueWithErrorCheck(binding.goalCalorieTextView
                , updateString
                , mPreferenceManager.goalCalorie!!)
            mViewModel.setToastMessage(getString(R.string.successfully_modify))
        }
    }

    fun updateSettingTimer() {
        mViewModel.setToastMessage(getString(R.string.update_timer))
    }

    fun getPreferenceManager() = mPreferenceManager
}
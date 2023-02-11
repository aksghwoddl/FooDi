package com.lee.foodi.ui.fragments.diary

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.SimpleItemAnimator
import com.lee.domain.model.local.DiaryItem
import com.lee.foodi.R
import com.lee.foodi.common.EXTRA_SELECTED_DATE
import com.lee.foodi.common.EXTRA_SELECTED_DIARY_ITEM
import com.lee.foodi.common.NOT_AVAILABLE
import com.lee.foodi.common.Utils
import com.lee.foodi.common.manager.CustomLinearLayoutManager
import com.lee.foodi.common.manager.FooDiPreferenceManager
import com.lee.foodi.databinding.FragmentDiaryBinding
import com.lee.foodi.ui.activities.search.SearchActivity
import com.lee.foodi.ui.base.BaseFragment
import com.lee.foodi.ui.fragments.diary.adapter.DiaryFoodItemRecyclerAdapter
import com.lee.foodi.ui.fragments.diary.detail.DiaryDetailActivity
import com.lee.foodi.ui.fragments.diary.viewmodel.DiaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

/**
 * 다이어리 Fragment class
 * **/
private const val TAG = "DiaryFragment"
private const val INITIAL_VALUE = "0"
@AndroidEntryPoint
class DiaryFragment : BaseFragment<FragmentDiaryBinding>(R.layout.fragment_diary) {
    private val viewModel : DiaryViewModel by viewModels()
    private lateinit var preferenceManager: FooDiPreferenceManager
    private lateinit var diaryFoodItemRecyclerAdapter : DiaryFoodItemRecyclerAdapter

    private var year = Calendar.getInstance().get(Calendar.YEAR)
    private var month = Calendar.getInstance().get(Calendar.MONTH)
    private var day = Calendar.getInstance().get(Calendar.DATE)

    companion object{
        fun newInstance() = DiaryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        preferenceManager = FooDiPreferenceManager.getInstance(requireContext())
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated()")
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        addListeners()
        observeData()
        viewModel.setIsNightMode(Utils.checkNightMode(requireContext()))
    }


    override fun onResume() {
        Log.d(TAG, "onResume()")
        super.onResume()
        viewModel.run {
            preferenceManager.goalCalorie?.let {
                setGoalCalorie(it)
            }
            getDiaryItems()
        }
    }

    private fun observeData() {
        with(viewModel){
            // Header Date
            date.observe(viewLifecycleOwner){ // 선택된 날짜
                Utils.convertValueWithErrorCheck(binding.headerDateTextView , getString(R.string.header_date), it)
                viewModel.getDiaryItems()
            }

            diaryItems.observe(viewLifecycleOwner){ // 섭취 음식 목록
                if(it.isEmpty()){
                    Log.d(TAG, "observeDate: diaryItems is empty")
                    binding.noDiaryItemLayout.visibility = View.VISIBLE
                    binding.diaryListLayout.visibility = View.GONE
                    initFoodSummary()
                    setCalorieProgress(0.0)
                    addDiarySummary()
                    setIsProgress(false)
                } else {
                    Log.d(TAG, "observeDate: diaryItems is not empty")
                    binding.noDiaryItemLayout.visibility = View.GONE
                    binding.diaryListLayout.visibility = View.VISIBLE
                    diaryFoodItemRecyclerAdapter.run {
                        val diaryItems = mutableListOf<DiaryItem>()
                        diaryItems.addAll(it)
                        submitList(diaryItems)
                    }
                    setIsProgress(false)
                    updateFoodSummary()
                    updateCalorieProgress()
                    addDiarySummary()
                }
            }

            goalCalorie.observe(viewLifecycleOwner){ // 목표 칼로리
                Utils.convertValueWithErrorCheck(binding.goalCalorieTextView, getString(R.string.goal_calorie), it)
            }

            spendCalories.observe(viewLifecycleOwner){ // 소비 칼로리
                Utils.convertValueWithErrorCheck(binding.spendCalorieTextView, getString(R.string.spend_calorie), it)
            }

            calorieProgress.observe(viewLifecycleOwner){ // 섭취 칼로리 정도
                binding.spendCalorieProgressBar.progress = it.toInt()
            }

            amountCarbon.observe(viewLifecycleOwner){ // 총 탄수화물
                Utils.convertValueWithErrorCheck(binding.amountCarbonTextView , getString(R.string.summary_format) , it)
            }

            amountProtein.observe(viewLifecycleOwner){ // 총 단백질
                Utils.convertValueWithErrorCheck(binding.amountProteinTextView , getString(R.string.summary_format) , it)
            }

            amountFat.observe(viewLifecycleOwner){ // 총 지방
                Utils.convertValueWithErrorCheck(binding.amountFatTextView , getString(R.string.summary_format) , it)
            }

            isNightMode.observe(viewLifecycleOwner){ // 다크모드인지 확인
                if(it){
                    with(binding){
                        calendarButton.setImageResource(R.drawable.ic_baseline_calendar_month_24_night)
                        searchButton.setImageResource(R.drawable.ic_baseline_search_24_night)
                    }
                } else {
                    with(binding){
                        calendarButton.setImageResource(R.drawable.ic_baseline_calendar_month_24)
                        searchButton.setImageResource(R.drawable.ic_baseline_search_24)
                    }
                }
            }

            isProgress.observe(viewLifecycleOwner){ // 진행 상태
                if(it){
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun initRecyclerView() {
        diaryFoodItemRecyclerAdapter = DiaryFoodItemRecyclerAdapter()
        diaryFoodItemRecyclerAdapter.run {
            setOnMenuItemClickListener(PopupMenuItemClickListener())
            setOnItemClickListener(OnDiaryItemClickListener())
        }
        binding.diaryRecyclerView.run {
            layoutManager = CustomLinearLayoutManager(requireContext())
            adapter = diaryFoodItemRecyclerAdapter
            itemAnimator = null // RecyclerView 깜빡임 현상 없애기
        }
    }

    private fun addListeners() {
        with(binding){
            headerSearchLayout.setOnClickListener { // 검색하기 레이아웃
                with(Intent(requireContext() , SearchActivity::class.java)){
                    putExtra(EXTRA_SELECTED_DATE , viewModel.date.value)
                    startActivity(this)
                }
            }

            searchButton.setOnClickListener { // 검색하기 버튼
                with(Intent(requireContext() , SearchActivity::class.java)){
                    putExtra(EXTRA_SELECTED_DATE , viewModel.date.value)
                    startActivity(this)
                }
            }

            addButton.setOnClickListener { // 추가하기 버튼
                with(Intent(requireContext() , SearchActivity::class.java)){
                    putExtra(EXTRA_SELECTED_DATE , viewModel.date.value)
                    startActivity(this)
                }
            }

            calendarButton.setOnClickListener { // 달력 버튼
                val listener = DatePickerListener()
                Log.d(TAG, "addListeners: year = $year , month = $month , day = $day")
                val calendarDialog = DatePickerDialog(requireContext() , listener , year , month , day)
                calendarDialog.show()
            }
        }
    }

    /**
     * 음식 정보 update하는 함수
     * **/
    private fun updateFoodSummary(){
        Log.d(TAG, "updateFoodSummary()")
        var calorie = 0
        var carbondydrate = 0
        var protein = 0
        var fat  = 0
        viewModel.diaryItems.value!!.forEach {
            // 칼로리
            if(it.food.calorie != NOT_AVAILABLE){
                calorie+= it.food.calorie.toDouble().roundToInt()
            }

            // 탄수화물
            if(it.food.carbohydrate != NOT_AVAILABLE){
                carbondydrate += it.food.carbohydrate.toDouble().roundToInt()
            }

            // 단백질
            if(it.food.protein != NOT_AVAILABLE){
                protein += it.food.protein.toDouble().roundToInt()
            }

            // 지방
            if(it.food.fat != NOT_AVAILABLE){
                fat += it.food.fat.toDouble().roundToInt()
            }
        }
        with(viewModel) {
            setSpendCalorie(calorie.toString())
            setAmountCarbon(carbondydrate.toString())
            setAmountProtein(protein.toString())
            setAmountFat(fat.toString())
        }
    }

    /**
     * 음식 정보 초기화 하는 함수
     * **/
    private fun initFoodSummary() {
        with(viewModel){
            setSpendCalorie(INITIAL_VALUE)
            setAmountCarbon(INITIAL_VALUE)
            setAmountProtein(INITIAL_VALUE)
            setAmountFat(INITIAL_VALUE)
        }
    }

    /**
     * 칼로리 소비 ProgressBar를 update하는 함수
     * **/
    private fun updateCalorieProgress() {
        Log.d(TAG, "updateCalorieProgress()")
        with(viewModel){
            if(spendCalories.value != "0" && goalCalorie.value != "0"){
                val progress = spendCalories.value!!.toDouble()/goalCalorie.value!!.toInt() * 100
                setCalorieProgress(progress)
            }
        }
    }

   /**
    * DatePickerDialog 리스너
    * **/
   private inner class DatePickerListener : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(datePicker: DatePicker?, _year: Int, _month: Int, _day: Int) {
            val selectedDate = LocalDate.of(year, month+1 ,day).format(DateTimeFormatter.ofPattern("yyyy년MM월dd일"))
            year = _year ; month = _month ; day = _day
            viewModel.setDate(selectedDate)
        }
    }

    /**
     * PopupMenu 클릭 리스너
     * **/
   private inner class PopupMenuItemClickListener : PopupMenu.OnMenuItemClickListener {
        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when(item?.itemId){
                R.id.itemDelete -> {
                    viewModel.deleteSelectedDiaryItem(diaryFoodItemRecyclerAdapter.getSelectedItem())
                }
            }
            return true
        }
    }

    /**
     * 다이어리 아이템 클릭 리스너
     * **/
   private inner class OnDiaryItemClickListener : DiaryFoodItemRecyclerAdapter.OnItemClickListener {
        override fun onItemClick(v: View, model: DiaryItem, position: Int) {
            super.onItemClick(v, model, position)
            with(Intent(requireContext() , DiaryDetailActivity::class.java)){
                putExtra(EXTRA_SELECTED_DIARY_ITEM , model)
                startActivity(this)
            }
        }
    }
}
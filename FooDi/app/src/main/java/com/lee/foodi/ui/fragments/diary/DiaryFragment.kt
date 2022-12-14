package com.lee.foodi.ui.fragments.diary

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.lee.foodi.R
import com.lee.foodi.common.*
import com.lee.foodi.common.manager.FooDiPreferenceManager
import com.lee.foodi.data.repository.FoodiRepository
import com.lee.foodi.data.room.entity.DiaryItem
import com.lee.foodi.databinding.FragmentDiaryBinding
import com.lee.foodi.ui.activities.search.SearchActivity
import com.lee.foodi.ui.adapter.DiaryFoodItemRecyclerAdapter
import com.lee.foodi.ui.factory.FoodiViewModelFactory
import com.lee.foodi.ui.fragments.diary.detail.DiaryDetailActivity
import com.lee.foodi.ui.fragments.diary.viewmodel.DiaryViewModel
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

private const val TAG = "DiaryFragment"
private const val INITIAL_VALUE = "0"

class DiaryFragment : Fragment() {
    private lateinit var binding : FragmentDiaryBinding
    private lateinit var mPreferenceManager: FooDiPreferenceManager
    private lateinit var mViewModel : DiaryViewModel
    private lateinit var mDiaryFoodItemRecyclerAdapter : DiaryFoodItemRecyclerAdapter

    private var mDiaryFoodItems = mutableListOf<DiaryItem>()
    private var mYear = Calendar.getInstance().get(Calendar.YEAR)
    private var mMonth = Calendar.getInstance().get(Calendar.MONTH)
    private var mDay = Calendar.getInstance().get(Calendar.DATE)

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
        mViewModel = ViewModelProvider(this , FoodiViewModelFactory(FoodiRepository.getInstance()))[DiaryViewModel::class.java]
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated()")
        super.onViewCreated(view, savedInstanceState)
        init()
        addListeners()
        observeData()
        mViewModel.isNightMode.value = Utils.checkNightMode(FoodiNewApplication.getInstance())
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        Log.d(TAG, "onResume()")
        super.onResume()
        mViewModel.goalCalorie.postValue(mPreferenceManager.goalCalorie)
        CoroutineScope(Dispatchers.IO).launch {
            mDiaryFoodItems = getDiaryItem()
            mViewModel.diaryItems.postValue(mDiaryFoodItems)
            mViewModel.isProgress.postValue(false)
        }
    }

    /**
     * Function for observe Live Datas
     * **/
    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeData() {
        with(mViewModel){
            // Header Date
            date.observe(viewLifecycleOwner){
                Utils.convertValueWithErrorCheck(binding.headerDateTextView , getString(R.string.header_date), it)
                CoroutineScope(Dispatchers.IO).launch {
                    mDiaryFoodItems = getDiaryItem()
                    CoroutineScope(Dispatchers.Main).launch {
                        mViewModel.diaryItems.postValue(mDiaryFoodItems)
                    }
                }
            }

            // Diary Items
            diaryItems.observe(viewLifecycleOwner){
                if(it.isEmpty()){
                    Log.d(TAG, "observeDate: diaryItems is empty")
                    binding.noDiaryItemLayout.visibility = View.VISIBLE
                    binding.diaryListLayout.visibility = View.GONE
                    initFoodSummary()
                    calorieProgress.value = 0.0
                    CoroutineScope(Dispatchers.IO).launch {
                        mViewModel.addDiarySummary()
                    }
                } else {
                    Log.d(TAG, "observeDate: diaryItems is not empty")
                    binding.noDiaryItemLayout.visibility = View.GONE
                    binding.diaryListLayout.visibility = View.VISIBLE
                    mDiaryFoodItemRecyclerAdapter.setDiaryList(it)
                    mDiaryFoodItemRecyclerAdapter.notifyDataSetChanged()
                    updateFoodSummary()
                    updateCalorieProgress()
                    CoroutineScope(Dispatchers.IO).launch {
                        mViewModel.addDiarySummary()
                    }
                }
            }

            // Goal Calorie
            goalCalorie.observe(viewLifecycleOwner){
                Utils.convertValueWithErrorCheck(binding.goalCalorieTextView, getString(R.string.goal_calorie), it)
            }

            // Spend Calorie
            spendCalories.observe(viewLifecycleOwner){
                Utils.convertValueWithErrorCheck(binding.spendCalorieTextView, getString(R.string.spend_calorie), it)
            }

            // Progress bar
            calorieProgress.observe(viewLifecycleOwner){
                binding.spendCalorieProgressBar.progress = it.toInt()
            }

            // Food Summary Layout observer
            amountCarbon.observe(viewLifecycleOwner){
                Utils.convertValueWithErrorCheck(binding.amountCarbonTextView , getString(R.string.summary_format) , it)
            }

            amountProtein.observe(viewLifecycleOwner){
                Utils.convertValueWithErrorCheck(binding.amountProteinTextView , getString(R.string.summary_format) , it)
            }

            amountFat.observe(viewLifecycleOwner){
                Utils.convertValueWithErrorCheck(binding.amountFatTextView , getString(R.string.summary_format) , it)
            }

            // Night Mode
            isNightMode.observe(viewLifecycleOwner){
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
        }
    }

    /**
     * Function for get diary item as selected date
     * **/
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getDiaryItem() : MutableList<DiaryItem> {
        Log.d(TAG, "getDiaryItem()")
        return withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            val date = mViewModel.date.value
            mViewModel.getDiaryItems(date!!)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun init() {
        // Init RecyclerView
        mDiaryFoodItemRecyclerAdapter = DiaryFoodItemRecyclerAdapter()
        mDiaryFoodItemRecyclerAdapter.setOnMenuItemClickListener(PopupMenuItemClickListener())
        mDiaryFoodItemRecyclerAdapter.setOnItemClickListener(OnDiaryItemClickListener())

        binding.diaryRecyclerView.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mDiaryFoodItemRecyclerAdapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addListeners() {
        with(binding){
            // Food Search Listeners
            headerSearchLayout.setOnClickListener {
                with(Intent(FoodiNewApplication.getInstance() , SearchActivity::class.java)){
                    putExtra(EXTRA_SELECTED_DATE , mViewModel.date.value)
                    startActivity(this)
                }
            }

            searchButton.setOnClickListener {
                with(Intent(FoodiNewApplication.getInstance() , SearchActivity::class.java)){
                    putExtra(EXTRA_SELECTED_DATE , mViewModel.date.value)
                    startActivity(this)
                }
            }

            addButton.setOnClickListener {
                with(Intent(FoodiNewApplication.getInstance() , SearchActivity::class.java)){
                    putExtra(EXTRA_SELECTED_DATE , mViewModel.date.value)
                    startActivity(this)
                }
            }

            // Calendar Listener
            calendarButton.setOnClickListener {
                val listener = DatePickerListener()
                Log.d(TAG, "addListeners: year = $mYear , month = $mMonth , day = $mDay")
                val calendarDialog = DatePickerDialog(requireContext() , listener ,mYear , mMonth , mDay)
                calendarDialog.show()
            }
        }
    }

    /**
     * Function for update food summary
     * **/
    private fun updateFoodSummary(){
        Log.d(TAG, "updateFoodSummary()")
        var calorie = 0
        var carbondydrate = 0
        var protein = 0
        var fat  = 0
        mDiaryFoodItems.forEach {
            // Calorie
            if(it.food?.calorie!! != NOT_AVAILABLE){
                calorie+= it.food?.calorie!!.toDouble().roundToInt()
            }

            // Carbohydrate
            if(it.food?.carbohydrate!! != NOT_AVAILABLE){
                carbondydrate += it.food?.carbohydrate!!.toDouble().roundToInt()
            }

            // Protein
            if(it.food?.protein!! != NOT_AVAILABLE){
                protein += it.food?.protein!!.toDouble().roundToInt()
            }

            // Fat
            if(it.food?.fat!! != NOT_AVAILABLE){
                fat += it.food?.fat!!.toDouble().roundToInt()
            }
        }
        with(mViewModel) {
            spendCalories.value = calorie.toString()
            amountCarbon.value = carbondydrate.toString()
            amountProtein.value = protein.toString()
            amountFat.value = fat.toString()
        }
    }

    private fun initFoodSummary() {
        with(mViewModel){
            spendCalories.value = INITIAL_VALUE
            amountCarbon.value = INITIAL_VALUE
            amountProtein.value = INITIAL_VALUE
            amountFat.value = INITIAL_VALUE
        }
    }

    /**
     * Function for update bottom calorie progress value
     * **/
    private fun updateCalorieProgress() {
        Log.d(TAG, "updateCalorieProgress()")
        with(mViewModel){
            if(spendCalories.value != "0" && goalCalorie.value != "0"){
                val progress = spendCalories.value!!.toDouble()/goalCalorie.value!!.toInt() * 100
                calorieProgress.value = progress
                Log.d(TAG, "updateCalorieProgress: progress = $progress")
            }
        }
    }

    /** For Date PickerDialog **/
    inner class DatePickerListener : DatePickerDialog.OnDateSetListener {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
            val selectedDate = LocalDate.of(year, month+1 ,day).format(DateTimeFormatter.ofPattern("yyyy년MM월dd일"))
            mYear = year ; mMonth = month ; mDay = day
            mViewModel.date.value = selectedDate
        }
    }

    /** For Popup Menu click **/
    inner class PopupMenuItemClickListener : PopupMenu.OnMenuItemClickListener {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when(item?.itemId){
                R.id.itemDelete -> {
                    mDiaryFoodItemRecyclerAdapter.deleteSelectedDiaryItem()
                    CoroutineScope(Dispatchers.IO).launch {
                        mViewModel.isProgress.postValue(true)
                        mDiaryFoodItems = getDiaryItem()
                        CoroutineScope(Dispatchers.Main).launch {
                            mViewModel.diaryItems.value = mDiaryFoodItems
                            mViewModel.isProgress.value = false
                        }
                    }
                }
            }
            return true
        }
    }

    inner class OnDiaryItemClickListener : DiaryFoodItemRecyclerAdapter.OnItemClickListener {
        override fun onItemClick(v: View, model: DiaryItem, position: Int) {
            super.onItemClick(v, model, position)
            with(Intent(requireContext() , DiaryDetailActivity::class.java)){
                putExtra(EXTRA_SELECTED_DIARY_ITEM , model)
                startActivity(this)
            }
        }
    }
}
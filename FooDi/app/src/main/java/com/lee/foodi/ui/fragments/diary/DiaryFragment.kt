package com.lee.foodi.ui.fragments.diary

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lee.foodi.R
import com.lee.foodi.common.FoodiNewApplication
import com.lee.foodi.common.Utils
import com.lee.foodi.common.manager.FooDiPreferenceManager
import com.lee.foodi.data.repository.FoodiRepository
import com.lee.foodi.data.room.entity.DiaryItem
import com.lee.foodi.databinding.FragmentDiaryBinding
import com.lee.foodi.ui.activities.search.SearchActivity
import com.lee.foodi.ui.adapter.DiaryFoodItemRecyclerAdapter
import com.lee.foodi.ui.factory.FoodiViewModelFactory
import com.lee.foodi.ui.fragments.diary.viewmodel.DiaryViewModel
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

private const val TAG = "DiaryFragment"

class DiaryFragment : Fragment() {
    private lateinit var binding : FragmentDiaryBinding
    private lateinit var mPreferenceManager: FooDiPreferenceManager
    private lateinit var mViewModel : DiaryViewModel
    private lateinit var mDiaryFoodItemRecyclerAdapter : DiaryFoodItemRecyclerAdapter

    private var mDiaryFoodItems = mutableListOf<DiaryItem>()

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
        mViewModel = ViewModelProvider(this , FoodiViewModelFactory(FoodiRepository()))[DiaryViewModel::class.java]
        mDiaryFoodItemRecyclerAdapter = DiaryFoodItemRecyclerAdapter()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated()")
        super.onViewCreated(view, savedInstanceState)
        init()
        addListeners()
        observeDate()
        CoroutineScope(Dispatchers.IO).launch {
            mDiaryFoodItems = getDiaryItem()
            mViewModel.diaryItems.postValue(mDiaryFoodItems)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        Log.d(TAG, "onResume()")
        super.onResume()
        mViewModel.goalCalorie.postValue(mPreferenceManager.goalCalorie)
        CoroutineScope(Dispatchers.IO).launch {
            mDiaryFoodItems = getDiaryItem()
            mViewModel.diaryItems.postValue(mDiaryFoodItems)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeDate() {
        with(mViewModel){
            // Header Date
            date.observe(viewLifecycleOwner){
                Utils.convertValueWithErrorCheck(binding.headerDateTextView , getString(R.string.header_date), it)
            }

            // Diary Items
            diaryItems.observe(viewLifecycleOwner){
                if(it.isEmpty()){
                    binding.noDiaryItemLayout.visibility = View.VISIBLE
                    binding.diaryListLayout.visibility = View.GONE
                } else {
                    binding.noDiaryItemLayout.visibility = View.GONE
                    binding.diaryListLayout.visibility = View.VISIBLE
                    mDiaryFoodItemRecyclerAdapter.setDiaryList(it)
                    mDiaryFoodItemRecyclerAdapter.notifyItemRangeChanged(0 , mDiaryFoodItemRecyclerAdapter.itemCount)
                    updateFoodSummary()
                    updateCalorieProgress()
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
        }
    }

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
        binding.diaryRecyclerView.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mDiaryFoodItemRecyclerAdapter
        }
    }

    private fun addListeners() {
        with(binding){
            headerSearchLayout.setOnClickListener {
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

    private fun updateFoodSummary(){
        Log.d(TAG, "updateFoodSummary()")
        var calorie = 0
        var carbondydrate = 0
        var protein = 0
        var fat  = 0
        mDiaryFoodItems.forEach {
            calorie+= it.food?.calorie!!.toDouble().roundToInt()
            carbondydrate += it.food?.carbohydrate!!.toDouble().roundToInt()
            protein += it.food?.protein!!.toDouble().roundToInt()
            fat += it.food?.fat!!.toDouble().roundToInt()
        }
        with(mViewModel) {
            spendCalories.value = calorie.toString()
            amountCarbon.postValue(carbondydrate.toString())
            amountProtein.postValue(protein.toString())
            amountFat.postValue(fat.toString())
        }
    }

    private fun updateCalorieProgress() {
        Log.d(TAG, "updateCalorieProgress()")
        with(mViewModel){
            if(spendCalories.value != "0" && goalCalorie.value != "0"){
                val progress = spendCalories.value!!.toDouble()/goalCalorie.value!!.toInt() * 100
                calorieProgress.postValue(progress)
                Log.d(TAG, "updateCalorieProgress: progress = $progress")
            }
        }
    }
}
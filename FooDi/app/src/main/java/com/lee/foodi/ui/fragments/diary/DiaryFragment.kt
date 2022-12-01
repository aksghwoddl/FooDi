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
import androidx.recyclerview.widget.RecyclerView
import com.lee.foodi.R
import com.lee.foodi.common.FoodiNewApplication
import com.lee.foodi.common.Utils
import com.lee.foodi.common.manager.FooDiPreferenceManager
import com.lee.foodi.data.repository.FoodiRepository
import com.lee.foodi.data.rest.model.FoodInfoData
import com.lee.foodi.data.room.db.DiaryDatabase
import com.lee.foodi.databinding.FragmentDiaryBinding
import com.lee.foodi.ui.activities.search.SearchActivity
import com.lee.foodi.ui.adapter.DiaryFoodItemRecyclerAdapter
import com.lee.foodi.ui.factory.FoodiViewModelFactory
import com.lee.foodi.ui.fragments.diary.viewmodel.DiaryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val TAG = "DiaryFragment"

class DiaryFragment : Fragment() {
    private lateinit var binding : FragmentDiaryBinding
    private lateinit var mPreferenceManager: FooDiPreferenceManager
    private lateinit var mViewModel : DiaryViewModel
    private lateinit var mDiaryFoodItemRecyclerAdapter : DiaryFoodItemRecyclerAdapter

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
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            getDiaryItem()
        }
        init()
        addListeners()
        observeDate()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        updateCalories()
        CoroutineScope(Dispatchers.IO).launch {
            getDiaryItem()
        }
    }

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
                }
            }

            // Goal Calorie
            goalCalorie.observe(viewLifecycleOwner){
                Utils.convertValueWithErrorCheck(binding.goalCalorieTextView
                    , getString(R.string.goal_calorie)
                    , it
                )
            }

            // Spend Calorie
            spendCalories.observe(viewLifecycleOwner){
                Utils.convertValueWithErrorCheck(binding.spendCalorieTextView
                    , getString(R.string.spend_calorie)
                    , "0")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getDiaryItem() {
         CoroutineScope(Dispatchers.IO).launch {
            val date = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
            mViewModel.getDiaryItems(date)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun init() {
        // Setting Today date format at header
        mViewModel.date.postValue(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년MM월dd일")))

        // Init RecyclerView
        binding.diaryRecyclerView.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mDiaryFoodItemRecyclerAdapter

        }
        updateCalories() // Update Calories
        binding.spendCalorieProgressBar.progress = 70 // Set Progress need to update

    }

    private fun updateCalories() {
        with(mViewModel){
            goalCalorie.postValue(mPreferenceManager.goalCalorie)
            spendCalories.postValue("0") // Need to add spend set calorie
        }

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
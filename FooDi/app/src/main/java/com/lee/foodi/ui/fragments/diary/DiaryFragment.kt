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
import com.lee.foodi.R
import com.lee.foodi.common.FoodiNewApplication
import com.lee.foodi.common.Utils
import com.lee.foodi.common.manager.FooDiPreferenceManager
import com.lee.foodi.data.rest.model.FoodInfoData
import com.lee.foodi.data.room.db.DiaryDatabase
import com.lee.foodi.databinding.FragmentDiaryBinding
import com.lee.foodi.ui.activities.search.SearchActivity
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
    private lateinit var mDiaryDatabase: DiaryDatabase

    private var mDiaryItemList = mutableListOf<FoodInfoData>()

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
        mDiaryDatabase = DiaryDatabase.getInstance()

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        updateCalories()
        CoroutineScope(Dispatchers.IO).launch {
            getDiaryItem()
            CoroutineScope(Dispatchers.Main).launch{
                if(mDiaryItemList.isEmpty()){
                    binding.noDiaryItemLayout.visibility = View.VISIBLE
                } else {
                    binding.noDiaryItemLayout.visibility = View.GONE
                    Log.d(TAG, "onResume: diaryItem is $mDiaryItemList")
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getDiaryItem() {
        mDiaryItemList = withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            val date = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
            mDiaryDatabase.diaryDao().getDiaryItemByDate(date)
        }
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
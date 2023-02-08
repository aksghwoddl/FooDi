package com.lee.foodi.ui.activities.search

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.lee.domain.model.remote.Food
import com.lee.foodi.R
import com.lee.foodi.common.EXTRA_SELECTED_DATE
import com.lee.foodi.common.EXTRA_SELECTED_FOOD
import com.lee.foodi.common.PAGE_ONE
import com.lee.foodi.common.Utils
import com.lee.foodi.common.manager.CustomLinearLayoutManager
import com.lee.foodi.databinding.ActivitySearchBinding
import com.lee.foodi.ui.activities.add.AddFoodActivity
import com.lee.foodi.ui.activities.search.adapter.SearchFoodRecyclerAdapter
import com.lee.foodi.ui.activities.search.detail.FoodDetailActivity
import com.lee.foodi.ui.activities.search.dialog.AddNewFoodDialog
import com.lee.foodi.ui.activities.search.viewmodel.SearchFoodViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * 음식 검색 Activity class
 * **/
@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySearchBinding
    private val mViewModel : SearchFoodViewModel by viewModels()
    private lateinit var mSearchFoodRecyclerAdapter: SearchFoodRecyclerAdapter
    private lateinit var mAddNewFoodDialog : AddNewFoodDialog

    private var mCurrentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        initRecyclerView()
        addListeners()
        observeData()
        mViewModel.setIsNightMode(Utils.checkNightMode(this@SearchActivity))
        if(!::mAddNewFoodDialog.isInitialized){
            mAddNewFoodDialog = AddNewFoodDialog(this)
        }
        mAddNewFoodDialog.show()
    }

    private fun initRecyclerView(){
        mSearchFoodRecyclerAdapter = SearchFoodRecyclerAdapter()
        mSearchFoodRecyclerAdapter.setOnItemClickListener(object : SearchFoodRecyclerAdapter.OnItemClickListener{
            // move to FoodDetailActivity when item selected
            override fun onItemClick(v: View, model: Food, position: Int) {
                super.onItemClick(v, model, position)
                with(Intent(this@SearchActivity , FoodDetailActivity::class.java)){
                    putExtra(EXTRA_SELECTED_FOOD , model)
                    putExtra(EXTRA_SELECTED_DATE , intent.getStringExtra(EXTRA_SELECTED_DATE))
                    startActivity(this)
                }
            }
        })
        with(binding.searchFoodRecyclerView){
            layoutManager = CustomLinearLayoutManager(context , RecyclerView.VERTICAL , false)
            adapter = mSearchFoodRecyclerAdapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false // RecyclerView 깜빡임 현상 없애기
        }
    }

    private fun addListeners(){
        with(binding){

            searchInputTextLayout.setEndIconOnClickListener { // 검색
                if(Utils.checkNetworkConnection(this@SearchActivity)){
                    val foodName = searchInputText.text.toString()
                    mViewModel.getSearchFoodList(foodName , PAGE_ONE)
                    mCurrentPage = 1
                } else {
                    mViewModel.setToastMessage(getString(R.string.check_network))
                }
            }

            // Key Event Listener
            searchInputText.setOnKeyListener { _ , keyCode, _ -> // EditText 확인 눌렸을때 동작
                when(keyCode){
                    KeyEvent.KEYCODE_ENTER -> {
                        if(Utils.checkNetworkConnection(this@SearchActivity)){
                            val foodName = searchInputText.text.toString()
                            mViewModel.getSearchFoodList(foodName , PAGE_ONE)
                            mCurrentPage = 1
                        } else {
                            mViewModel.setToastMessage(getString(R.string.check_network))
                        }
                    }
                }
                false
            }

            nextButton.setOnClickListener { // 다음버튼
                val foodName = searchInputText.text.toString()
                mViewModel.getSearchFoodList(foodName , (++mCurrentPage).toString())
                mViewModel.setPreviousEnable(true)
                binding.searchFoodRecyclerView.smoothScrollToPosition(0)
            }

            // Previous Button Listener
            previousButton.setOnClickListener { // 이전버튼
                if(mCurrentPage > 1){
                    val foodName = searchInputText.text.toString()
                    mViewModel.getSearchFoodList(foodName , (--mCurrentPage).toString())
                    if(mCurrentPage == 1){
                        mViewModel.setPreviousEnable(false)
                    }
                    binding.searchFoodRecyclerView.smoothScrollToPosition(0)
                }
            }

            addFoodButton.setOnClickListener { // 음식 추가하기
                moveToAddNewFood()
            }

            backButton.setOnClickListener { // 뒤로가기
                finish()
            }
        }
    }

    /**
     * Function for observing ViewModel Data
     * **/
    private fun observeData() {
       with(mViewModel){
           // Searched List
           foodList.observe(this@SearchActivity){ // 검색한 음식 리스트
               it?.let {
                   if(it.isNotEmpty()){
                       mSearchFoodRecyclerAdapter.run {
                           setList(it)
                           notifyItemRangeChanged(0 , itemCount)
                       }
                       setAddFoodLayoutVisible(false)
                   } else {
                       setAddFoodLayoutVisible(true)
                       setIsProgress(false)
                   }
               }
           }


           isProgress.observe(this@SearchActivity) { // 진행상태
               if (it) {
                   binding.progressBar.visibility = View.VISIBLE
               } else {
                   binding.progressBar.visibility = View.GONE
               }
           }

           // When there are no search results
           addFoodLayoutVisible.observe(this@SearchActivity){ // 음식 추가하기 레이아웃 보여주기 여부
               if(it){
                   with(binding){
                       searchFoodRecyclerView.visibility = View.GONE
                       buttonLayout.visibility = View.GONE
                       noSearchFoodLayout.visibility = View.VISIBLE
                   }
               } else {
                   with(binding){
                       searchFoodRecyclerView.visibility = View.VISIBLE
                       buttonLayout.visibility = View.VISIBLE
                       noSearchFoodLayout.visibility = View.GONE
                   }
               }
           }

           // Next Button enable
           isNextEnable.observe(this@SearchActivity){ // 다음버튼 보여주기 여부
               binding.nextButton.isEnabled = it
           }

           // Previous Button enable
           isPreviousEnable.observe(this@SearchActivity){ // 이전버튼 보여주기 여부
               binding.previousButton.isEnabled = it
           }

           toastMessage.observe(this@SearchActivity){ // Toast Message
               Utils.toastMessage(this@SearchActivity , it)
           }

           // Night Mode
           isNightMode.observe(this@SearchActivity){ // 다크모드인지 확인
               if(it){
                   binding.backButton.setImageResource(R.drawable.ic_baseline_arrow_back_24_night)
               } else {
                   binding.backButton.setImageResource(R.drawable.ic_baseline_arrow_back_24)
               }
           }
       }
    }

    /**
     * 음식 추가하기 화면으로 이동하는 함수
     * **/
    fun moveToAddNewFood() {
        with(Intent(this , AddFoodActivity::class.java)){
            startActivity(this)
        }
    }
}
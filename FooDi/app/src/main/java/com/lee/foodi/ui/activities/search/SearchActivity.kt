package com.lee.foodi.ui.activities.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lee.foodi.R
import com.lee.foodi.common.*
import com.lee.foodi.data.repository.FoodiRepository
import com.lee.foodi.data.rest.model.Food
import com.lee.foodi.databinding.ActivitySearchBinding
import com.lee.foodi.ui.activities.add.AddFoodActivity
import com.lee.foodi.ui.activities.search.adapter.SearchFoodRecyclerAdapter
import com.lee.foodi.ui.activities.search.detail.FoodDetailActivity
import com.lee.foodi.ui.activities.search.dialog.AddNewFoodDialog
import com.lee.foodi.ui.activities.search.viewmodel.SearchFoodViewModel
import com.lee.foodi.ui.factory.FoodiViewModelFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

private const val TAG = "SearchFragment"

class SearchActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySearchBinding
    private lateinit var mViewModel : SearchFoodViewModel
    private lateinit var mSearchFoodRecyclerAdapter: SearchFoodRecyclerAdapter
    private lateinit var mAddNewFoodDialog : AddNewFoodDialog

    private var mCurrentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        mViewModel = ViewModelProvider(this , FoodiViewModelFactory(FoodiRepository.getInstance()))[SearchFoodViewModel::class.java]
        initRecyclerView()
        addListeners()
        observeData()
        mViewModel.isNightMode.value = Utils.checkNightMode(FoodiNewApplication.getInstance())
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
            layoutManager = LinearLayoutManager(context , RecyclerView.VERTICAL , false)
            adapter = mSearchFoodRecyclerAdapter
        }
    }

    private fun addListeners(){
        with(binding){
            // Search Button Listener
            searchInputTextLayout.setEndIconOnClickListener {
                if(Utils.checkNetworkConnection(this@SearchActivity) != "null"){
                    val foodName = searchInputText.text.toString()
                    mViewModel.getSearchFoodList(foodName , PAGE_ONE)
                    mCurrentPage = 1
                } else {
                    mViewModel.errorMessage.value = NETWORK_NOT_CONNECTED
                }
            }

            // Key Event Listener
            searchInputText.setOnKeyListener { _ , keyCode, _ ->
                when(keyCode){
                    KeyEvent.KEYCODE_ENTER -> {
                        if(Utils.checkNetworkConnection(this@SearchActivity) != "null"){
                            val foodName = searchInputText.text.toString()
                            mViewModel.getSearchFoodList(foodName , PAGE_ONE)
                            mCurrentPage = 1
                        } else {
                            mViewModel.errorMessage.value = NETWORK_NOT_CONNECTED
                        }
                    }
                }
                false
            }

            // Next Button Listener
            nextButton.setOnClickListener {
                val foodName = searchInputText.text.toString()
                mViewModel.getSearchFoodList(foodName , (++mCurrentPage).toString())
                mViewModel.isPreviousEnable.postValue(true)
                binding.searchFoodRecyclerView.smoothScrollToPosition(0)
            }

            // Previous Button Listener
            previousButton.setOnClickListener {
                if(mCurrentPage > 1){
                    val foodName = searchInputText.text.toString()
                    mViewModel.getSearchFoodList(foodName , (--mCurrentPage).toString())
                    if(mCurrentPage == 1){
                        mViewModel.isPreviousEnable.postValue(false)
                    }
                    binding.searchFoodRecyclerView.smoothScrollToPosition(0)
                }
            }

            addFoodButton.setOnClickListener {
                moveToAddNewFood()
            }

            backButton.setOnClickListener {
                finish()
            }
        }
    }

    /**
     * Function for observing ViewModel Data
     * **/
    @SuppressLint("NotifyDataSetChanged")
    private fun observeData() {
       with(mViewModel){
           // Searched List
           foodList.observe(this@SearchActivity){
               it?.let {
                   if(it.isNotEmpty()){
                       mSearchFoodRecyclerAdapter.setList(it)
                       mSearchFoodRecyclerAdapter.notifyDataSetChanged()
                       addFoodLayoutVisible.postValue(false)
                   } else {
                       addFoodLayoutVisible.postValue(true)
                       isProgressVisible.postValue(false)
                   }
               }
           }

           // Progress bar showing
           isProgressVisible.observe(this@SearchActivity) {
               if (it) {
                   binding.progressBar.visibility = View.VISIBLE
               } else {
                   binding.progressBar.visibility = View.GONE
               }
           }

           // Error Message
           errorMessage.observe(this@SearchActivity){
               Utils.toastMessage(it)
           }

           // When there are no search results
           addFoodLayoutVisible.observe(this@SearchActivity){
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
           isNextEnable.observe(this@SearchActivity){
               binding.nextButton.isEnabled = it
           }

           // Previous Button enable
           isPreviousEnable.observe(this@SearchActivity){
               binding.previousButton.isEnabled = it
           }

           // Night Mode
           isNightMode.observe(this@SearchActivity){
               if(it){
                   binding.backButton.setImageResource(R.drawable.ic_baseline_arrow_back_24_night)
               } else {
                   binding.backButton.setImageResource(R.drawable.ic_baseline_arrow_back_24)
               }
           }
       }
    }

    /**
     * Function for move to AddFoodActivity
     * **/
    fun moveToAddNewFood() {
        with(Intent(this , AddFoodActivity::class.java)){
            startActivity(this)
        }
    }
}
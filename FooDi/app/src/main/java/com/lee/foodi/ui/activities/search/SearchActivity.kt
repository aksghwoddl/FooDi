package com.lee.foodi.ui.activities.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lee.foodi.common.EXTRA_SELECTED_FOOD
import com.lee.foodi.common.FoodiNewApplication
import com.lee.foodi.common.PAGE_ONE
import com.lee.foodi.data.model.FoodInfoData
import com.lee.foodi.data.repository.FoodiRepository
import com.lee.foodi.databinding.ActivitySearchBinding
import com.lee.foodi.ui.activities.add.AddFoodActivity
import com.lee.foodi.ui.activities.search.viewmodel.SearchFoodViewModel
import com.lee.foodi.ui.adapter.SearchFoodRecyclerAdapter
import com.lee.foodi.ui.factory.FoodiViewModelFactory

class SearchActivity : AppCompatActivity() {
    private val TAG = "SearchFragment"

    private lateinit var binding : ActivitySearchBinding
    private lateinit var mViewModel : SearchFoodViewModel
    private lateinit var mSearchFoodRecyclerAdapter: SearchFoodRecyclerAdapter

    private var mCurrentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        mViewModel = ViewModelProvider(this , FoodiViewModelFactory(FoodiRepository()))[SearchFoodViewModel::class.java]
        initRecyclerView()
        addListeners()
        observeData()
    }

    private fun initRecyclerView(){
        mSearchFoodRecyclerAdapter = SearchFoodRecyclerAdapter()
        mSearchFoodRecyclerAdapter.setOnItemClickListener(object : SearchFoodRecyclerAdapter.OnItemClickListener{
            // move to FoodDetailActivity when item selected
            override fun onItemClick(v: View, model: FoodInfoData, position: Int) {
                super.onItemClick(v, model, position)
                with(Intent(this@SearchActivity , FoodDetailActivity::class.java)){
                    putExtra(EXTRA_SELECTED_FOOD , model)
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
                val foodName = searchInputText.text.toString()
                mViewModel.getSearchFoodList(foodName , PAGE_ONE)
                mCurrentPage = 1
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
                with(Intent(FoodiNewApplication.getInstance() , AddFoodActivity::class.java)){
                    startActivity(this)
                }
            }
        }
    }

    /**
     * Function for observing ViewModel Data
     * **/

    @SuppressLint("NotifyDataSetChanged")
    private fun observeData() {
       with(mViewModel){
           foodList.observe(this@SearchActivity){
               it?.let {
                   mSearchFoodRecyclerAdapter.setList(it)
                   mSearchFoodRecyclerAdapter.notifyDataSetChanged()
                   addFoodLayoutVisible.postValue(false)
               }?:run {
                       addFoodLayoutVisible.postValue(true)
                       isProgressVisible.postValue(false)
               }
           }

           isProgressVisible.observe(this@SearchActivity){
               if(it){
                   binding.progressBar.visibility = View.VISIBLE
               } else {
                   binding.progressBar.visibility = View.GONE
               }
           }

           errorMessage.observe(this@SearchActivity){
               Toast.makeText(this@SearchActivity , it , Toast.LENGTH_SHORT).show()
           }

           addFoodLayoutVisible.observe(this@SearchActivity){
               if(it){
                   with(binding){
                       searchFoodRecyclerScrollView.visibility = View.GONE
                       noSearchFoodLayout.visibility = View.VISIBLE
                   }
               } else {
                   with(binding){
                       searchFoodRecyclerScrollView.visibility = View.VISIBLE
                       noSearchFoodLayout.visibility = View.GONE
                   }
               }
           }

           isNextEnable.observe(this@SearchActivity){
               binding.nextButton.isEnabled = it
           }

           isPreviousEnable.observe(this@SearchActivity){
               binding.previousButton.isEnabled = it
           }
       }
    }
}
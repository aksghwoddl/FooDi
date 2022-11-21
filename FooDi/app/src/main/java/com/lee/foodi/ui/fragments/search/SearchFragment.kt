package com.lee.foodi.ui.fragments.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lee.foodi.R
import com.lee.foodi.common.EXTRA_SELECTED_FOOD
import com.lee.foodi.common.FoodiNewApplication
import com.lee.foodi.common.PAGE_ONE
import com.lee.foodi.data.model.FoodInfoData
import com.lee.foodi.data.repository.FoodiRepository
import com.lee.foodi.databinding.FragmentSearchBinding
import com.lee.foodi.ui.activities.add.AddFoodActivity
import com.lee.foodi.ui.adapter.SearchFoodRecyclerAdapter
import com.lee.foodi.ui.fragments.search.viewmodel.SearchFoodViewModel
import com.lee.foodi.ui.factory.FoodiViewModelFactory

class SearchFragment : Fragment() {
    private val TAG = "SearchFragment"

    private lateinit var binding : FragmentSearchBinding
    private lateinit var mViewModel : SearchFoodViewModel
    private lateinit var mSearchFoodRecyclerAdapter: SearchFoodRecyclerAdapter

    private var mCurrentPage = 0

    companion object{
        fun newInstance() = SearchFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater , container , false)
        mViewModel = ViewModelProvider(this , FoodiViewModelFactory(FoodiRepository()))[SearchFoodViewModel::class.java]
        initRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addListeners()
        observeData()
    }

    private fun initRecyclerView(){
        mSearchFoodRecyclerAdapter = SearchFoodRecyclerAdapter()
        mSearchFoodRecyclerAdapter.setOnItemClickListener(object : SearchFoodRecyclerAdapter.OnItemClickListener{
            // move to FoodDetailActivity when item selected
            override fun onItemClick(v: View, model: FoodInfoData, position: Int) {
                super.onItemClick(v, model, position)
                with(Intent(requireContext() , FoodDetailActivity::class.java)){
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
                mViewModel.getSearchFoodList(resources.getString(R.string.food_info_service_key) , foodName , PAGE_ONE)
                mCurrentPage = 1
            }

            // Next Button Listener
            nextButton.setOnClickListener {
                val foodName = searchInputText.text.toString()
                mViewModel.getSearchFoodList(resources.getString(R.string.food_info_service_key) , foodName , (++mCurrentPage).toString())
                mViewModel.isPreviousEnable.postValue(true)
                binding.searchFoodRecyclerView.smoothScrollToPosition(0)
            }

            // Previous Button Listener
            previousButton.setOnClickListener {
                if(mCurrentPage > 1){
                    val foodName = searchInputText.text.toString()
                    mViewModel.getSearchFoodList(resources.getString(R.string.food_info_service_key) , foodName , (--mCurrentPage).toString())
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
           foodList.observe(viewLifecycleOwner){
               it?.let {
                   mSearchFoodRecyclerAdapter.setList(it)
                   mSearchFoodRecyclerAdapter.notifyDataSetChanged()
                   addFoodLayoutVisible.postValue(false)
               }?:run {
                       addFoodLayoutVisible.postValue(true)
                       isProgressVisible.postValue(false)
               }
           }

           isProgressVisible.observe(viewLifecycleOwner){
               if(it){
                   binding.progressBar.visibility = View.VISIBLE
               } else {
                   binding.progressBar.visibility = View.GONE
               }
           }

           errorMessage.observe(viewLifecycleOwner){
               Toast.makeText(context , it , Toast.LENGTH_SHORT).show()
           }

           addFoodLayoutVisible.observe(viewLifecycleOwner){
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

           isNextEnable.observe(viewLifecycleOwner){
               binding.nextButton.isEnabled = it
           }

           isPreviousEnable.observe(viewLifecycleOwner){
               binding.previousButton.isEnabled = it
           }
       }
    }
}
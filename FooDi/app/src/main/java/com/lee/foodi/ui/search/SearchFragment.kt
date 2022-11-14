package com.lee.foodi.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lee.foodi.common.PAGE_ONE
import com.lee.foodi.data.repository.RestRepository
import com.lee.foodi.databinding.FragmentSearchBinding
import com.lee.foodi.ui.adapter.SearchFoodRecyclerAdapter
import com.lee.foodi.ui.viewmodel.FoodInfoViewModel
import com.lee.foodi.ui.viewmodel.factory.FoodViewModelFactory

class SearchFragment : Fragment() {
    private lateinit var binding : FragmentSearchBinding
    private lateinit var mViewModel : FoodInfoViewModel
    private lateinit var mSearchFoodRecyclerAdapter: SearchFoodRecyclerAdapter

    companion object{
        fun newInstance() = SearchFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater , container , false)
        mViewModel = ViewModelProvider(this , FoodViewModelFactory(RestRepository()))[FoodInfoViewModel::class.java]
        initRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchInputTextLayout.setEndIconOnClickListener {
            val foodName = binding.searchInputText.text.toString()
            mViewModel.getSearchFoodList(foodName , PAGE_ONE)
        }
        observeData()
    }

    private fun initRecyclerView(){
        mSearchFoodRecyclerAdapter = SearchFoodRecyclerAdapter()
        with(binding.searchFoodRecyclerView){
            layoutManager = LinearLayoutManager(context , RecyclerView.VERTICAL , false)
            adapter = mSearchFoodRecyclerAdapter
        }
    }

    private fun observeData() {
       with(mViewModel){
           foodList.observe(viewLifecycleOwner){
               it?.let {
                   mSearchFoodRecyclerAdapter.setList(it)
                   mSearchFoodRecyclerAdapter.notifyItemRangeChanged(0 , mSearchFoodRecyclerAdapter.itemCount)
                   mViewModel.addFoodLayoutVisible.postValue(false)
               }?:run {
                   Toast.makeText(context , "검색결과가 존재하지 않습니다!!" , Toast.LENGTH_SHORT).show()
                   mViewModel.isProgressVisible.postValue(false)
                   mViewModel.addFoodLayoutVisible.postValue(true)
               }
           }
           mViewModel.isProgressVisible.observe(viewLifecycleOwner){
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
                       searchFoodRecyclerView.visibility = View.GONE
                       noSearchFoodLayout.visibility = View.VISIBLE
                   }
               } else {
                   with(binding){
                       searchFoodRecyclerView.visibility = View.VISIBLE
                       noSearchFoodLayout.visibility = View.GONE
                   }
               }
           }
       }
    }
}
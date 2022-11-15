package com.lee.foodi.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lee.foodi.data.repository.RestRepository
import com.lee.foodi.ui.search.viewmodel.SearchFoodViewModel

class FoodViewModelFactory(private val repository: RestRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(SearchFoodViewModel::class.java)){
            SearchFoodViewModel(repository) as T
        } else {
            throw java.lang.IllegalArgumentException("해당 ViewModel을 찾을수가 없습니다!")
        }
    }
}
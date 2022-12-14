package com.lee.foodi.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lee.foodi.data.repository.FoodiRepository
import com.lee.foodi.ui.activities.add.viewmodel.AddFoodViewModel
import com.lee.foodi.ui.activities.search.viewmodel.SearchFoodViewModel
import com.lee.foodi.ui.fragments.diary.viewmodel.DiaryViewModel
import com.lee.foodi.ui.fragments.report.viewmodel.ReportViewModel
import com.lee.foodi.ui.fragments.user.viewmodel.SettingUserViewModel

class FoodiViewModelFactory(private val repository: FoodiRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(SearchFoodViewModel::class.java)){
            SearchFoodViewModel(repository) as T
        } else if(modelClass.isAssignableFrom(SettingUserViewModel::class.java)){
            SettingUserViewModel() as T
        } else if(modelClass.isAssignableFrom(DiaryViewModel::class.java)){
            DiaryViewModel(repository) as T
        } else if(modelClass.isAssignableFrom(ReportViewModel::class.java)){
            ReportViewModel(repository) as T
        } else if(modelClass.isAssignableFrom(AddFoodViewModel::class.java)){
            AddFoodViewModel(repository) as T
        } else {
            throw java.lang.IllegalArgumentException("해당 ViewModel을 찾을수가 없습니다!")
        }
    }
}
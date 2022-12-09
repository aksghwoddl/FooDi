package com.lee.foodi.ui.activities.add.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddFoodViewModel : ViewModel() {
    val headTitle = MutableLiveData<String>()
    val progress = MutableLiveData<Int>(1)
    val buttonText = MutableLiveData<String>()


}
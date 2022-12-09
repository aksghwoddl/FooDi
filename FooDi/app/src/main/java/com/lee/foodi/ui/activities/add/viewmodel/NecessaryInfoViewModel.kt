package com.lee.foodi.ui.activities.add.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NecessaryInfoViewModel : ViewModel() {
    var foodName = MutableLiveData<String>("")
}
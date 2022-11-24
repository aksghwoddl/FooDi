package com.lee.foodi.ui.fragments.user.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingUserViewModel : ViewModel() {
    var weight = 0
    var age = 0
    var gender = ""

    var maintenanceCalorie = MutableLiveData<String>()
    var genderButtonToggled = MutableLiveData<Boolean>()


}
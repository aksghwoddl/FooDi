package com.lee.foodi.common

import android.util.Log

const val PAGE_ONE = "1"
const val EXTRA_SELECTED_FOOD = "selectedFood"
const val NOT_ASSIGNED = ""

class Utils {
    companion object{
        private val TAG = "Utils"
        /**
         * Function that check value received from server (sometimes received minus or N/A)
         * **/
        fun checkErrorValue(value : String) : String {
            return if(value.contains("-")){
                Log.d(TAG , "convertErrorString: value = $value is minus")
                "0"
            } else if(value == "N/A"){
                ""
            } else {
                value
            }
        }
    }
}
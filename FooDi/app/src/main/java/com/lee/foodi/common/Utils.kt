package com.lee.foodi.common

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.util.Log
import android.view.WindowManager
import android.widget.Toast

const val PAGE_ONE = "1"
const val EXTRA_SELECTED_FOOD = "selectedFood"

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

        /**
         * Function that resize dialog
         * defaultDisplay is deprecated over 30
         * so I will divide function each version
         * **/

        fun dialogResize(context: Context, dialog: Dialog, width: Float, height: Float){
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R){ // sdk version under 30
                val display = windowManager.defaultDisplay
                val size = Point()

                display.getSize(size)

                val window = dialog.window

                val x = (size.x * width).toInt()
                val y = (size.y * height).toInt()

                window?.setLayout(x, y)

            }else{ // sdk version over 30
                val rect = windowManager.currentWindowMetrics.bounds

                val window = dialog.window
                val x = (rect.width() * width).toInt()
                val y = (rect.height() * height).toInt()

                window?.setLayout(x, y)
            }
        }

        /**
         * Function for toast message
         * **/

        fun toastMessage(message : String){
            Toast.makeText(FoodiNewApplication.getInstance() , message , Toast.LENGTH_SHORT).show()
        }
    }
}
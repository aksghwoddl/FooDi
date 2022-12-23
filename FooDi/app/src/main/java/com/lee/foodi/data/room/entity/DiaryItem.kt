package com.lee.foodi.data.room.entity

import com.lee.foodi.data.rest.model.Food
import java.io.Serializable

data class DiaryItem(
    var index : Int? = 0,
    var date : String = "",
    var food : Food?,
    var time : String = "",
    var servingSize : String = ""
) : Serializable
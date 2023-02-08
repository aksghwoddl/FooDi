package com.lee.domain.model.local

import com.lee.domain.model.remote.Food
import java.io.Serializable

/**
 * 조회한 다이어리의 음식 객체 class
 * **/
data class DiaryItem(
    var index : Int? = 0,
    var date : String = "",
    var food : Food,
    var time : String = "",
    var servingSize : String = ""
) : Serializable
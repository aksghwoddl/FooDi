package com.lee.domain.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lee.domain.model.common.DIARY_ITEM_TABLE
import com.lee.domain.model.remote.Food

/**
 * Domain의 DiaryItem Entity class
 * **/
@Entity(tableName = DIARY_ITEM_TABLE)
data class DiaryItemEntity(
    @PrimaryKey(autoGenerate = true)
    var index : Int? = 0,
    var date : String = "",
    var food : Food,
    var time : String = "",
    var servingSize : String  = ""
)

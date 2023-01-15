package com.lee.foodi.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lee.foodi.common.DIARY_ITEM_TABLE
import com.lee.foodi.data.rest.model.Food

@Entity(tableName = DIARY_ITEM_TABLE)
data class DiaryItemEntity(
    @PrimaryKey(autoGenerate = true)
    var index : Int? = 0,
    var date : String = "",
    var food : Food?,
    var time : String = "",
    var servingSize : String  = ""
)

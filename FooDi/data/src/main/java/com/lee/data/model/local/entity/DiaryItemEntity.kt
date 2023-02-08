package com.lee.data.model.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lee.data.common.DIARY_ITEM_TABLE
import com.lee.domain.model.remote.Food

/**
 * 다이어리에 기록된 음식 정보 Entity class
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

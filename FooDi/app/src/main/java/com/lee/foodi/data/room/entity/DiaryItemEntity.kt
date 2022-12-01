package com.lee.foodi.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lee.foodi.data.rest.model.FoodInfoData

@Entity(tableName = "diary_item_tbl")
data class DiaryItemEntity(
    @PrimaryKey(autoGenerate = true)
    var index : Int? = 0 ,
    var date : String = "" ,
    var food : FoodInfoData? ,
    var time : String = "" ,
    var servingSize : String  = ""
)

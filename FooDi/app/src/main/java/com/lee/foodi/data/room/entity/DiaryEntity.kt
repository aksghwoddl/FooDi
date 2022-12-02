package com.lee.foodi.data.room.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lee.foodi.data.rest.model.FoodInfoData

@Entity(tableName = "diary_tbl")
data class DiaryEntity(
    @PrimaryKey val date : String ,
    var totalCalorie : String ,
    var totalCarbon : String ,
    var totalProtein : String ,
    var totalFat : String
)


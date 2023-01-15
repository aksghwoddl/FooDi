package com.lee.foodi.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lee.foodi.common.DIARY_TABLE

@Entity(tableName = DIARY_TABLE)
data class DiaryEntity(
    @PrimaryKey val date : String ,
    var totalCalorie : String ,
    var totalCarbon : String ,
    var totalProtein : String ,
    var totalFat : String
)


package com.lee.data.model.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lee.data.common.DIARY_TABLE

/**
 * Diary 정보 Entity class
 * **/
@Entity(tableName = DIARY_TABLE)
data class DiaryEntity(
    @PrimaryKey val date : String ,
    var totalCalorie : String ,
    var totalCarbon : String ,
    var totalProtein : String ,
    var totalFat : String
)


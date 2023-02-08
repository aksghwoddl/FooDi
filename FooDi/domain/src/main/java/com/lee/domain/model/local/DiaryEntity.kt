package com.lee.domain.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lee.domain.model.common.DIARY_TABLE

/**
 * Domain의 DiaryEntity class
 */
@Entity(tableName = DIARY_TABLE)
data class DiaryEntity(
    @PrimaryKey val date : String ,
    var totalCalorie : String ,
    var totalCarbon : String ,
    var totalProtein : String ,
    var totalFat : String
)


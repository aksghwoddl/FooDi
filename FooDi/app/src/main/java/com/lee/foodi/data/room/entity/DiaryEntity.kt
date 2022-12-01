package com.lee.foodi.data.room.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lee.foodi.data.rest.model.FoodInfoData

@Entity
data class DiaryEntity(
    @PrimaryKey val date : String ,
    var totalCalorie : Int ,
    var totalCarbon : Int ,
    var totalProtein : Int ,
    var totalFat : Int
)


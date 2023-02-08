package com.lee.domain.model.local

/**
 * 조회한 Diary 객체 class
 * **/
data class Diary (
    val date : String ,
    var totalCalorie : String ,
    var totalCarbon : String ,
    var totalProtein : String ,
    var totalFat : String
)
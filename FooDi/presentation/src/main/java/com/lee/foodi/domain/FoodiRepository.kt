package com.lee.foodi.domain

import com.lee.foodi.data.rest.model.AddingFood
import com.lee.foodi.data.rest.model.SearchingFoodResponse
import com.lee.foodi.data.room.entity.Diary
import com.lee.foodi.data.room.entity.DiaryEntity
import com.lee.foodi.data.room.entity.DiaryItem
import com.lee.foodi.data.room.entity.DiaryItemEntity
import retrofit2.Response

interface FoodiRepository {
    /**
     * Function for search food at API
     * **/
    suspend fun getNewSearchFood(foodName : String , page : String) : Response<SearchingFoodResponse>

    suspend fun addNewFood(addingFood: AddingFood) : Response<Void>

    /**
     * Function for get food data in Room DB
     * **/
    suspend fun getAllDiaryItems(date : String ) : MutableList<DiaryItem>


    /**
     * Function for get diary summary in Room DB
     * **/
    suspend fun getDiarySummary(date : String) : Diary

    suspend fun addDiaryItem(diaryItem: DiaryItemEntity)
    /**
     * Function for add Diary in Room DB
     * **/
    suspend fun addDiary(diary : DiaryEntity)

    /**
     * Function for delete diary item in Room DB
     * **/
    suspend fun deleteDiaryItem(diaryItem: DiaryItemEntity)

    /**
     * Function for update diary item in Room DB
     * **/
    suspend fun updateDiaryItem(diaryItem: DiaryItemEntity)
}
package com.lee.foodi.data.repository

import com.lee.foodi.data.rest.RestServiceInstance
import com.lee.foodi.data.room.db.DiaryDatabase

class FoodiRepository {
    private val diaryDAO = DiaryDatabase.getInstance().diaryDao()

    /**
     * Function for search food at API
     * **/
    suspend fun getSearchFood(foodName : String , page : String) = RestServiceInstance.getInstance().getSearchFood(foodName , page)
    suspend fun getNewSearchFood(foodName : String , page : String) = RestServiceInstance.getInstance().getNewSearchFood(foodName , page) // Convert New Food InfoData

    /**
     * Function for get food data in Room DB
     * **/
    suspend fun getAllDiaryItems(date : String ) = diaryDAO.getDiaryItemByDate(date)


    /**
     * Function for get diary summary in Room DB
     * **/
    suspend fun getDiarySummary(date : String) = diaryDAO.getDiaryByDate(date)

}
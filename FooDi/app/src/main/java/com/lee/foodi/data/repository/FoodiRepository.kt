package com.lee.foodi.data.repository

import com.lee.foodi.data.rest.RestServiceInstance
import com.lee.foodi.data.rest.model.AddFoodData
import com.lee.foodi.data.room.db.DiaryDatabase
import com.lee.foodi.data.room.entity.DiaryEntity
import com.lee.foodi.data.room.entity.DiaryItemEntity

class FoodiRepository {
    private val diaryDAO = DiaryDatabase.getInstance().diaryDao()

    companion object{
        private lateinit var instance : FoodiRepository
        fun getInstance() : FoodiRepository {
            if(!::instance.isInitialized){
                instance = FoodiRepository()
            }
            return instance
        }
    }

    /**
     * Function for search food at API
     * **/
   // suspend fun getSearchFood(foodName : String , page : String) = RestServiceInstance.getInstance().getSearchFood(foodName , page) Convert New Food InfoData
    suspend fun getNewSearchFood(foodName : String , page : String) = RestServiceInstance.getInstance().getNewSearchFood(foodName , page)

    suspend fun addNewFood(addFoodData: AddFoodData) = RestServiceInstance.getInstance().addNewFood(addFoodData)

    /**
     * Function for get food data in Room DB
     * **/
    suspend fun getAllDiaryItems(date : String ) = diaryDAO.getDiaryItemByDate(date)


    /**
     * Function for get diary summary in Room DB
     * **/
    suspend fun getDiarySummary(date : String) = diaryDAO.getDiaryByDate(date)

    /**
     * Function for add Diary in Room DB
     * **/
    suspend fun addDiary(diary : DiaryEntity) = diaryDAO.addDiary(diary)

    suspend fun deleteDiaryItem(diaryItem: DiaryItemEntity) = diaryDAO.deleteDiaryItem(diaryItem)

}
package com.lee.foodi.data.repository

import com.lee.foodi.data.rest.RestService
import com.lee.foodi.data.rest.model.AddingFood
import com.lee.foodi.data.room.dao.DiaryDAO
import com.lee.foodi.data.room.entity.DiaryEntity
import com.lee.foodi.data.room.entity.DiaryItemEntity
import com.lee.foodi.domain.FoodiRepository
import javax.inject.Inject

class FoodiRepositoryImpl @Inject constructor(
    private val restService: RestService ,
    private val diaryDAO : DiaryDAO
) : FoodiRepository {

    /**
     * Function for search food at API
     * **/
    override suspend fun getNewSearchFood(foodName : String , page : String) = restService.getNewSearchFood(foodName , page)

    override suspend fun addNewFood(addingFood: AddingFood) = restService.addNewFood(addingFood)

    /**
     * Function for get food data in Room DB
     * **/
    override suspend fun getAllDiaryItems(date : String ) = diaryDAO.getDiaryItemByDate(date)


    /**
     * Function for get diary summary in Room DB
     * **/
    override suspend fun getDiarySummary(date : String) = diaryDAO.getDiaryByDate(date)

    override suspend fun addDiaryItem(diaryItem: DiaryItemEntity) = diaryDAO.addDiaryItem(diaryItem)

    /**
     * Function for add Diary in Room DB
     * **/
    override suspend fun addDiary(diary : DiaryEntity) = diaryDAO.addDiary(diary)

    /**
     * Function for delete diary item in Room DB
     * **/
    override suspend fun deleteDiaryItem(diaryItem: DiaryItemEntity) = diaryDAO.deleteDiaryItem(diaryItem)

    /**
     * Function for update diary item in Room DB
     * **/
    override suspend fun updateDiaryItem(diaryItem: DiaryItemEntity) = diaryDAO.updateDiaryItem(diaryItem)

}
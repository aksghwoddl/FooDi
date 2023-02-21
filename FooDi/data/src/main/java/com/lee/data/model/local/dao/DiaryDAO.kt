package com.lee.data.model.local.dao

import androidx.room.*
import com.lee.data.model.local.entity.DiaryEntity
import com.lee.data.model.local.entity.DiaryItemEntity
import com.lee.domain.model.local.Diary
import com.lee.domain.model.local.DiaryItem

/**
 * Room의 DAO class
 * **/
@Dao
interface DiaryDAO {
    /**
     * Function for add diary item (food , date , calories ...)
     * **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDiaryItem(item : DiaryItemEntity)

    /**
     * Function for add summary of diary , because need to draw graph or showing previous diary
     * **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDiary(diary : DiaryEntity)

    /**
     * Function for search diary item
     * **/
    @Query("SELECT * FROM diary_item_tbl WHERE date =:queryDate")
    suspend fun getDiaryItemsByDate(queryDate : String) : MutableList<DiaryItem>

    /**
     * Function for search diary summary
     * **/
    @Query("SELECT * FROM diary_tbl WHERE date = :queryDate")
    suspend fun getDiaryByDate(queryDate : String) : Diary


    /**
     * Function for delete diary item
     * **/
    @Delete
    suspend fun deleteDiaryItem(diaryItem : DiaryItemEntity)

    /**
     * Function for update diary item
     * **/
    @Update
    suspend fun updateDiaryItem(diaryItem: DiaryItemEntity)
}
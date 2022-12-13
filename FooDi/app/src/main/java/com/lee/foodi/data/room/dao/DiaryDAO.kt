package com.lee.foodi.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lee.foodi.data.room.entity.Diary
import com.lee.foodi.data.room.entity.DiaryEntity
import com.lee.foodi.data.room.entity.DiaryItem
import com.lee.foodi.data.room.entity.DiaryItemEntity

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
    suspend fun getDiaryItemByDate(queryDate : String) : MutableList<DiaryItem>

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

    @Update
    suspend fun updateDiaryItem(diaryItem: DiaryItemEntity)
}
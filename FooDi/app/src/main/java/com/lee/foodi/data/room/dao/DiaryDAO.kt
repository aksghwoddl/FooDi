package com.lee.foodi.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lee.foodi.data.room.entity.DiaryItem
import com.lee.foodi.data.room.entity.DiaryItemEntity

@Dao
interface DiaryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDiaryItem(item : DiaryItemEntity)

    @Query("SELECT * FROM DIARY_ITEM_TBL WHERE date =:queryDate")
    suspend fun getDiaryItemByDate(queryDate : String) : MutableList<DiaryItem>
}
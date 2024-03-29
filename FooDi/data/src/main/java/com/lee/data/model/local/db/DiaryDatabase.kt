package com.lee.data.model.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lee.data.model.local.dao.DiaryDAO
import com.lee.data.model.local.TypeConverter
import com.lee.data.model.local.entity.DiaryEntity
import com.lee.data.model.local.entity.DiaryItemEntity

/**
 * 다이어리 Database class
 * **/
@Database(entities = [DiaryItemEntity::class , DiaryEntity::class] , version = 1 , exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class DiaryDatabase : RoomDatabase() {
    abstract fun diaryDao() : DiaryDAO
}
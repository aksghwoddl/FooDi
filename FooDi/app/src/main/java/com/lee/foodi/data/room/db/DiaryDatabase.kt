package com.lee.foodi.data.room.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lee.foodi.common.DB_NAME
import com.lee.foodi.common.FoodiNewApplication
import com.lee.foodi.data.room.TypeConverter
import com.lee.foodi.data.room.dao.DiaryDAO
import com.lee.foodi.data.room.entity.DiaryEntity
import com.lee.foodi.data.room.entity.DiaryItemEntity

@Database(entities = [DiaryItemEntity::class , DiaryEntity::class] , version = 1 , exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class DiaryDatabase : RoomDatabase() {
    abstract fun diaryDao() : DiaryDAO
}
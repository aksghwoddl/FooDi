package com.lee.data.mapper

import com.lee.data.model.local.entity.DiaryEntity
import com.lee.data.model.local.entity.DiaryItemEntity
import com.lee.domain.model.local.Diary
import com.lee.domain.model.local.DiaryItem

/**
 * Local data를 mapping 해주는 class (현재 프로젝트에서는 사용하지 않음)
 * **/
object LocalMapper {
    fun mapperToDiaryItemEntity(diaryItem: DiaryItem) : DiaryItemEntity {
        val diaryItemEntity : DiaryItemEntity
        with(diaryItem){
            diaryItemEntity = DiaryItemEntity(index, date, food, time, servingSize)
        }
        return diaryItemEntity
    }

    fun mapperToDiaryEntity(diary: Diary) : DiaryEntity {
        val diaryEntity : DiaryEntity
        with(diary){
            diaryEntity = DiaryEntity(date, totalCalorie, totalCarbon, totalProtein, totalFat)
        }
        return diaryEntity
    }
}
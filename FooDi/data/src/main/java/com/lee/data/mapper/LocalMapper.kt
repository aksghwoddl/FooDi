package com.lee.data.mapper

import com.lee.data.model.local.entity.DiaryEntity
import com.lee.domain.model.local.DiaryItemEntity

/**
 * Local data를 mapping 해주는 class (현재 프로젝트에서는 사용하지 않음)
 * **/
object LocalMapper {
    fun mapperToDiaryItemEntity(diaryItemEntity: com.lee.data.model.local.entity.DiaryItemEntity) : DiaryItemEntity {
        val ret : DiaryItemEntity
        with(diaryItemEntity){
            ret = DiaryItemEntity(index, date, food, time, servingSize)
        }
        return ret
    }

    fun mapperToDiaryEntity(diaryEntity: DiaryEntity) : com.lee.domain.model.local.DiaryEntity{
        val ret : com.lee.domain.model.local.DiaryEntity
        with(diaryEntity){
            ret= com.lee.domain.model.local.DiaryEntity(date, totalCalorie, totalCarbon, totalProtein, totalFat)
        }
        return ret
    }
}
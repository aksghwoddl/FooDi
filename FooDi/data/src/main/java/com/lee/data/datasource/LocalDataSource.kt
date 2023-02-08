package com.lee.data.datasource

import com.lee.data.api.room.DiaryDAO
import com.lee.domain.model.local.Diary
import com.lee.domain.model.local.DiaryEntity
import com.lee.domain.model.local.DiaryItem
import com.lee.domain.model.local.DiaryItemEntity
import javax.inject.Inject

interface LocalDataSource{
    suspend fun getAllDiaryItems(date : String) : MutableList<DiaryItem>

    suspend fun getDiary(date : String) : Diary

    suspend fun addDiaryItem(item: DiaryItemEntity)

    suspend fun addDiary(diary : DiaryEntity)

    suspend fun deleteDiaryItem(diaryItemEntity: DiaryItemEntity)

    suspend fun updateDiaryItem(diaryItemEntity: DiaryItemEntity)
}

class LocalDataSourceImpl @Inject constructor(
    private val diaryDAO: DiaryDAO
) : LocalDataSource {
    override suspend fun getAllDiaryItems(date: String) = diaryDAO.getDiaryItemsByDate(date)

    override suspend fun getDiary(date: String) = diaryDAO.getDiaryByDate(date)

    override suspend fun addDiaryItem(item: DiaryItemEntity) = diaryDAO.addDiaryItem(item)

    override suspend fun addDiary(diary: DiaryEntity)  = diaryDAO.addDiary(diary)

    override suspend fun deleteDiaryItem(diaryItemEntity: DiaryItemEntity) = diaryDAO.deleteDiaryItem(diaryItemEntity)

    override suspend fun updateDiaryItem(diaryItemEntity: DiaryItemEntity)  = diaryDAO.updateDiaryItem(diaryItemEntity)
}
package com.lee.data.datasource

import com.lee.data.mapper.LocalMapper
import com.lee.data.model.local.dao.DiaryDAO
import com.lee.domain.model.local.Diary
import com.lee.domain.model.local.DiaryItem
import javax.inject.Inject

interface LocalDataSource{
    suspend fun getAllDiaryItems(date : String) : MutableList<DiaryItem>

    suspend fun getDiary(date : String) : Diary

    suspend fun addDiaryItem(diaryItem : DiaryItem)

    suspend fun addDiary(diary : Diary)

    suspend fun deleteDiaryItem(diaryItem : DiaryItem)

    suspend fun updateDiaryItem(diaryItem : DiaryItem)
}

class LocalDataSourceImpl @Inject constructor(
    private val diaryDAO: DiaryDAO
) : LocalDataSource {
    override suspend fun getAllDiaryItems(date: String) = diaryDAO.getDiaryItemsByDate(date)

    override suspend fun getDiary(date: String) = diaryDAO.getDiaryByDate(date)

    override suspend fun addDiaryItem(diaryItem: DiaryItem) = diaryDAO.addDiaryItem(LocalMapper.mapperToDiaryItemEntity(diaryItem))

    override suspend fun addDiary(diary: Diary)  = diaryDAO.addDiary(LocalMapper.mapperToDiaryEntity(diary))

    override suspend fun deleteDiaryItem(diaryItem : DiaryItem) = diaryDAO.deleteDiaryItem(LocalMapper.mapperToDiaryItemEntity(diaryItem))

    override suspend fun updateDiaryItem(diaryItem : DiaryItem)  = diaryDAO.updateDiaryItem(LocalMapper.mapperToDiaryItemEntity(diaryItem))
}
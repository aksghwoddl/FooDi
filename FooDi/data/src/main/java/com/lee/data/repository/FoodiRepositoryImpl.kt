package com.lee.data.repository

import com.lee.data.datasource.LocalDataSource
import com.lee.data.datasource.RemoteDateSource
import com.lee.domain.model.local.Diary
import com.lee.domain.model.local.DiaryItem
import com.lee.domain.model.remote.AddingFoodRequest
import com.lee.domain.repository.FoodiRepository
import javax.inject.Inject

/**
 * Repository의 구현부
 * **/
class FoodiRepositoryImpl @Inject constructor(
    private val remoteDateSource: RemoteDateSource ,
    private val localDataSource: LocalDataSource
) : FoodiRepository {

    /**
     * 음식 정보 검색하기
     * **/
    override suspend fun getSearchFood(foodName: String, page: String) = remoteDateSource.getSearchFood(foodName , page)

    /**
     * 새로운 음식 서버에 추가하기
     * **/
    override suspend fun addNewFood(addingFood: AddingFoodRequest) = remoteDateSource.addNewFood(addingFood)

    /**
     * 다이어리에 기록된 음식 가져오기
     * **/
    override suspend fun getAllDiaryItems(date: String) = localDataSource.getAllDiaryItems(date)

    /**
     * 선택된 날짜의 다이어리 정보 가져오기
     * **/
    override suspend fun getDiaryByDate(date: String) = localDataSource.getDiary(date)

    /**
     * 다이어리에 음식 정보 추가하기
     * **/
    override suspend fun addDiaryItem(diaryItem: DiaryItem) = localDataSource.addDiaryItem(diaryItem)

    /**
     * 당일의 다이어리 정보 추가하기
     * **/
    override suspend fun addDiary(diary: Diary) = localDataSource.addDiary(diary)

    /**
     * 다이어리에 기록된 음식 삭제하기
     * **/
    override suspend fun deleteDiaryItem(diaryItem: DiaryItem) = localDataSource.deleteDiaryItem(diaryItem)

    /**
     * 다이어리에 기록된 음식 업데이트하기
     * **/
    override suspend fun updateDiaryItem(diaryItem: DiaryItem) = localDataSource.updateDiaryItem(diaryItem)
}
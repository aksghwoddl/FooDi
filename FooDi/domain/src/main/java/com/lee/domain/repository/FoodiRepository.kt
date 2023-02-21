package com.lee.domain.repository

import com.lee.domain.model.local.Diary
import com.lee.domain.model.local.DiaryItem
import com.lee.domain.model.remote.AddingFoodRequest
import com.lee.domain.model.remote.SearchingFood
import retrofit2.Response

/**
 * Repository Interface
 * **/
interface FoodiRepository {
    /**
     * 음식 정보 검색하기
     * **/
    suspend fun getSearchFood(foodName : String , page : String) : SearchingFood

    /**
     * 새로운 음식 서버에 추가하기
     * **/
    suspend fun addNewFood(addingFood: AddingFoodRequest) : Response<Void>

    /**
     * 다이어리에 기록된 음식 가져오기
     * **/
    suspend fun getAllDiaryItems(date : String) : MutableList<DiaryItem>


    /**
     * 선택된 날짜의 다이어리 정보 가져오기
     * **/
    suspend fun getDiaryByDate(date : String) : Diary

    /**
     * 다이어리에 음식 정보 추가하기
     * **/
    suspend fun addDiaryItem(diaryItem: DiaryItem)

    /**
     * 당일의 다이어리 정보 추가하기
     * **/
    suspend fun addDiary(diary : Diary)

    /**
     * 다이어리에 기록된 음식 삭제하기
     * **/
    suspend fun deleteDiaryItem(diaryItem: DiaryItem)

    /**
     * 다이어리에 기록된 음식 업데이트하기
     * **/
    suspend fun updateDiaryItem(diaryItem: DiaryItem)
}
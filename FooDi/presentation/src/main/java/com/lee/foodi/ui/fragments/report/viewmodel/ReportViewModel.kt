package com.lee.foodi.ui.fragments.report.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.domain.model.local.Diary
import com.lee.domain.usecase.GetDiary
import com.lee.foodi.common.LATELY
import com.lee.foodi.common.MONTHLY
import com.lee.foodi.common.ResourceProvider
import com.lee.foodi.common.WEEKS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

/**
 * 리포트 ViewModel
 * **/
private const val TAG = "ReportViewModel"
@HiltViewModel
class ReportViewModel @Inject constructor(
    private val getDiary: GetDiary,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val _summaryList = MutableLiveData<MutableList<Diary>>() // Diary Summary List
    val summaryList : LiveData<MutableList<Diary>>
    get() = _summaryList

    private val _averageCalorie = MutableLiveData<String>("0") // Average Calorie
    val averageCalorie : LiveData<String>
    get() = _averageCalorie

    fun setSummaryList(list : MutableList<Diary>){
        _summaryList.value = list
    }

    fun setAverageCalorie(calorie : String){
        _averageCalorie.value = calorie
    }

    private var days = mutableListOf<Int>()

    /**
     * 총 소비 칼로리를 불러오는 함수
     * **/
    suspend fun getDiarySummary(selectedSection : String) : MutableList<Diary>? {
        if(days.isNotEmpty()){
            days.clear()
        }
        val calendar = Calendar.getInstance()
        val diarySummaryList = mutableListOf<Diary>()
        val ret = viewModelScope.async{
            val list = withContext(Dispatchers.IO){
                when(selectedSection){
                    LATELY -> {
                        val year = calendar.get(Calendar.YEAR)
                        val month = calendar.get(Calendar.MONTH) + 1
                        val day = calendar.get(Calendar.DATE)
                        for(i in 0 .. 2){
                            val insertDate = day - i
                            if(insertDate != 0){
                                days.add(insertDate)
                            }
                        }
                        days.reverse()
                        days.forEach {
                            val queryDate = LocalDate.of(year , month , it).format(DateTimeFormatter.ofPattern("yyyy년MM월dd일"))
                            diarySummaryList.add(getDiary.invoke(queryDate))
                        }
                        diarySummaryList
                    }
                    WEEKS -> {
                        val year = calendar.get(Calendar.YEAR)
                        val month = calendar.get(Calendar.MONTH) + 1
                        val day = calendar.get(Calendar.DATE)
                        for(i in 0 .. 6){
                            val insertDate = day - i
                            if(insertDate != 0){
                                days.add(day - i)
                            }
                        }
                        days.reverse()
                        days.forEach {
                            val queryDate = LocalDate.of(year , month , it).format(DateTimeFormatter.ofPattern("yyyy년MM월dd일"))
                            diarySummaryList.add(getDiary.invoke(queryDate))
                        }
                        diarySummaryList
                    }
                    MONTHLY -> {
                        val year = calendar.get(Calendar.YEAR)
                        val month = calendar.get(Calendar.MONTH) + 1
                        val dayCounts = getDayCountInMonth(year , month)
                        for(i in 1 .. dayCounts){
                            days.add(i)
                        }
                        days.forEach {
                            val queryDate = LocalDate.of(year , month , it).format(DateTimeFormatter.ofPattern("yyyy년MM월dd일"))
                            diarySummaryList.add(getDiary.invoke(queryDate))
                        }
                        diarySummaryList
                    }
                    else -> {
                        null
                    }
                }
            }
           list
        }
        return ret.await()
    }

    fun getDays() = days

    /**
     * 입력된 달의 총 날짜를 출력하는 함수
     * **/
    private fun getDayCountInMonth(year : Int , month : Int) : Int {
        Log.d(TAG, "getDayCountInMonth: year = $year , month = $month")
        return when(month - 1){
            Calendar.JANUARY , Calendar.MARCH , Calendar.MAY , Calendar.JULY , Calendar.AUGUST , Calendar.OCTOBER , Calendar.DECEMBER -> 31
            Calendar.APRIL, Calendar.JUNE , Calendar.SEPTEMBER , Calendar.NOVEMBER -> 30
            Calendar.FEBRUARY -> {
                if(year % 4 == 0 && year % 100 != 0 || year % 400 == 0) 29
                else 28
            }
            else -> throw java.lang.IllegalArgumentException("Invalid Month!!")
        }
    }
}
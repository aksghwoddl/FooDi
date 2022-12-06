package com.lee.foodi.ui.fragments.report.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.foodi.data.repository.FoodiRepository
import com.lee.foodi.data.room.entity.Diary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

private const val TAG = "ReportViewModel"

private const val LATELY = "최근"
private const val WEEKS = "7일"
private const val MONTHLY = "한달"

class ReportViewModel(private val repository: FoodiRepository) : ViewModel() {
    val summaryList = MutableLiveData<MutableList<Diary>>()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getDiarySummary(selectedSection : String) : MutableList<Diary>? {
        val calendar = Calendar.getInstance()
        val diarySummaryList = mutableListOf<Diary>()
        val ret = CoroutineScope(Dispatchers.IO).async {
            when(selectedSection){
                LATELY -> {
                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH) + 1
                    val day = calendar.get(Calendar.DATE)
                    val dayList = mutableListOf<Int>()
                    for(i in 0 .. 2){
                        dayList.add(day  - i)
                    }
                    dayList.reverse()
                    dayList.forEach {
                        val queryDate = LocalDate.of(year , month , it).format(DateTimeFormatter.ofPattern("yyyy년MM월dd일"))
                        diarySummaryList.add(repository.getDiarySummary(queryDate))
                    }
                    diarySummaryList
                }
                WEEKS -> {
                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH) + 1
                    val day = calendar.get(Calendar.DATE)
                    val dayList = mutableListOf<Int>()
                    for(i in 0 .. 6){
                        val insertDate = day - i
                        if(insertDate != 0){
                            dayList.add(day - i)
                        }
                    }
                    dayList.reverse()
                    dayList.forEach {
                        val queryDate = LocalDate.of(year , month , it).format(DateTimeFormatter.ofPattern("yyyy년MM월dd일"))
                        diarySummaryList.add(repository.getDiarySummary(queryDate))
                    }
                    diarySummaryList
                }
                MONTHLY -> {
                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH) + 1
                    val dayCounts = getDayCountInMonth(year , month)
                    val dayList = mutableListOf<Int>()
                    for(i in 1 .. dayCounts){
                        dayList.add(i)
                    }
                    dayList.forEach {
                        val queryDate = LocalDate.of(year , month , it).format(DateTimeFormatter.ofPattern("yyyy년MM월dd일"))
                        diarySummaryList.add(repository.getDiarySummary(queryDate))
                    }
                    diarySummaryList
                }
                else -> {
                    null
                }
            }
        }
        Log.d(TAG, "getDiarySummary: ${ret.await()}")
        return  ret.await()
    }

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
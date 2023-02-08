package com.lee.foodi.ui.fragments.report

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.lee.foodi.R
import com.lee.foodi.common.Utils
import com.lee.foodi.common.manager.FooDiPreferenceManager
import com.lee.foodi.databinding.FragmentReportBinding
import com.lee.foodi.ui.base.BaseFragment
import com.lee.foodi.ui.fragments.report.viewmodel.ReportViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

/**
 * 리포트 Fragment class
 * **/
@AndroidEntryPoint
class ReportFragment : BaseFragment<FragmentReportBinding>(R.layout.fragment_report) {
    private val mViewModel : ReportViewModel by viewModels()
    private lateinit var preferenceManager : FooDiPreferenceManager

    companion object{
        fun newInstance() = ReportFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        preferenceManager = FooDiPreferenceManager.getInstance(requireContext())
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinner()
        initChart()
        observeData()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val bSummaryList = withContext(Dispatchers.IO){
                mViewModel.getDiarySummary(binding.reportSelection.selectedItem.toString())
            }
            bSummaryList?.let {
                mViewModel.setSummaryList(it)
            }
            preferenceManager = FooDiPreferenceManager.getInstance(requireContext())
            if(preferenceManager.goalCalorie  != "0"){
                binding.reportChart.axisLeft.setAxisMaxValue(preferenceManager.goalCalorie!!.toFloat())
                binding.reportChart.axisRight.setAxisMaxValue(preferenceManager.goalCalorie!!.toFloat())
            }
            binding.reportChart.animateY(1000)
            mViewModel.setAverageCalorie(calculateAverageCalorie())
        }
    }


    /**
     * Spinner 초기화 하는 함수
     * **/
    private fun initSpinner() {
        val selectionArray = resources.getStringArray(R.array.report_selection_array)
        val adapter = ArrayAdapter<String>(requireContext() , R.layout.spinner_item , selectionArray)
        with(binding){
            reportSelection.adapter = adapter
            reportSelection.onItemSelectedListener = object : OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    lifecycleScope.launch {
                        with(mViewModel){
                            val bSummaryList = withContext(Dispatchers.IO){
                                getDiarySummary(reportSelection.selectedItem.toString())
                            }
                            bSummaryList?.let {
                                setSummaryList(it)
                            }
                           setAverageCalorie(calculateAverageCalorie())
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) { }
            }
        }
    }

    /**
     * 차트 초기화 하는 함수
     * **/
    private fun initChart() {
        binding.reportChart.apply {
            description.isEnabled = false
            extraBottomOffset = 20f
            val xAxis = xAxis
            with(xAxis){
                position = XAxis.XAxisPosition.BOTTOM // xAxis 위치
                granularity = 1f
                textColor = Color.BLACK
                setDrawAxisLine(false) // xAxis 선 draw 여부
                setDrawGridLines(false)
                typeface = Typeface.createFromAsset(requireContext().assets , "swagger.ttf")
                textColor = resources.getColor(R.color.text_colors)
            }

            val yAxis = axisLeft
            with(yAxis){
                textColor = Color.BLACK
                setDrawAxisLine(false)
                typeface = Typeface.createFromAsset(requireContext().assets , "swagger.ttf")
                textColor = resources.getColor(R.color.text_colors)
                setStartAtZero(true)
                if(preferenceManager.goalCalorie  != "0"){
                    setAxisMaxValue(preferenceManager.goalCalorie!!.toFloat())
                }
            }

            val rightAxis = axisRight
            with(rightAxis){
                textColor = Color.BLACK
                setDrawAxisLine(false)
                typeface = Typeface.createFromAsset(requireContext().assets , "swagger.ttf")
                textColor = resources.getColor(R.color.text_colors)
                setStartAtZero(true)
                if(preferenceManager.goalCalorie  != "0"){
                    setAxisMaxValue(preferenceManager.goalCalorie!!.toFloat())
                }
            }

            val legend = legend
            with(legend){
                form = Legend.LegendForm.LINE
                textSize = 10f
                textColor = resources.getColor(R.color.text_colors)
                typeface = Typeface.createFromAsset(requireContext().assets , "swagger.ttf")
                verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                orientation = Legend.LegendOrientation.HORIZONTAL
                setDrawInside(false)
            }
        }
    }

    /**
     * Function for observe Live data
     * **/
    private fun observeData() {
        with(mViewModel){
            summaryList.observe(viewLifecycleOwner){ // 불러온 총 소비 칼로리 리스트
                val labelList = arrayListOf<String>()
                val dayList = getDays()
                dayList.forEach {
                    labelList.add("${it}일")
                }
                binding.reportChart.xAxis.valueFormatter = MyValueFormatter(labelList)
                setData()
            }

            averageCalorie.observe(viewLifecycleOwner){ // 평균 칼로리
                Utils.convertValueWithErrorCheck(binding.averageCalorieTextView , getString(R.string.average_calorie) , it)
            }
        }
    }

    /**
     * 평균칼로리를 계산하는 함수
     * **/
    private fun calculateAverageCalorie() : String {
        val diaryList = mViewModel.summaryList.value
        var sum  = 0.0
        diaryList?.forEach { diary ->
           diary?.let { sum += it.totalCalorie.toDouble() }
        }
        return (sum/diaryList!!.size).roundToInt().toString()
    }

    /**
     * 그래프 데이터를 세팅하는 함수
     * **/
    private fun setData() {
        val valueList = ArrayList<BarEntry>()
        val title = getString(R.string.spend_calorie_title)
        val summaryList = mViewModel.summaryList.value
        summaryList?.forEachIndexed{index , diary ->
            diary?.let { valueList.add(BarEntry(index.toFloat() , diary.totalCalorie.toFloat())) }
        }
        val barDataSet = BarDataSet(valueList , title)
        barDataSet.color = requireContext().getColor(R.color.main_color)
        val barData = BarData(barDataSet)
        val tf = Typeface.createFromAsset(requireContext().assets , "swagger.ttf")
        barData.setValueTypeface(tf)
        binding.reportChart.apply {
            setScaleEnabled(false)
            data = barData
            invalidate()
        }
    }

    /**
     * xAxis의 값을 바꾸기 위한 ValueFormatter class
     * **/
    private class MyValueFormatter(private val dateList : ArrayList<String>) : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return dateList.getOrNull(value.toInt()) ?: value.toString()
        }
    }
}
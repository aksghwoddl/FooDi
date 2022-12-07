package com.lee.foodi.ui.fragments.report

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.lee.foodi.R
import com.lee.foodi.common.FoodiNewApplication
import com.lee.foodi.common.MONTHLY
import com.lee.foodi.common.Utils
import com.lee.foodi.data.repository.FoodiRepository
import com.lee.foodi.databinding.FragmentReportBinding
import com.lee.foodi.ui.factory.FoodiViewModelFactory
import com.lee.foodi.ui.fragments.report.viewmodel.ReportViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val TAG = "ReportFragment"

class ReportFragment : Fragment() {
    private lateinit var binding : FragmentReportBinding
    private lateinit var mViewModel : ReportViewModel

    companion object{
        fun newInstance() = ReportFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportBinding.inflate(inflater , container , false)
        mViewModel = ViewModelProvider(this , FoodiViewModelFactory(FoodiRepository()))[ReportViewModel::class.java]
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinner()
        initChart()
        CoroutineScope(Dispatchers.IO).launch {
            mViewModel.summaryList.postValue(mViewModel.getDiarySummary(binding.reportSelection.selectedItem.toString()))
            CoroutineScope(Dispatchers.Main).launch{
                setData()
                mViewModel.averageCalorie.value = calculateAverageCalorie()
            }
        }
        observeDate()
    }


    /**
     * Function for init Spinner
     * **/
    private fun initSpinner() {
        val selectionArray = resources.getStringArray(R.array.report_selection_array)
        val adapter = ArrayAdapter<String>(FoodiNewApplication.getInstance() , R.layout.spinner_item , selectionArray)
        with(binding){
            reportSelection.adapter = adapter
            reportSelection.onItemSelectedListener = object : OnItemSelectedListener{
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    CoroutineScope(Dispatchers.IO).launch {
                        mViewModel.summaryList.postValue(mViewModel.getDiarySummary(reportSelection.selectedItem.toString()))
                        CoroutineScope(Dispatchers.Main).launch{
                            mViewModel.averageCalorie.value = calculateAverageCalorie()
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) { }
            }
        }
    }

    /**
     * Function for init Chart
     * **/
    private fun initChart() {
        binding.reportChart.apply {
            description.isEnabled = false
            extraBottomOffset = 20f

            animateX(1000)
            animateY(1000)
            val xAxis = xAxis
            with(xAxis){
                position = XAxis.XAxisPosition.BOTTOM // xAxis 위치
                granularity = 1f
                textColor = Color.BLACK
                setDrawAxisLine(false) // xAxis 선 draw 여부
                setDrawGridLines(false)
                typeface = Typeface.createFromAsset(requireContext().assets , "swagger.ttf")
            }

            val yAxis = axisLeft
            with(yAxis){
                textColor = Color.BLACK
                setDrawAxisLine(false)
                typeface = Typeface.createFromAsset(requireContext().assets , "swagger.ttf")
            }

            val rightAxis = axisRight
            with(rightAxis){
                textColor = Color.BLACK
                setDrawAxisLine(false)
                typeface = Typeface.createFromAsset(requireContext().assets , "swagger.ttf")
            }

            val legend = legend
            with(legend){
                form = Legend.LegendForm.LINE
                textSize = 10f
                textColor = Color.BLACK
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
    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeDate() {
        with(mViewModel){
            // Summary List
            summaryList.observe(viewLifecycleOwner){ diary ->
                val labelList = arrayListOf<String>()
                val dayList = getDayList()
                dayList.forEach {
                    labelList.add("${it}일")
                }
                binding.reportChart.xAxis.valueFormatter = MyValueFormatter(labelList)
                setData()
            }

            // Average Calorie
            averageCalorie.observe(viewLifecycleOwner){
                Utils.convertValueWithErrorCheck(binding.averageCalorieTextView , getString(R.string.average_calorie) , it)
            }
        }
    }

    /**
     * Calculate Average Calorie as selected section
     * **/
    private fun calculateAverageCalorie() : String {
        val diaryList = mViewModel.summaryList.value
        var sum  = 0.0
        diaryList?.forEach {
            if(it != null){
                sum += it.totalCalorie.toDouble()
            }
        }
        return (sum/diaryList!!.size).roundToInt().toString()
    }

    /**
     * Function for set data at char
     * **/
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData() {
        val valueList = ArrayList<BarEntry>()
        val title = "칼로리 소모량"
        CoroutineScope(Dispatchers.IO).launch {
            val summaryList = mViewModel.summaryList.value
            summaryList?.forEachIndexed{index , diary ->
                if(diary != null){
                    valueList.add(BarEntry(index.toFloat() , diary.totalCalorie.toFloat()))
                }
            }
            val barDataSet = BarDataSet(valueList , title)
            barDataSet.color = requireContext().getColor(R.color.main_color)
            val barData = BarData(barDataSet)
            val tf = Typeface.createFromAsset(requireContext().assets , "swagger.ttf")
            barData.setValueTypeface(tf)
            CoroutineScope(Dispatchers.Main).launch{
                binding.reportChart.apply {
                    setScaleEnabled(false)
                    data = barData
                    invalidate()
                }
            }
        }
    }

    /**
     * ValueFormatter for convert chart xAxis's label
     * **/
    private inner class MyValueFormatter(private val dateList : ArrayList<String>) : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return dateList.getOrNull(value.toInt()) ?: value.toString()
        }
    }
}
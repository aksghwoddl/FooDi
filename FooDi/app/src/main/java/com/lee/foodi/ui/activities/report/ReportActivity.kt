package com.lee.foodi.ui.activities.report

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.lee.foodi.R
import com.lee.foodi.databinding.ActivityReportBinding

class ReportActivity : AppCompatActivity() {
    private lateinit var binding : ActivityReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        initSpinner()
    }

    private fun initSpinner() {
        val selectionArray = resources.getStringArray(R.array.report_selection_array)
        val adapter = ArrayAdapter<String>(this , R.layout.spinner_item , selectionArray)
        binding.reportSelection.adapter = adapter
    }
}
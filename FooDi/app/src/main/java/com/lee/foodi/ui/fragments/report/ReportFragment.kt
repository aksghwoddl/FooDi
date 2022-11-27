package com.lee.foodi.ui.fragments.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.lee.foodi.R
import com.lee.foodi.common.FoodiNewApplication
import com.lee.foodi.databinding.FragmentReportBinding

class ReportFragment : Fragment() {
    private lateinit var binding : FragmentReportBinding

    companion object{
        fun newInstance() = ReportFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinner()
    }

    private fun initSpinner() {
        val selectionArray = resources.getStringArray(R.array.report_selection_array)
        val adapter = ArrayAdapter<String>(FoodiNewApplication.getInstance() , R.layout.spinner_item , selectionArray)
        binding.reportSelection.adapter = adapter
    }
}
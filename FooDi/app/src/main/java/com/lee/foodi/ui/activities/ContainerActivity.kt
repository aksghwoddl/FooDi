package com.lee.foodi.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.lee.foodi.R
import com.lee.foodi.databinding.ActivityContainerBinding
import com.lee.foodi.ui.activities.adapter.ContainerFragmentStateAdapter
import com.lee.foodi.ui.fragments.diary.DiaryFragment
import com.lee.foodi.ui.fragments.report.ReportFragment
import com.lee.foodi.ui.fragments.user.UserFragment

class ContainerActivity : AppCompatActivity() {
    private lateinit var binding : ActivityContainerBinding
    private lateinit var mReportFragment : ReportFragment
    private lateinit var mDiaryFragment : DiaryFragment
    private lateinit var mUserFragment: UserFragment
    private lateinit var mFragmentStateAdapter : ContainerFragmentStateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContainerBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        mFragmentStateAdapter = ContainerFragmentStateAdapter(this)

        savedInstanceState?: initFragmentAdapter()
    }


    /**
     * Function for init FragmentStateAdapter
     * **/
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initFragmentAdapter() {
        val tabImageArray = arrayOf(
            resources.getDrawable(R.drawable.graph_icon  , null),
            resources.getDrawable(R.drawable.note , null),
            resources.getDrawable(R.drawable.ic_baseline_settings_24 , null)
        )

        mReportFragment = ReportFragment.newInstance()
        mDiaryFragment = DiaryFragment.newInstance()
        mUserFragment = UserFragment.newInstance()
        mFragmentStateAdapter.appendFragment(mReportFragment)
        mFragmentStateAdapter.appendFragment(mDiaryFragment)
        mFragmentStateAdapter.appendFragment(mUserFragment)

        with(binding){
            containerViewPager.adapter = mFragmentStateAdapter
            TabLayoutMediator(bottomTabLayout , containerViewPager){tab , position ->
                tab.icon = tabImageArray[position]
            }.attach()
            val initialTab = binding.bottomTabLayout.getTabAt(1)
            initialTab?.select()
        }
    }
}
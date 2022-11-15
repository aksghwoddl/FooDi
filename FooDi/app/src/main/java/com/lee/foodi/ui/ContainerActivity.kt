package com.lee.foodi.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.lee.foodi.R
import com.lee.foodi.databinding.ActivityContainerBinding
import com.lee.foodi.ui.adapter.ContainerFragmentStateAdapter
import com.lee.foodi.ui.diary.DiaryFragment
import com.lee.foodi.ui.search.SearchFragment
import com.lee.foodi.ui.user.UserFragment

class ContainerActivity : AppCompatActivity() {
    private lateinit var binding : ActivityContainerBinding
    private lateinit var mSearchFragment : SearchFragment
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
    private fun initFragmentAdapter() {
        val tabNameArray = resources.getStringArray(R.array.bottom_tab_array)
        mSearchFragment = SearchFragment.newInstance()
        mDiaryFragment = DiaryFragment.newInstance()
        mUserFragment = UserFragment.newInstance()
        mFragmentStateAdapter.appendFragment(mSearchFragment)
        mFragmentStateAdapter.appendFragment(mDiaryFragment)
        mFragmentStateAdapter.appendFragment(mUserFragment)

        with(binding){
            containerViewPager.adapter = mFragmentStateAdapter
            TabLayoutMediator(bottomTabLayout , containerViewPager){tab , position ->
                tab.text = tabNameArray[position]
            }.attach()
        }
    }
}
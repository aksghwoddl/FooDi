package com.lee.foodi.ui.activities.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * ViewPager의 Fragment를 관리하는 Adapter class
 * **/
class ContainerFragmentStateAdapter(fragmentActivity : FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragments = mutableListOf<Fragment>()

    fun appendFragment(fragment: Fragment) {
        fragments.add(fragment)
    }

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return  fragments[position]
    }
}
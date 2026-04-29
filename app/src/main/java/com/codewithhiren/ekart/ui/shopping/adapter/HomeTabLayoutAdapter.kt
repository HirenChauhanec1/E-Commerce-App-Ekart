package com.codewithhiren.ekart.ui.shopping.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeTabLayoutAdapter(
    fragmentActivity: FragmentActivity,
    private val tabLayoutFragmentArray: Array<Fragment>
) : FragmentStateAdapter(fragmentActivity)
{
    override fun createFragment(position: Int): Fragment {
        return tabLayoutFragmentArray[position]
    }

    override fun getItemCount(): Int {
        return tabLayoutFragmentArray.size
    }
}
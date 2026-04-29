package com.codewithhiren.ekart.ui.shopping.fragment.bottomNavFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.codewithhiren.ekart.R
import com.codewithhiren.ekart.databinding.FragmentHomeBinding
import com.codewithhiren.ekart.ui.shopping.adapter.HomeTabLayoutAdapter
import com.codewithhiren.ekart.ui.shopping.fragment.tabLayoutFragment.ChairFragment
import com.codewithhiren.ekart.ui.shopping.fragment.tabLayoutFragment.CupboardFragment
import com.codewithhiren.ekart.ui.shopping.fragment.tabLayoutFragment.ElectronicsFragment
import com.codewithhiren.ekart.ui.shopping.fragment.tabLayoutFragment.FurnitureFragment
import com.codewithhiren.ekart.ui.shopping.fragment.tabLayoutFragment.MainCategoryFragment
import com.codewithhiren.ekart.ui.shopping.fragment.tabLayoutFragment.TableFragment
import com.codewithhiren.ekart.utils.showBottomNav
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val tabLayoutFragmentArray = arrayOf(
        MainCategoryFragment(),
        CupboardFragment(),
        ChairFragment(),
        TableFragment(),
        FurnitureFragment(),
        ElectronicsFragment()
    )
    private val tabNameArray = arrayOf("Main", "Cupboard", "Chair", "Table", "Furniture", "Electronics")

    val tabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let {
                if (binding.viewPager.currentItem != it.position) {
                    binding.viewPager.setCurrentItem(it.position, false)
                }
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {

        }

        override fun onTabReselected(tab: TabLayout.Tab?) {

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater)
        showBottomNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTabLayout()

    }

    private fun setTabLayout() {
        binding.apply {
            viewPager.adapter = HomeTabLayoutAdapter(requireActivity(), tabLayoutFragmentArray)
            viewPager.isUserInputEnabled = false

            TabLayoutMediator(tabLayout, viewPager) { tab: TabLayout.Tab, position: Int ->
                tab.text = tabNameArray[position]
            }.attach()

            tabLayout.addOnTabSelectedListener(tabSelectedListener)

            for (i in 0 until tabNameArray.size){
                val textView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.tab_layout_custom_tv,null) as TextView

                tabLayout.getTabAt(i)?.customView = textView
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.tabLayout.removeOnTabSelectedListener(tabSelectedListener)
        _binding = null
    }

}
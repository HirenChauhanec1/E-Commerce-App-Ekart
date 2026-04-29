package com.codewithhiren.ekart.ui.shopping.fragment.tabLayoutFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.codewithhiren.ekart.databinding.FragmentMainCategoryBinding
import com.codewithhiren.ekart.ui.shopping.adapter.BestDealsProductAdapter
import com.codewithhiren.ekart.ui.shopping.adapter.BestProductsAdapter
import com.codewithhiren.ekart.ui.shopping.adapter.SpecialProductAdapter
import com.codewithhiren.ekart.ui.shopping.viewmodel.MainCategoryViewmodel
import com.codewithhiren.ekart.utils.setRecyclerViewData
import com.codewithhiren.ekart.utils.showBottomNav
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainCategoryFragment : Fragment() {

    private var _binding: FragmentMainCategoryBinding? = null
    private val binding get() = _binding!!

    private val mainCategoryViewmodel: MainCategoryViewmodel by viewModels()

    @Inject
    lateinit var bestProductsAdapter: BestProductsAdapter

    @Inject
    lateinit var bestDealsProductAdapter: BestDealsProductAdapter

    @Inject
    lateinit var specialProductAdapter: SpecialProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainCategoryBinding.inflate(inflater)
        showBottomNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRvData()
    }

    private fun setRvData() {
        binding.apply {
            rvSpecialProducts.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
                setRecyclerViewData(
                    lifeCycleOwner = this@MainCategoryFragment.viewLifecycleOwner,
                    products = mainCategoryViewmodel.specialProducts,
                    adapter = specialProductAdapter,
                    progressBar = pbBestProducts
                )
            }

            rvBestDeal.apply {
                layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                setRecyclerViewData(
                    lifeCycleOwner = this@MainCategoryFragment.viewLifecycleOwner,
                    products = mainCategoryViewmodel.bestDealsProducts,
                    adapter = bestDealsProductAdapter,
                    progressBar = pbBestProducts
                )
            }
            rvBestProducts.apply {
                layoutManager = GridLayoutManager(requireContext(),2)
                setRecyclerViewData(
                    lifeCycleOwner = this@MainCategoryFragment.viewLifecycleOwner,
                    products = mainCategoryViewmodel.bestProducts,
                    adapter = bestProductsAdapter,
                    progressBar = pbBestProducts
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
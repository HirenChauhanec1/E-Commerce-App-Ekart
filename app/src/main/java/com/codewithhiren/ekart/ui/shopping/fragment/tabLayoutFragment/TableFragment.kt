package com.codewithhiren.ekart.ui.shopping.fragment.tabLayoutFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.codewithhiren.ekart.databinding.FragmentTableBinding
import com.codewithhiren.ekart.ui.shopping.adapter.BestProductsAdapter
import com.codewithhiren.ekart.ui.shopping.adapter.SpecialProductAdapter
import com.codewithhiren.ekart.ui.shopping.viewmodel.CategoryViewModel
import com.codewithhiren.ekart.utils.setRecyclerViewData
import com.codewithhiren.ekart.utils.showBottomNav
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TableFragment : Fragment() {

    private var _binding: FragmentTableBinding? = null
    private val binding get() = _binding!!

    private val categoryViewModel : CategoryViewModel by activityViewModels()

    @Inject
    lateinit var specialProductAdapter: SpecialProductAdapter
    @Inject
    lateinit var bestProductsAdapter: BestProductsAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        categoryViewModel.getTableProducts()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTableBinding.inflate(inflater)
        showBottomNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRvData()
    }

    private fun setRvData() {
        binding.apply {
            rvTableProducts.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
                setRecyclerViewData(
                    lifeCycleOwner = this@TableFragment.viewLifecycleOwner,
                    products = categoryViewModel.tableProducts,
                    adapter = specialProductAdapter,
                    progressBar = pb
                )
            }
            rvTableBestProducts.apply {
                layoutManager = GridLayoutManager(requireContext(),2)
                setRecyclerViewData(
                    lifeCycleOwner = this@TableFragment.viewLifecycleOwner,
                    products = categoryViewModel.bestDealsTableProducts,
                    adapter = bestProductsAdapter,
                    progressBar = pb
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
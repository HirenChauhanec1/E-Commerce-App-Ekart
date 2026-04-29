package com.codewithhiren.ekart.ui.shopping.fragment.bottomNavFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.codewithhiren.ekart.databinding.FragmentSearchBinding
import com.codewithhiren.ekart.ui.shopping.adapter.SpecialProductAdapter
import com.codewithhiren.ekart.ui.shopping.viewmodel.SearchViewmodel
import com.codewithhiren.ekart.utils.Category
import com.codewithhiren.ekart.utils.MainCategory
import com.codewithhiren.ekart.utils.setRecyclerViewData
import com.codewithhiren.ekart.utils.showBottomNav
import com.codewithhiren.ekart.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var selectedCategory: String? = null
    private val searchViewmodel: SearchViewmodel by viewModels()

    @Inject
    lateinit var specialProductAdapter: SpecialProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater)
        showBottomNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAutoCompleteView()
        setRvData()
        clickListeners()
    }

    private fun setAutoCompleteView() {
        binding.autoCompleteSearch.apply {
            threshold = 1
            setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    MainCategory.entries.toTypedArray()
                )
            )
            onItemClickListener = AdapterView.OnItemClickListener { parent, p1, position, p3 ->
                selectedCategory = parent?.getItemAtPosition(position).toString()
            }
        }
    }

    private fun setRvData() {
        binding.rvSearchedList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setRecyclerViewData(
                lifeCycleOwner = this@SearchFragment.viewLifecycleOwner,
                products = searchViewmodel.selectedCategoryProducts,
                adapter = specialProductAdapter,
                progressBar = binding.pb
            )
        }
    }

    private fun clickListeners() {
        binding.apply {
            btnSearch.setOnClickListener { searchProducts() }
            btnChair.setOnClickListener { getCategoryProducts(Category.Chair.category) }
            btnCupboard.setOnClickListener { getCategoryProducts(Category.Cupboard.category) }
            btnFurniture.setOnClickListener { getCategoryProducts(Category.Furniture.category) }
            btnElectronics.setOnClickListener { getCategoryProducts(Category.Electronics.category) }
        }
    }

    private fun searchProducts() {
        if (selectedCategory == null)
            showToast("Select category")
        else
            getCategoryProducts(selectedCategory!!)
    }

    private fun getCategoryProducts(category: String) {
        searchViewmodel.getCupboardProducts(category)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
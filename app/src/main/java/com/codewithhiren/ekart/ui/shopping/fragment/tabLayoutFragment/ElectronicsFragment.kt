package com.codewithhiren.ekart.ui.shopping.fragment.tabLayoutFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.codewithhiren.ekart.databinding.FragmentElectronicsBinding
import com.codewithhiren.ekart.ui.shopping.adapter.BestProductsAdapter
import com.codewithhiren.ekart.ui.shopping.adapter.SpecialProductAdapter
import com.codewithhiren.ekart.ui.shopping.viewmodel.ElectronicsViewmodel
import com.codewithhiren.ekart.utils.setRecyclerViewData
import com.codewithhiren.ekart.utils.showBottomNav
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ElectronicsFragment : Fragment() {

    private var _binding : FragmentElectronicsBinding ?= null
    private val binding get() = _binding!!

    private val electronicsViewmodel: ElectronicsViewmodel by viewModels()

    @Inject
    lateinit var specialProductAdapter : SpecialProductAdapter
    @Inject
    lateinit var bestProductsAdapter : BestProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentElectronicsBinding.inflate(inflater)
        showBottomNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setRvData()
    }

    private fun setRvData() {

        binding.apply {
            rvElectronicsProducts.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
                setRecyclerViewData(
                    lifeCycleOwner = this@ElectronicsFragment.viewLifecycleOwner,
                    products = electronicsViewmodel.electronicsProducts,
                    adapter = specialProductAdapter,
                    progressBar = pb
                )
            }
            rvElectronicsBestProducts.apply {
                layoutManager = GridLayoutManager(requireContext(),2)
                setRecyclerViewData(
                    lifeCycleOwner = this@ElectronicsFragment.viewLifecycleOwner,
                    products = electronicsViewmodel.bestDealsElectronicsProducts,
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
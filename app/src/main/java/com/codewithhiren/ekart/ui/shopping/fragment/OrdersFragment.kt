package com.codewithhiren.ekart.ui.shopping.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.codewithhiren.ekart.databinding.FragmentOrdersBinding
import com.codewithhiren.ekart.model.Order
import com.codewithhiren.ekart.ui.shopping.adapter.OrdersAdapter
import com.codewithhiren.ekart.ui.shopping.viewmodel.OrdersViewmodel
import com.codewithhiren.ekart.utils.hideBottomNav
import com.codewithhiren.ekart.utils.setRecyclerViewData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : Fragment() {

    private var _binding : FragmentOrdersBinding ?= null
    private val binding get() = _binding!!

    private val ordersViewmodel : OrdersViewmodel by viewModels()
    private val navController by lazy { findNavController() }


    private val clickListeners = object : OrdersAdapter.ClickListeners{
        override fun showOrderDetails(order: Order) {
            navController.navigate(
                OrdersFragmentDirections.actionOrdersFragmentToOrderDetailsFragment(order)
            )
        }

    }
    private val ordersAdapter by lazy{ OrdersAdapter(clickListeners) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hideBottomNav()
       _binding = FragmentOrdersBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRvData()
    }

    private fun setRvData() {
        binding.rvAllOrders.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setRecyclerViewData(
                lifeCycleOwner = this@OrdersFragment.viewLifecycleOwner,
                products = ordersViewmodel.userOrders,
                adapter = ordersAdapter,
                progressBar = binding.pb
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
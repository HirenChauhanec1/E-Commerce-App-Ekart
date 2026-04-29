package com.codewithhiren.ekart.ui.shopping.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.codewithhiren.ekart.R
import com.codewithhiren.ekart.databinding.FragmentOrderDetailsBinding
import com.codewithhiren.ekart.ui.shopping.adapter.OrderDetailsAdapter
import com.codewithhiren.ekart.utils.OrderStatus
import com.codewithhiren.ekart.utils.hideBottomNav
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailsFragment : Fragment() {

    private var _binding : FragmentOrderDetailsBinding ?= null
    private val binding get() = _binding!!

    private val args : OrderDetailsFragmentArgs by navArgs()
    private val orderDetailsAdapter  by lazy { OrderDetailsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hideBottomNav()
       _binding = FragmentOrderDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProductRv()
        setData()
        setOrderStatus()
    }

    private fun setProductRv() {
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = orderDetailsAdapter
            orderDetailsAdapter.submitList(args.order.orderedProductList)
        }
    }

    private fun setData() {
        binding.apply {
            args.order.apply {
                tvFullName.text = address.fullName
                tvAddress.text =  address.addressTitle
                tvPhone.text = address.phone
                tvTotalPrice.text = getString(R.string.product_price_rv,totalPrice.toString())
            }
        }
    }

    private fun setOrderStatus() {
        binding.apply {
            stepView.setSteps(listOf(
                OrderStatus.Ordered.name,
                OrderStatus.Confirmed.name,
                OrderStatus.Shipped.name,
                OrderStatus.Delivered.name
            ))

            val orderStatus = when(args.order.orderStatus){
                OrderStatus.Ordered.name -> 0
                OrderStatus.Confirmed.name -> 1
                OrderStatus.Shipped.name -> 2
                OrderStatus.Delivered.name -> 3
                else -> 0
            }
            stepView.go(orderStatus,false)
            if(orderStatus == 3)
                stepView.done(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
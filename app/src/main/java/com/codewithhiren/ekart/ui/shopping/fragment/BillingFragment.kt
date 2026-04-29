package com.codewithhiren.ekart.ui.shopping.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.codewithhiren.ekart.R
import com.codewithhiren.ekart.databinding.FragmentBillingBinding
import com.codewithhiren.ekart.model.Address
import com.codewithhiren.ekart.model.Order
import com.codewithhiren.ekart.ui.shopping.adapter.AddressAdapter
import com.codewithhiren.ekart.ui.shopping.adapter.OrderDetailsAdapter
import com.codewithhiren.ekart.ui.shopping.viewmodel.BillingViewmodel
import com.codewithhiren.ekart.utils.HelperClass
import com.codewithhiren.ekart.utils.NetworkResponse
import com.codewithhiren.ekart.utils.OrderStatus
import com.codewithhiren.ekart.utils.hideBottomNav
import com.codewithhiren.ekart.utils.showToast
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BillingFragment : Fragment() {

    private var _binding: FragmentBillingBinding? = null
    private val binding get() = _binding!!

    private var selectedAddress: Address? = null
    private var isAddressAdded = false
    private var isCartEmpty = true
    private var order = Order()

    private val billingViewmodel: BillingViewmodel by viewModels()
    private val navController by lazy { findNavController() }

    private val clickListeners = object : AddressAdapter.ClickListeners {
        override fun selectAddress(address: Address) {
            selectedAddress = address
            order.address = address
        }
    }
    private val addressAdapter by lazy { AddressAdapter(clickListeners) }

    private val orderDetailsAdapter by lazy { OrderDetailsAdapter() }

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hideBottomNav()
        _binding = FragmentBillingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRvData()
        setObservers()
        clickListeners()
    }

    private fun setRvData() {
        binding.apply {
            rvAddress.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
            }
            rvOrderedProducts.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }
    }

    private fun setObservers() {
        binding.apply {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    billingViewmodel.addressList.collect {
                        hideProgressbar()
                        when (it) {
                            is NetworkResponse.Success -> {

                                isAddressAdded = !it.data.isEmpty()

                                rvAddress.adapter = addressAdapter
                                addressAdapter.submitList(it.data)
                            }

                            is NetworkResponse.Error -> showToast(it.error)
                            is NetworkResponse.Loading -> showProgressbar()
                        }
                    }
                }
            }
            lifecycleScope.launch {
                billingViewmodel.cartProducts.collect {
                    hideProgressbar()
                    when (it) {
                        is NetworkResponse.Success -> {

                            isCartEmpty = if (it.data.isEmpty())
                                true
                            else {

                                rvOrderedProducts.adapter = orderDetailsAdapter
                                orderDetailsAdapter.submitList(it.data)

                                tvTotalPrice.text = getString(
                                    R.string.product_price_rv,
                                    HelperClass.calculateTotalPrice(it.data)
                                )

                                order = order.copy(
                                    email = auth.currentUser!!.email!!,
                                    orderStatus = OrderStatus.Ordered.name,
                                    totalPrice = HelperClass.calculateTotalPrice(it.data)
                                        .toFloat(),
                                    orderedProductList = it.data
                                )
                                false
                            }


                            mainConstraintLayout.isVisible = !isCartEmpty
                            emptyConstraintLayout.isVisible = isCartEmpty
                        }

                        is NetworkResponse.Error -> showToast(it.error)
                        is NetworkResponse.Loading -> showProgressbar()
                    }
                }
            }
        }
    }

    private fun clickListeners() {
        binding.apply {
            btnPlaceOrder.setOnClickListener { showDialogToPlaceOrder() }
            ivAddAddress.setOnClickListener {
                navController
                    .navigate(BillingFragmentDirections.actionBillingFragmentToAddressFragment())
            }
        }
    }

    private fun showDialogToPlaceOrder() {
        binding.apply {
            when {
                !isAddressAdded -> showToast("Please add Address")
                selectedAddress == null -> showToast("Select Address")
                isCartEmpty -> showToast("Your cart is empty")
                else -> {
                    AlertDialog.Builder(requireContext()).apply {
                        setTitle("Place Order ?")
                        setMessage("Are you want to place order ?")
                        setIcon(R.drawable.baseline_shopping_cart_24)
                        setPositiveButton("Place") { dialog, which ->
                            placeOrder()
                            dialog.dismiss()
                        }
                        setNegativeButton("No") { dialog, which ->
                            dialog.dismiss()
                        }
                        show()
                    }
                }
            }
        }
    }

    private fun placeOrder() {
        lifecycleScope.launch {
            billingViewmodel.placeOrder(order).collect {
                hideProgressbar()
                when (it) {
                    is NetworkResponse.Success -> {
                        showToast(it.data)
                        navController.popBackStack(R.id.homeFragment, false)
                    }

                    is NetworkResponse.Error -> showToast(it.error)
                    is NetworkResponse.Loading -> showProgressbar()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun showProgressbar() {
        binding.pb.isVisible = true
    }
    private fun hideProgressbar() {
        binding.pb.isVisible = false
    }
}
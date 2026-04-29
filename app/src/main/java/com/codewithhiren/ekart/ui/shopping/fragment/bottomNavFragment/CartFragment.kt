package com.codewithhiren.ekart.ui.shopping.fragment.bottomNavFragment

import android.app.Dialog
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
import com.codewithhiren.ekart.ShoppingNavGraphDirections
import com.codewithhiren.ekart.databinding.ChangeQuantityBinding
import com.codewithhiren.ekart.databinding.FragmentCartBinding
import com.codewithhiren.ekart.model.CartProduct
import com.codewithhiren.ekart.ui.shopping.adapter.CartProductAdapter
import com.codewithhiren.ekart.ui.shopping.viewmodel.CartViewmodel
import com.codewithhiren.ekart.utils.HelperClass
import com.codewithhiren.ekart.utils.NetworkResponse
import com.codewithhiren.ekart.utils.hide
import com.codewithhiren.ekart.utils.show
import com.codewithhiren.ekart.utils.showBottomNav
import com.codewithhiren.ekart.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val cartViewmodel: CartViewmodel by viewModels()

    private val clickListeners = object : CartProductAdapter.ClickListeners {
        override fun showDialogToChangeQuantity(cartProduct: CartProduct) {
            this@CartFragment.showDialogToChangeQuantity(cartProduct)
        }
    }
    private val cartProductAdapter by lazy { CartProductAdapter(clickListeners) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater)
        showBottomNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCartRv()
        clickListeners()
        setObservers()
    }

    private fun setCartRv() {
        binding.rvCartProduct.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun setObservers() {
        binding.apply {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    cartViewmodel.cartProducts.collect {
                        pb.hide()
                        when (it) {
                            is NetworkResponse.Success -> {

                                cardEmptyLayout.isVisible = it.data.isEmpty()
                                mainLayout.isVisible = !it.data.isEmpty()

                                rvCartProduct.adapter = cartProductAdapter
                                cartProductAdapter.submitList(it.data)

                                tvTotalPrice.text = getString(R.string.product_price_rv,HelperClass.calculateTotalPrice(it.data))

                            }

                            is NetworkResponse.Error -> showToast(it.error)
                            is NetworkResponse.Loading -> pb.show()
                        }
                    }
                }
            }
        }

    }

    private fun clickListeners() {
        binding.btnCheckOut.setOnClickListener {
            findNavController().navigate(ShoppingNavGraphDirections.actionGlobalBillingFragment())
        }
    }

    private fun showDialogToChangeQuantity(cartProduct: CartProduct) {
        val dialogBinding = ChangeQuantityBinding.inflate(LayoutInflater.from(context), null, false)
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogBinding.root)
        dialog.show()

        dialogBinding.apply {
            btnChange.setOnClickListener {
                val quantity = etQuantity.text.toString().trim()
                if (quantity.isEmpty())
                    showToast("Enter quantity")
                else {
                    changeQuantityToCartProduct(quantity.toInt(), cartProduct)
                    dialog.dismiss()
                }
            }
            btnCancel.setOnClickListener { dialog.dismiss() }
        }

    }

    private fun changeQuantityToCartProduct(quantity: Int, cartProduct: CartProduct) {
        binding.apply {
            lifecycleScope.launch {
                cartViewmodel.changeQuantityOfCartProduct(cartProduct.copy(quantity = quantity))
                    .collect {
                        linearProgressIndicator.isVisible = false
                        when (it) {
                            is NetworkResponse.Success -> {}
                            is NetworkResponse.Error -> showToast(it.error)
                            is NetworkResponse.Loading -> linearProgressIndicator.isVisible = true
                        }
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
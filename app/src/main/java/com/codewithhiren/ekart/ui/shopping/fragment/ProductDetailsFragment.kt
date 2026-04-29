package com.codewithhiren.ekart.ui.shopping.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codewithhiren.ekart.R
import com.codewithhiren.ekart.databinding.FragmentProductDetailsBinding
import com.codewithhiren.ekart.model.CartProduct
import com.codewithhiren.ekart.ui.shopping.adapter.ColorAdapter
import com.codewithhiren.ekart.ui.shopping.adapter.ImageAdapter
import com.codewithhiren.ekart.ui.shopping.adapter.SizeAdapter
import com.codewithhiren.ekart.ui.shopping.viewmodel.ProductDetailsViewmodel
import com.codewithhiren.ekart.utils.NetworkResponse
import com.codewithhiren.ekart.utils.hide
import com.codewithhiren.ekart.utils.hideBottomNav
import com.codewithhiren.ekart.utils.show
import com.codewithhiren.ekart.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    var selectedColor: Int? = null
    var selectedSize: String? = null

    private val imageAdapter by lazy { ImageAdapter() }

    private val clickListenersColor = object : ColorAdapter.ClickListeners {
        override fun selectColour(color: Int) {
            selectedColor = color
        }
    }
    private val colorAdapter by lazy { ColorAdapter(clickListenersColor) }

    private val clickListenersSize = object : SizeAdapter.ClickListeners {
        override fun selectSize(size: String) {
            selectedSize = size
        }
    }
    private val sizeAdapter by lazy { SizeAdapter(clickListenersSize) }

    private val args: ProductDetailsFragmentArgs by navArgs()
    private val navController by lazy { findNavController() }
    private val productDetailsViewmodel: ProductDetailsViewmodel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailsBinding.inflate(inflater)
        hideBottomNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData()
        setRvData()
        clickListeners()
    }

    private fun setData() {
        binding.apply {
            args.product.apply {
                val price = if (offerPercentage == null) price else (price / 100) * (100 - offerPercentage)
                tvProductName.text = name
                tvProductPrice.text = getString(R.string.product_price_rv, price.toString())
                tvProductDescription.text = description
            }
        }
    }

    private fun setRvData() {
        binding.apply {
            args.product.apply {
                viewpagerProductImages.apply {
                    adapter = imageAdapter
                    imageAdapter.submitList(images)
                }
                rvColors.setRvLayoutManager(colorAdapter,colors)
                rvSizes.setRvLayoutManager(sizeAdapter,sizes)
            }
        }
    }

    fun <T>RecyclerView.setRvLayoutManager(adapter: ListAdapter<T,*>,list: List<T>?) {
        this.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            this.adapter = adapter
            adapter.submitList(list)
        }
    }

    private fun clickListeners() {
        binding.apply {
            ivClose.setOnClickListener { navController.navigateUp() }
            btnAddToCart.setOnClickListener { addProductToCart() }
        }
    }

    private fun addProductToCart() {
        when {
            selectedColor == null -> showToast("Select Color")
            selectedSize == null -> showToast("Select Size")
            else -> {
                lifecycleScope.launch {
                    productDetailsViewmodel.addProductToCart(
                        CartProduct(args.product, selectedColor!!, selectedSize!!, 1)
                    )
                        .collect {
                            binding.pb.hide()
                            when (it) {
                                is NetworkResponse.Success -> {
                                    showToast(it.data)
                                    navController.navigateUp()
                                }

                                is NetworkResponse.Error -> showToast(it.error)
                                is NetworkResponse.Loading -> binding.pb.show()
                            }
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
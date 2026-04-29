package com.codewithhiren.ekart.ui.shopping.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codewithhiren.ekart.R
import com.codewithhiren.ekart.databinding.BestDealsRvItemBinding
import com.codewithhiren.ekart.model.Product
import com.squareup.picasso.Picasso
import javax.inject.Inject

class BestDealsProductAdapter @Inject constructor(private val clickListeners: ClickListeners) :
    ListAdapter<Product, BestDealsProductAdapter.ViewHolder>(Product.productDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(BestDealsRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setRvData(getItem(position))
    }

    inner class ViewHolder(private val binding: BestDealsRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setRvData(currentItem: Product) {
            val context = binding.root.context
            binding.apply {
                currentItem.apply {

                    val discountPrice = if (offerPercentage == null) {
                        tvProductPrice.isVisible = false
                        price
                    } else {
                        tvProductPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                        (price / 100) * (100 - offerPercentage)
                    }

                    Picasso.get().load(images[0]).into(ivProductImg)
                    tvProductName.text = name
                    tvProductDiscountPrice.text = context.getString(R.string.product_price_rv, discountPrice.toString())
                    tvProductPrice.text = context.getString(R.string.product_price_rv, price.toString())
                }
                btnSeeProduct.setOnClickListener { clickListeners.showProduct(currentItem) }
            }
        }
    }
}
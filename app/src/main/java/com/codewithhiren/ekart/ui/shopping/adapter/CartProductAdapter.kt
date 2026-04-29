package com.codewithhiren.ekart.ui.shopping.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codewithhiren.ekart.R
import com.codewithhiren.ekart.databinding.CartProductRvItemBinding
import com.codewithhiren.ekart.model.CartProduct
import com.squareup.picasso.Picasso

class CartProductAdapter (private val clickListeners: ClickListeners) :
    ListAdapter<CartProduct, CartProductAdapter.ViewHolder>(CartProduct.cartProductDiffUtil) {


    interface ClickListeners {
        fun showDialogToChangeQuantity(cartProduct: CartProduct)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CartProductRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(getItem(position))
    }

   inner class ViewHolder(private val binding: CartProductRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(cartProduct: CartProduct) {
            binding.apply {
                cartProduct.product.apply {
                    val price = if (offerPercentage == null) price else (price / 100) * (100 - offerPercentage)
                    Picasso.get().load(images[0]).into(ivProductImg)
                    tvProductName.text = name
                    tvProductPrice.text = root.context.getString(R.string.product_price_rv, price.toString())
                    layoutSelectedSize.sizeArea.text = cartProduct.selectedSize
                    layoutSelectedColor.colorArea.setBackgroundColor(cartProduct.selectedColor)
                    tvQuantity.text = cartProduct.quantity.toString()

                    ivIncreaseQuantity.setOnClickListener { clickListeners.showDialogToChangeQuantity(cartProduct) }
                    ivDecreaseQuantity.setOnClickListener { clickListeners.showDialogToChangeQuantity(cartProduct) }
                }
            }
        }
    }
}


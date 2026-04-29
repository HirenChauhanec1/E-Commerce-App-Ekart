package com.codewithhiren.ekart.ui.shopping.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codewithhiren.ekart.R
import com.codewithhiren.ekart.databinding.OrdersDetailsRvItemBinding
import com.codewithhiren.ekart.model.CartProduct
import com.squareup.picasso.Picasso


class OrderDetailsAdapter : ListAdapter<CartProduct, OrderDetailsAdapter.ViewHolder>(CartProduct.cartProductDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(OrdersDetailsRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setRvData(getItem(position))
    }

    class ViewHolder(private val binding: OrdersDetailsRvItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setRvData(cartProduct: CartProduct) {
            binding.apply {
                val context = root.context
                cartProduct.product.apply {
                    val price = if(offerPercentage == null) price else ((price/100)*(100-offerPercentage))
                    Picasso.get().load(images[0]).into(ivProductImg)
                    tvProductName.text = name
                    tvProductPrice.text = context.getString(R.string.product_price_rv,price.toString())
                    tvProductQuantity.text = cartProduct.quantity.toString()
                    layoutSelectedColor.colorArea.setBackgroundColor(cartProduct.selectedColor)
                    layoutSelectedSize.sizeArea.text = cartProduct.selectedSize
                }
            }
        }
    }
}


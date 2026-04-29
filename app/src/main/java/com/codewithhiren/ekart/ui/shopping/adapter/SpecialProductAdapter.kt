package com.codewithhiren.ekart.ui.shopping.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codewithhiren.ekart.R
import com.codewithhiren.ekart.databinding.SpecialProductsRvItemBinding
import com.codewithhiren.ekart.model.Product
import com.squareup.picasso.Picasso
import javax.inject.Inject

class SpecialProductAdapter @Inject constructor(
    private val clickListeners: ClickListeners
) : ListAdapter<Product, SpecialProductAdapter.ViewHolder>(Product.productDiffUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SpecialProductsRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setRvData(getItem(position))
    }

    inner class ViewHolder(private val binding: SpecialProductsRvItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun setRvData(currentItem: Product){
            binding.apply {
                currentItem.apply {
                    Picasso.get().load(images[0]).into(ivProductImg)
                    tvProductName.text = name
                    tvProductPrice.text =  root.context.getString(R.string.product_price_rv, price.toString())
                }
                    tvAddToCart.setOnClickListener { clickListeners.showProduct(currentItem) }
            }
        }
    }
}

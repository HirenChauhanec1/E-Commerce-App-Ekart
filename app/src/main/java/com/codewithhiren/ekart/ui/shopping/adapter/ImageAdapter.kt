package com.codewithhiren.ekart.ui.shopping.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codewithhiren.ekart.databinding.ProductImagesRvItemBinding
import com.squareup.picasso.Picasso

class ImageAdapter : ListAdapter<String, ImageAdapter.ViewHolder>(stringDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ProductImagesRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setRvData(getItem(position))
    }

    inner class ViewHolder(private val binding: ProductImagesRvItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setRvData(currentItem: String) {
            Picasso.get().load(currentItem).into(binding.ivProductImg)
        }
    }
}
val stringDiffUtil = object : DiffUtil.ItemCallback<String>(){
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}

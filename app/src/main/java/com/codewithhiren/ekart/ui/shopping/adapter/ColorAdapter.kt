package com.codewithhiren.ekart.ui.shopping.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codewithhiren.ekart.databinding.ColorsRvItemBinding


class ColorAdapter(private val clickListeners: ClickListeners) :
    ListAdapter<Int, ColorAdapter.ViewHolder>(intDIffUtil) {

    var selectedPosition: Int? = null
    var previousSelectedPosition: Int? = null

    interface ClickListeners {
        fun selectColour(color: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ColorsRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setRvData(getItem(position), position)
    }

    inner class ViewHolder(private val binding: ColorsRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setRvData(currentItem: Int, position: Int) {
            binding.apply {

                selectionArea.isVisible = selectedPosition == position
                colorArea.setBackgroundColor(currentItem)

                main.setOnClickListener {
                    previousSelectedPosition = selectedPosition
                    selectedPosition = position
                    previousSelectedPosition?.let { notifyItemChanged(it) }
                    selectedPosition?.let { notifyItemChanged(it) }
                    clickListeners.selectColour(currentItem)
                }
            }
        }
    }
}

val intDIffUtil = object : DiffUtil.ItemCallback<Int>() {
    override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
        return oldItem == newItem
    }

}
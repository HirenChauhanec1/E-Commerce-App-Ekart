package com.codewithhiren.ekart.ui.shopping.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codewithhiren.ekart.databinding.SizesRvItemBinding


class SizeAdapter(
    private val clickListeners: ClickListeners
) : ListAdapter<String, SizeAdapter.ViewHolder>(stringDiffUtil) {


    var selectedPosition : Int ?= null
    var previousSelectedPosition : Int ?= null

    interface ClickListeners {
        fun selectSize(size: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SizesRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setRvData(getItem(position),position)
    }

    inner class ViewHolder(private val binding: SizesRvItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setRvData(currentItem: String, position: Int) {
            binding.apply {
                selectionArea.isVisible = selectedPosition == position
                sizeArea.text = currentItem

                main.setOnClickListener {

                    previousSelectedPosition = selectedPosition
                    selectedPosition = position
                    previousSelectedPosition?.let { notifyItemChanged(it) }
                    selectedPosition?.let { notifyItemChanged(it) }
                    clickListeners.selectSize(currentItem)
                }
            }
        }
    }
}
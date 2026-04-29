package com.codewithhiren.ekart.ui.shopping.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codewithhiren.ekart.R
import com.codewithhiren.ekart.databinding.AddressRvItemBinding
import com.codewithhiren.ekart.model.Address


class AddressAdapter(private val clickListeners: ClickListeners) :
    ListAdapter<Address, AddressAdapter.ViewHolder>(Address.addressDiffUtil) {

    var selectedPosition: Int? = null

    interface ClickListeners {
        fun selectAddress(address: Address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AddressRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setRvData(getItem(position), position)
    }

    inner class ViewHolder(private val binding: AddressRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setRvData(address: Address, position: Int) {

            binding.btnAddress.apply {
                text = address.addressTitle
                if (selectedPosition == position)
                    setBackgroundColor(binding.root.context.getColor(R.color.g_gray200))
                else
                    setBackgroundColor(binding.root.context.getColor(R.color.white))

                setOnClickListener {
                    val previousSelectedPosition = selectedPosition
                    selectedPosition = position
                    previousSelectedPosition?.let { notifyItemChanged(it) }
                    selectedPosition?.let { notifyItemChanged(it) }
                    clickListeners.selectAddress(address)
                }
            }
        }
    }
}

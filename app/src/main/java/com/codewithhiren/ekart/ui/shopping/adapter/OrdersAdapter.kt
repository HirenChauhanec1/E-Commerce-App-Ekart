package com.codewithhiren.ekart.ui.shopping.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codewithhiren.ekart.R
import com.codewithhiren.ekart.databinding.OrderRvItemBinding
import com.codewithhiren.ekart.model.Order
import com.codewithhiren.ekart.utils.OrderStatus


class OrdersAdapter(private val clickListeners: ClickListeners) :
    ListAdapter<Order, OrdersAdapter.ViewHolder>(Order.orderDiffUtil) {


    interface ClickListeners{
        fun showOrderDetails(order: Order)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(OrderRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setRvData(getItem(position))
    }

    inner class ViewHolder(private val binding: OrderRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setRvData(order: Order) {
            binding.apply {
                order.apply {
                    tvOrderId.text = orderId.toString()
                    tvOrderDate.text = date

                    val colorForStatus = when(orderStatus){
                        OrderStatus.Ordered.name -> R.color.g_orange_yellow
                        OrderStatus.Confirmed.name -> R.color.g_green
                        OrderStatus.Canceled.name -> R.color.g_red
                        else -> R.color.g_black
                    }
                    ivStatus.setBackgroundColor(root.context.getColor(colorForStatus))
                    mainLL.setOnClickListener { clickListeners.showOrderDetails(order) }
                }
            }
        }
    }
}



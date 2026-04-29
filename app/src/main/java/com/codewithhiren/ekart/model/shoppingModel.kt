package com.codewithhiren.ekart.model

import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random.Default.nextLong


data class Product(
    val id : String,
    val name : String,
    val category: List<String>,
    val price: Float,
    val offerPercentage: Float? = null,
    val description : String ?= null,
    val colors : List<Int> ?= null,
    val sizes : List<String>?=null,
    val images : List<String>
) : Serializable{
    constructor() : this(id = "0",name = "",category = emptyList(),price = 0f, images = emptyList())
    companion object{
        val productDiffUtil = object : DiffUtil.ItemCallback<Product>(){
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }

        }
    }
}

data class CartProduct(
    val product: Product,
    val selectedColor : Int,
    val selectedSize : String,
    val quantity : Int
) : Serializable {
    constructor() : this(Product(),0,"",0)
    companion object{
        val cartProductDiffUtil = object : DiffUtil.ItemCallback<CartProduct>() {
            override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
                return oldItem.product.id == newItem.product.id
            }

            override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
                return oldItem == newItem
            }

        }
    }
}


data class  Address(
    val addressTitle : String,
    val fullName : String,
    val street : String,
    val phone : String,
    val city : String,
    val state : String
): Serializable {
    constructor():this("","","","","","")
    companion object{
        val addressDiffUtil = object : DiffUtil.ItemCallback<Address>() {
            override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
                return oldItem.addressTitle == newItem.addressTitle
            }

            override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
                return oldItem == newItem
            }
        }
    }
}

data class Order(
    val email : String = "",
    val orderStatus :String = "",
    val totalPrice : Float = 0f,
    val orderedProductList : List<CartProduct> = emptyList(),
    var address: Address = Address(),
    val date : String = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(Date()),
    val orderId : Long = nextLong(0, 100_000_000_000) + totalPrice.toLong()
) : Serializable{
    companion object{
        val orderDiffUtil = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
                return oldItem.orderId == newItem.orderId
            }

            override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
                return oldItem == newItem
            }
        }
    }
}
package com.codewithhiren.ekart.ui.shopping.viewmodel

import androidx.lifecycle.ViewModel
import com.codewithhiren.ekart.model.CartProduct
import com.codewithhiren.ekart.repo.ShoppingRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ProductDetailsViewmodel @Inject constructor(private val shoppingRepo : ShoppingRepo) : ViewModel(){

    fun addProductToCart(cartProduct: CartProduct) = shoppingRepo.addProductToCart(cartProduct)

}
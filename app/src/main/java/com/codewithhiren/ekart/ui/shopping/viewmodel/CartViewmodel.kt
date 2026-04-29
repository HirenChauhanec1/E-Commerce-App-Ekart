package com.codewithhiren.ekart.ui.shopping.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithhiren.ekart.model.CartProduct
import com.codewithhiren.ekart.repo.ShoppingRepo
import com.codewithhiren.ekart.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CartViewmodel @Inject constructor(private val shoppingRepo: ShoppingRepo) : ViewModel() {

    var cartProducts = MutableStateFlow<NetworkResponse<List<CartProduct>>>(NetworkResponse.Loading())
        private set


    init {
        getCupboardProducts()
    }


    fun getCupboardProducts(){
        viewModelScope.launch {
            shoppingRepo.getCartProducts().collect {
                cartProducts.emit(it)
            }
        }
    }

    fun changeQuantityOfCartProduct(cartProduct: CartProduct) = shoppingRepo.changeQuantityOfCartProduct(cartProduct)
}
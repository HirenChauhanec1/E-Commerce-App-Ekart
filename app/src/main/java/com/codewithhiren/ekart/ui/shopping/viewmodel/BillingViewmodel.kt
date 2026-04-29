package com.codewithhiren.ekart.ui.shopping.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithhiren.ekart.di.DispatcherIO
import com.codewithhiren.ekart.model.Address
import com.codewithhiren.ekart.model.CartProduct
import com.codewithhiren.ekart.model.Order
import com.codewithhiren.ekart.repo.ShoppingRepo
import com.codewithhiren.ekart.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BillingViewmodel @Inject constructor(
    private val shoppingRepo: ShoppingRepo,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher
) : ViewModel() {

    var addressList = MutableStateFlow<NetworkResponse<List<Address>>>(NetworkResponse.Loading())
    private set
    var cartProducts = MutableStateFlow<NetworkResponse<List<CartProduct>>>(NetworkResponse.Loading())
    private set

    init {
        viewModelScope.launch{
            shoppingRepo.getUserAddresses().collect {
                addressList.emit(it)
            }
        }
        viewModelScope.launch {
            shoppingRepo.getCartProducts().collect {
                cartProducts.emit(it)
            }
        }
    }

    fun placeOrder(order: Order) = shoppingRepo.placeOrder(order)
}
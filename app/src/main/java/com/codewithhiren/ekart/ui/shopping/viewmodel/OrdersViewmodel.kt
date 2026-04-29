package com.codewithhiren.ekart.ui.shopping.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithhiren.ekart.di.DispatcherIO
import com.codewithhiren.ekart.model.Order
import com.codewithhiren.ekart.repo.ShoppingRepo
import com.codewithhiren.ekart.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrdersViewmodel @Inject constructor(
    private val shoppingRepo: ShoppingRepo,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher
) : ViewModel() {

    var userOrders = MutableStateFlow<NetworkResponse<List<Order>>>(NetworkResponse.Loading())
        private set


    init {
        viewModelScope.launch (dispatcherIO){
            shoppingRepo.getUserOrders().collect {
                userOrders.emit(it)
            }
        }
    }
}
package com.codewithhiren.ekart.ui.shopping.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithhiren.ekart.di.DispatcherIO
import com.codewithhiren.ekart.model.Product
import com.codewithhiren.ekart.repo.ShoppingRepo
import com.codewithhiren.ekart.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewmodel @Inject constructor(
    private val shoppingRepo: ShoppingRepo
) : ViewModel() {

    var selectedCategoryProducts = MutableStateFlow<NetworkResponse<List<Product>>>(NetworkResponse.Success(emptyList()))
        private set


    fun getCupboardProducts(category: String){
        viewModelScope.launch {
            shoppingRepo.getParticularCategoryProducts(category).collect {
                selectedCategoryProducts.emit(it)
            }
        }
    }
}

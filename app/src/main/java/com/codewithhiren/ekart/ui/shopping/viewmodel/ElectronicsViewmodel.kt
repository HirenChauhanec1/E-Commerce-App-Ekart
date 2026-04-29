package com.codewithhiren.ekart.ui.shopping.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithhiren.ekart.model.Product
import com.codewithhiren.ekart.repo.ShoppingRepo
import com.codewithhiren.ekart.utils.Category
import com.codewithhiren.ekart.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ElectronicsViewmodel @Inject constructor(private val shoppingRepo: ShoppingRepo) : ViewModel() {

    var electronicsProducts = MutableStateFlow<NetworkResponse<List<Product>>>(NetworkResponse.Loading())
        private set
    var bestDealsElectronicsProducts = MutableStateFlow<NetworkResponse<List<Product>>>(NetworkResponse.Loading())
        private set
    init {
        getCupboardProducts()
    }
    fun getCupboardProducts(){
        viewModelScope.launch {
            shoppingRepo.getParticularCategoryProducts(Category.Electronics.category).collect {
                electronicsProducts.emit(it)
            }
        }
        viewModelScope.launch {
            shoppingRepo.getParticularCategoryProducts(Category.Electronics.category).collect {
                if (it is NetworkResponse.Success) {
                    val electronicsBestDealsProducts = it.data.filter {
                        it.category.contains(Category.BestDeals.category)
                    }
                    bestDealsElectronicsProducts.emit(NetworkResponse.Success(electronicsBestDealsProducts))
                    return@collect
                }
                bestDealsElectronicsProducts.emit(it)
            }
        }
    }



}

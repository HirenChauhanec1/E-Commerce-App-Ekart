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
class FurnitureViewmodel @Inject constructor(private val shoppingRepo: ShoppingRepo) : ViewModel() {

    var furnitureProducts = MutableStateFlow<NetworkResponse<List<Product>>>(NetworkResponse.Loading())
        private set
    var bestDealsFurnitureProducts = MutableStateFlow<NetworkResponse<List<Product>>>(NetworkResponse.Loading())
        private set
    init {
        getCupboardProducts()
    }
    fun getCupboardProducts(){
        viewModelScope.launch {
            shoppingRepo.getParticularCategoryProducts(Category.Furniture.category).collect {
                furnitureProducts.emit(it)
            }
        }
        viewModelScope.launch {
            shoppingRepo.getParticularCategoryProducts(Category.Furniture.category).collect {
                if (it is NetworkResponse.Success) {
                    val furnitureBestDealsProducts = it.data.filter {
                        it.category.contains(Category.BestDeals.category)
                    }
                    bestDealsFurnitureProducts.emit(NetworkResponse.Success(furnitureBestDealsProducts))
                    return@collect
                }
                bestDealsFurnitureProducts.emit(it)
            }
        }
    }



}

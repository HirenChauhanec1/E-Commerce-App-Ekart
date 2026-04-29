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
class MainCategoryViewmodel @Inject constructor(private val shoppingRepo: ShoppingRepo) : ViewModel(){

    var specialProducts = MutableStateFlow<NetworkResponse<List<Product>>>(NetworkResponse.Loading())
    private set
    var bestDealsProducts = MutableStateFlow<NetworkResponse<List<Product>>>(NetworkResponse.Loading())
    private set
    var bestProducts = MutableStateFlow<NetworkResponse<List<Product>>>(NetworkResponse.Loading())
    private set

    init {
        getMainCategoryProducts()
    }

    fun getMainCategoryProducts(){
        viewModelScope.launch {
            shoppingRepo.getParticularCategoryProducts(Category.SpecialProducts.category).collect {
                specialProducts.emit(it)
            }
        }
        viewModelScope.launch {
            shoppingRepo.getParticularCategoryProducts(Category.BestDeals.category).collect {
                bestDealsProducts.emit(it)
            }
        }
        viewModelScope.launch {
            shoppingRepo.getAllProducts().collect {
                bestProducts.emit(it)
            }
        }
    }

}
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
class CupboardViewmodel @Inject constructor(private val shoppingRepo: ShoppingRepo) : ViewModel() {

    var cupboardProducts = MutableStateFlow<NetworkResponse<List<Product>>>(NetworkResponse.Loading())
        private set
    var bestDealsCupboardProducts = MutableStateFlow<NetworkResponse<List<Product>>>(NetworkResponse.Loading())
        private set
    init {
        getCupboardProducts()
    }
    fun getCupboardProducts(){
        viewModelScope.launch {
            shoppingRepo.getParticularCategoryProducts(Category.Cupboard.category).collect {
                cupboardProducts.emit(it)
            }
        }
        viewModelScope.launch {
            shoppingRepo.getParticularCategoryProducts(Category.Cupboard.category).collect {
                if (it is NetworkResponse.Success) {
                    val cupboardBestDealsProducts = it.data.filter {
                        it.category.contains(Category.BestDeals.category)
                    }
                    bestDealsCupboardProducts.emit(NetworkResponse.Success(cupboardBestDealsProducts))
                    return@collect
                }
                bestDealsCupboardProducts.emit(it)
            }
        }
    }



}

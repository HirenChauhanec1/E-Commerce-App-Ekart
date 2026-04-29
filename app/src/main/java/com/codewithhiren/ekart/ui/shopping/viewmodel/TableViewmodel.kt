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
class TableViewmodel @Inject constructor(private val shoppingRepo: ShoppingRepo) : ViewModel() {

    var tableProducts = MutableStateFlow<NetworkResponse<List<Product>>>(NetworkResponse.Loading())
        private set
    var bestDealsTableProducts = MutableStateFlow<NetworkResponse<List<Product>>>(NetworkResponse.Loading())
        private set
    init {
        getCupboardProducts()
    }
    fun getCupboardProducts(){
        viewModelScope.launch {
            shoppingRepo.getParticularCategoryProducts(Category.Table.category).collect {
                tableProducts.emit(it)
            }
        }
        viewModelScope.launch {
            shoppingRepo.getParticularCategoryProducts(Category.Table.category).collect {
                if (it is NetworkResponse.Success) {
                    val tableProductsBestDealsProducts = it.data.filter {
                        it.category.contains(Category.BestDeals.category)
                    }
                    bestDealsTableProducts.emit(NetworkResponse.Success(tableProductsBestDealsProducts))
                    return@collect
                }
                bestDealsTableProducts.emit(it)
            }
        }
    }



}

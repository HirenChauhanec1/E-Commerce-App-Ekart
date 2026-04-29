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
class ChairViewmodel @Inject constructor(private val shoppingRepo: ShoppingRepo) : ViewModel() {

    var chairProducts = MutableStateFlow<NetworkResponse<List<Product>>>(NetworkResponse.Loading())
        private set
    var bestDealsChairProducts = MutableStateFlow<NetworkResponse<List<Product>>>(NetworkResponse.Loading())
        private set


    init {
        getChairProducts()
    }

    fun getChairProducts(){
        viewModelScope.launch {
            shoppingRepo.getParticularCategoryProducts(Category.Chair.category).collect {
                chairProducts.emit(it)
            }
        }
        viewModelScope.launch {
            shoppingRepo.getParticularCategoryProducts(Category.Chair.category).collect {
                if (it is NetworkResponse.Success) {
                    val chairBestDealsProducts = it.data.filter {
                        it.category.contains(Category.BestDeals.category)
                    }
                    bestDealsChairProducts.emit(NetworkResponse.Success(chairBestDealsProducts))
                    return@collect
                }
                bestDealsChairProducts.emit(it)
            }
        }
    }
}

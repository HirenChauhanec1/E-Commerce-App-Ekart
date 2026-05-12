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
class CategoryViewModel @Inject constructor(private val shoppingRepo: ShoppingRepo) : ViewModel() {

    var chairProducts = MutableStateFlow<NetworkResponse<List<Product>>>(NetworkResponse.Loading())
        private set
    var bestDealsChairProducts = MutableStateFlow<NetworkResponse<List<Product>>>(NetworkResponse.Loading())
        private set

    fun getChairProducts(){
        viewModelScope.launch {
            shoppingRepo.getParticularCategoryProducts(Category.Chair.category).collect {
                chairProducts.emit(it)
            }
        }
        viewModelScope.launch {
            shoppingRepo.getParticularCategoryProducts(Category.Chair.category).collect {
                if (it is NetworkResponse.Success) {
                    val chairBestDealsProducts = it.data.filter { product ->
                        product.category.contains(Category.BestDeals.category)
                    }
                    bestDealsChairProducts.emit(NetworkResponse.Success(chairBestDealsProducts))
                    return@collect
                }
                bestDealsChairProducts.emit(it)
            }
        }
    }

    var cupboardProducts = MutableStateFlow<NetworkResponse<List<Product>>>(NetworkResponse.Loading())
        private set
    var bestDealsCupboardProducts = MutableStateFlow<NetworkResponse<List<Product>>>(NetworkResponse.Loading())
        private set

    fun getCupboardProducts(){
        viewModelScope.launch {
            shoppingRepo.getParticularCategoryProducts(Category.Cupboard.category).collect {
                cupboardProducts.emit(it)
            }
        }
        viewModelScope.launch {
            shoppingRepo.getParticularCategoryProducts(Category.Cupboard.category).collect {
                if (it is NetworkResponse.Success) {
                    val cupboardBestDealsProducts = it.data.filter { product ->
                        product.category.contains(Category.BestDeals.category)
                    }
                    bestDealsCupboardProducts.emit(NetworkResponse.Success(cupboardBestDealsProducts))
                    return@collect
                }
                bestDealsCupboardProducts.emit(it)
            }
        }
    }

    var electronicsProducts = MutableStateFlow<NetworkResponse<List<Product>>>(NetworkResponse.Loading())
        private set
    var bestDealsElectronicsProducts = MutableStateFlow<NetworkResponse<List<Product>>>(NetworkResponse.Loading())
        private set

    fun getElectronicsProducts(){
        viewModelScope.launch {
            shoppingRepo.getParticularCategoryProducts(Category.Electronics.category).collect {
                electronicsProducts.emit(it)
            }
        }
        viewModelScope.launch {
            shoppingRepo.getParticularCategoryProducts(Category.Electronics.category).collect {
                if (it is NetworkResponse.Success) {
                    val electronicsBestDealsProducts = it.data.filter { product ->
                        product.category.contains(Category.BestDeals.category)
                    }
                    bestDealsElectronicsProducts.emit(NetworkResponse.Success(electronicsBestDealsProducts))
                    return@collect
                }
                bestDealsElectronicsProducts.emit(it)
            }
        }
    }

    var furnitureProducts = MutableStateFlow<NetworkResponse<List<Product>>>(NetworkResponse.Loading())
        private set
    var bestDealsFurnitureProducts = MutableStateFlow<NetworkResponse<List<Product>>>(NetworkResponse.Loading())
        private set

    fun getFurnitureProducts(){
        viewModelScope.launch {
            shoppingRepo.getParticularCategoryProducts(Category.Furniture.category).collect {
                furnitureProducts.emit(it)
            }
        }
        viewModelScope.launch {
            shoppingRepo.getParticularCategoryProducts(Category.Furniture.category).collect {
                if (it is NetworkResponse.Success) {
                    val furnitureBestDealsProducts = it.data.filter { product ->
                        product.category.contains(Category.BestDeals.category)
                    }
                    bestDealsFurnitureProducts.emit(NetworkResponse.Success(furnitureBestDealsProducts))
                    return@collect
                }
                bestDealsFurnitureProducts.emit(it)
            }
        }
    }

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

    var tableProducts = MutableStateFlow<NetworkResponse<List<Product>>>(NetworkResponse.Loading())
        private set
    var bestDealsTableProducts = MutableStateFlow<NetworkResponse<List<Product>>>(NetworkResponse.Loading())
        private set

    fun getTableProducts(){
        viewModelScope.launch {
            shoppingRepo.getParticularCategoryProducts(Category.Table.category).collect {
                tableProducts.emit(it)
            }
        }
        viewModelScope.launch {
            shoppingRepo.getParticularCategoryProducts(Category.Table.category).collect {
                if (it is NetworkResponse.Success) {
                    val tableProductsBestDealsProducts = it.data.filter { product ->
                        product.category.contains(Category.BestDeals.category)
                    }
                    bestDealsTableProducts.emit(NetworkResponse.Success(tableProductsBestDealsProducts))
                    return@collect
                }
                bestDealsTableProducts.emit(it)
            }
        }
    }


}
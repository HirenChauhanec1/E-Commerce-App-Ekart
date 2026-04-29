package com.codewithhiren.ekart.ui.shopping.viewmodel

import androidx.lifecycle.ViewModel
import com.codewithhiren.ekart.model.Address
import com.codewithhiren.ekart.repo.ShoppingRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


    @HiltViewModel
    class AddressViewmodel @Inject constructor(private val shoppingRepo: ShoppingRepo) : ViewModel(){

        fun addUserAddress(address: Address) = shoppingRepo.addUserAddress(address)

    }
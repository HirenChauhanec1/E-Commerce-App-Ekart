package com.codewithhiren.ekart.ui.shopping.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithhiren.ekart.di.DispatcherIO
import com.codewithhiren.ekart.model.User
import com.codewithhiren.ekart.repo.ShoppingRepo
import com.codewithhiren.ekart.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewmodel @Inject constructor(
    private val shoppingRepo: ShoppingRepo,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher
) : ViewModel() {

    var user = MutableStateFlow<NetworkResponse<User>>(NetworkResponse.Loading())
        private set


    init {
        viewModelScope.launch(dispatcherIO) {
            shoppingRepo.getRegisteredUserProfile().collect {
                user.emit(it)
            }
        }
    }
}
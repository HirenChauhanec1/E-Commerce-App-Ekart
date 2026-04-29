package com.codewithhiren.ekart.ui.shopping.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.codewithhiren.ekart.model.User
import com.codewithhiren.ekart.repo.AuthRepo
import com.codewithhiren.ekart.repo.ShoppingRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserAccountViewmodel @Inject constructor(
    private val shoppingRepo: ShoppingRepo,
    private val authRepo: AuthRepo
) : ViewModel(){

    fun changeUserProfile(user: User,userPic : Uri?) = shoppingRepo.changeUserProfile(user,userPic)

    fun resetOrChangePassword(email: String) = authRepo.resetPasswordUsingEmailLink(email)

}
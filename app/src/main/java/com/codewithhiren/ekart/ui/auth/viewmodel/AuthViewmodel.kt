package com.codewithhiren.ekart.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import com.codewithhiren.ekart.model.User
import com.codewithhiren.ekart.repo.AuthRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AuthViewmodel @Inject constructor(private val authRepo: AuthRepo) : ViewModel() {

    fun registerWithEmailAndPassword(user: User,password: String) = authRepo.registerWithEmailAndPassword(user,password)
    fun loginWithEmailAndPassword(email: String, password: String) = authRepo.loginWithEmailAndPassword(email,password)
    fun resetPasswordUsingEmailLink(email: String) = authRepo.resetPasswordUsingEmailLink(email)
}
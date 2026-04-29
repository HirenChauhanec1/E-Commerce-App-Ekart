package com.codewithhiren.ekart.ui.auth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.codewithhiren.ekart.databinding.FragmentRegisterBinding
import com.codewithhiren.ekart.model.User
import com.codewithhiren.ekart.ui.auth.viewmodel.AuthViewmodel
import com.codewithhiren.ekart.utils.HelperClass
import com.codewithhiren.ekart.utils.NetworkResponse
import com.codewithhiren.ekart.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val navController by lazy { findNavController() }
    private val authViewmodel: AuthViewmodel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListeners()
    }

    private fun clickListeners() {
        binding.apply {
            tvAccountLogin.setOnClickListener {
                navController.navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
            }
            btnRegister.setOnClickListener { register() }
        }
    }

    private fun register() {
        binding.apply {
            val firstName = etFirstName.editText?.text.toString().trim()
            val lastName = etLastName.editText?.text.toString().trim()
            val email = etEmail.editText?.text.toString().trim()
            val password = etPassword.editText?.text.toString().trim()

            val emailPair = HelperClass.validateEmail(email)
            val passwordPair = HelperClass.validatePassword(password)

            when {
                firstName.isEmpty() -> {
                    showToast("Enter first name")
                    etFirstName.requestFocus()
                }

                lastName.isEmpty() -> {
                    showToast("Enter last name")
                    etLastName.requestFocus()
                }

                !emailPair.first -> {
                    showToast(emailPair.second)
                    etEmail.requestFocus()
                }

                !passwordPair.first -> {
                    showToast(passwordPair.second)
                    etPassword.requestFocus()
                }

                else -> {
                    lifecycleScope.launch {
                        authViewmodel.registerWithEmailAndPassword(
                            user = User(firstName, lastName, email, ""),
                            password = password
                        )
                            .collect {
                                btnRegister.revertAnimation()
                                when (it) {
                                    is NetworkResponse.Success -> {
                                        showToast(it.data)
                                        navController.navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
                                    }

                                    is NetworkResponse.Error -> showToast(it.error)
                                    is NetworkResponse.Loading -> btnRegister.startAnimation()
                                }
                            }

                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
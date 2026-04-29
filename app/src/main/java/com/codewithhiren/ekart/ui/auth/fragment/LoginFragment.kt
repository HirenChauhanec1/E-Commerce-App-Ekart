package com.codewithhiren.ekart.ui.auth.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.codewithhiren.ekart.ui.shopping.activity.ShoppingActivity
import com.codewithhiren.ekart.databinding.FragmentLoginBinding
import com.codewithhiren.ekart.ui.auth.viewmodel.AuthViewmodel
import com.codewithhiren.ekart.utils.HelperClass
import com.codewithhiren.ekart.utils.NetworkResponse
import com.codewithhiren.ekart.utils.showToast
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val navController by lazy { findNavController() }
    private val authViewmodel: AuthViewmodel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListeners()
    }

    private fun clickListeners() {
        binding.apply {
            tvAccountRegister.setOnClickListener {
                navController.navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }
            tvForgetPassword.setOnClickListener {
                navController.navigate(LoginFragmentDirections.actionLoginFragmentToResetPasswordDialogFragment())
            }
            btnLogin.setOnClickListener { login() }
        }
    }

    private fun login() {
        binding.apply {
            val email = etEmail.editText?.text.toString().trim()
            val password = etPassword.editText?.text.toString().trim()

            val pairEmail = HelperClass.validateEmail(email)
            val pairPassword = HelperClass.validatePassword(password)

            when {
                !pairEmail.first -> {
                    showToast(pairEmail.second)
                    etEmail.requestFocus()
                }
                !pairPassword.first -> {
                    showToast(pairPassword.second)
                    etPassword.requestFocus()
                }
                else -> {
                    lifecycleScope.launch {
                        authViewmodel.loginWithEmailAndPassword(email,password).collect {
                            btnLogin.revertAnimation()
                            when (it) {
                                is NetworkResponse.Success -> {
                                    showToast(it.data)
                                    startActivity(Intent(requireActivity(), ShoppingActivity::class.java))
                                    requireActivity().finish()
                                }

                                is NetworkResponse.Error -> showToast(it.error)
                                is NetworkResponse.Loading -> btnLogin.startAnimation()
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
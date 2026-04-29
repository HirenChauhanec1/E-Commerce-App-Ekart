package com.codewithhiren.ekart.ui.auth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.codewithhiren.ekart.databinding.FragmentResetPasswordDialogBinding
import com.codewithhiren.ekart.ui.auth.viewmodel.AuthViewmodel
import com.codewithhiren.ekart.utils.HelperClass
import com.codewithhiren.ekart.utils.NetworkResponse
import com.codewithhiren.ekart.utils.showToast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch


class ResetPasswordDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentResetPasswordDialogBinding? = null
    private val binding get() = _binding!!

    private val authViewmodel: AuthViewmodel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResetPasswordDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListeners()
    }

    private fun clickListeners() {
        binding.apply {
            btnCancel.setOnClickListener { dismiss() }
            btnSend.setOnClickListener { resetPassword() }
        }
    }

    private fun resetPassword() {
        binding.apply {
            val email = etResetPassword.text.toString().trim()
            val pairEmail = HelperClass.validateEmail(email)
            when{
                !pairEmail.first -> {
                    showToast(pairEmail.second)
                    etResetPassword.requestFocus()
                }
                else -> {
                    lifecycleScope.launch {
                        authViewmodel.resetPasswordUsingEmailLink(email).collect {
                            btnSend.revertAnimation()
                            when (it) {
                                is NetworkResponse.Success -> {
                                    showToast(it.data)
                                    dismiss()
                                }
                                is NetworkResponse.Error -> showToast(it.error)
                                is NetworkResponse.Loading -> btnSend.startAnimation()
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
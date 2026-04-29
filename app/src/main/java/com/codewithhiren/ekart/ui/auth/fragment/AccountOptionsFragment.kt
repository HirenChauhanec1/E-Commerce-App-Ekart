package com.codewithhiren.ekart.ui.auth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.codewithhiren.ekart.databinding.FragmentAccountOptionsBinding


class AccountOptionsFragment : Fragment() {

    private var _binding : FragmentAccountOptionsBinding?= null
    private val binding get() = _binding!!

    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountOptionsBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListeners()
    }

    private fun clickListeners() {
        binding.apply {
            btnRegister.setOnClickListener {
                navController.navigate(AccountOptionsFragmentDirections.actionAccountOptionsFragmentToRegisterFragment())
            }
            btnLogin.setOnClickListener {
                navController.navigate(AccountOptionsFragmentDirections.actionAccountOptionsFragmentToLoginFragment())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
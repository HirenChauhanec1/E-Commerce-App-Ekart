package com.codewithhiren.ekart.ui.shopping.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.codewithhiren.ekart.R
import com.codewithhiren.ekart.databinding.FragmentAddressBinding
import com.codewithhiren.ekart.model.Address
import com.codewithhiren.ekart.ui.shopping.viewmodel.AddressViewmodel
import com.codewithhiren.ekart.utils.NetworkResponse
import com.codewithhiren.ekart.utils.hide
import com.codewithhiren.ekart.utils.hideBottomNav
import com.codewithhiren.ekart.utils.show
import com.codewithhiren.ekart.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddressFragment : Fragment() {

    private var _binding: FragmentAddressBinding? = null
    private val binding get() = _binding!!

    private val addressViewmodel : AddressViewmodel by viewModels()
    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hideBottomNav()
        _binding = FragmentAddressBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListeners()

    }

    private fun clickListeners() {
        binding.apply {
            btnCancel.setOnClickListener { navController.navigateUp() }
            btnSave.setOnClickListener { saveAddress() }
        }
    }

    private fun saveAddress() {
        binding.apply {
            val addressTitle = etAddressLocation.text.toString().trim()
            val fullName = etFullName.text.toString().trim()
            val street = etStreet.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val city = etCity.text.toString().trim()
            val state = etState.text.toString().trim()

            when {
                addressTitle.isEmpty() -> showToast("Enter address location")
                fullName.isEmpty() -> showToast("Enter fullName")
                street.isEmpty() -> showToast("Enter street")
                phone.isEmpty() -> showToast("Enter phone")
                city.isEmpty() -> showToast("Enter city")
                state.isEmpty() -> showToast("Enter state")
                else -> {
                    lifecycleScope.launch {
                        addressViewmodel.addUserAddress(
                            Address(addressTitle,fullName,street,phone,city,state)
                        )
                            .collect {
                                pb.hide()
                                when (it) {
                                    is NetworkResponse.Success -> {
                                        showToast(it.data)
                                        navController.popBackStack(R.id.billingFragment,true)
                                        navController.navigate(R.id.billingFragment)
                                    }
                                    is NetworkResponse.Error -> showToast(it.error)
                                    is NetworkResponse.Loading -> pb.show()
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
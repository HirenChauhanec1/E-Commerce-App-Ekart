package com.codewithhiren.ekart.ui.shopping.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.codewithhiren.ekart.R
import com.codewithhiren.ekart.databinding.FragmentUserAccountBinding
import com.codewithhiren.ekart.ui.shopping.viewmodel.UserAccountViewmodel
import com.codewithhiren.ekart.utils.NetworkResponse
import com.codewithhiren.ekart.utils.hideBottomNav
import com.codewithhiren.ekart.utils.showToast
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserAccountFragment : Fragment() {

    private var _binding: FragmentUserAccountBinding? = null
    private val binding get() = _binding!!

    private val args: UserAccountFragmentArgs by navArgs()
    private val userAccountViewmodel: UserAccountViewmodel by viewModels()
    private val navController by lazy { findNavController() }

    private var userPic: Uri? = null
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { it: Uri? ->
            if (it != null) {
                binding.ivProfilePic.setImageURI(it)
                userPic = it
            } else
                showToast("Image is not picked up")
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         hideBottomNav()
        _binding = FragmentUserAccountBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData()
        clickListeners()
    }

    private fun setData() {
        binding.apply {
            args.User.apply {
                if (imagePath.isNotEmpty())
                    Picasso.get().load(imagePath).into(ivProfilePic)
                etFirstName.setText(firstName)
                etLastName.setText(lastName)
            }
        }
    }

    private fun clickListeners() {
        binding.apply {
            ivEditProfilePic.setOnClickListener {
                pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            btnSave.setOnClickListener { changeUserName() }
            tvForgetChangePassword.setOnClickListener { resetOrChangePassword() }
        }
    }

    private fun changeUserName() {
        binding.apply {
            val firstName = etFirstName.text.toString().trim()
            val lastName = etLastName.text.toString().trim()
            when {
                firstName.isEmpty() -> showToast("Enter first name")
                lastName.isEmpty() -> showToast("Enter last name")
                else -> {
                    lifecycleScope.launch {
                        userAccountViewmodel.changeUserProfile(
                            user = args.User.copy(firstName = firstName, lastName = lastName),
                            userPic = userPic
                        )
                            .collect {
                                hideProgressbar()
                                when (it) {
                                    is NetworkResponse.Success -> {
                                        showToast(it.data)
                                        navController.popBackStack(R.id.profileFragment, true)
                                        navController.navigate(R.id.profileFragment)
                                    }

                                    is NetworkResponse.Error -> showToast(it.error)
                                    is NetworkResponse.Loading -> showProgressbar()
                                }
                            }
                    }
                }
            }
        }
    }

    private fun resetOrChangePassword() {
        lifecycleScope.launch {
            userAccountViewmodel.resetOrChangePassword(args.User.email).collect {
                hideProgressbar()
                when (it) {
                    is NetworkResponse.Success -> {
                        showToast(it.data)
                        navController.popBackStack(R.id.profileFragment, true)
                        navController.navigate(R.id.profileFragment)
                    }

                    is NetworkResponse.Error -> showToast(it.error)
                    is NetworkResponse.Loading -> showProgressbar()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showProgressbar() { binding.pb.isVisible = true }
    private fun hideProgressbar() { binding.pb.isVisible = false }
}
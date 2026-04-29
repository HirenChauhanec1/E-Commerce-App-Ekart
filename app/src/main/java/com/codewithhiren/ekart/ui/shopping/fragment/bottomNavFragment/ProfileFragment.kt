package com.codewithhiren.ekart.ui.shopping.fragment.bottomNavFragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.codewithhiren.ekart.R
import com.codewithhiren.ekart.ShoppingNavGraphDirections
import com.codewithhiren.ekart.databinding.FragmentProfileBinding
import com.codewithhiren.ekart.model.User
import com.codewithhiren.ekart.ui.auth.activity.AuthActivity
import com.codewithhiren.ekart.ui.shopping.viewmodel.ProfileViewmodel
import com.codewithhiren.ekart.utils.NetworkResponse
import com.codewithhiren.ekart.utils.hide
import com.codewithhiren.ekart.utils.show
import com.codewithhiren.ekart.utils.showBottomNav
import com.codewithhiren.ekart.utils.showToast
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var user = User()
    private val navController by lazy { findNavController() }
    private val profileViewmodel: ProfileViewmodel by viewModels()

    @Inject
    lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater)
        showBottomNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
        clickListeners()
    }

    private fun setObservers() {
        binding.apply {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    profileViewmodel.user.collect {
                        pb.hide()
                        when (it) {
                            is NetworkResponse.Success -> {
                                it.data.apply {

                                    user = this
                                    tvUserNameInitiate.isVisible = imagePath.isEmpty()

                                    if (imagePath.isEmpty())
                                        tvUserNameInitiate.text = getString(
                                            R.string.firstName_lastName_first_char,
                                            firstName[0].uppercase(),
                                            lastName[0].uppercase()
                                        )
                                    else
                                        Picasso.get().load(imagePath).into(ivProfilePic)

                                    tvUserName.text =
                                        getString(R.string.firstName_lastName, firstName, lastName)
                                    tvEmail.text = email
                                }
                            }

                            is NetworkResponse.Error -> showToast(it.error)
                            is NetworkResponse.Loading -> pb.show()
                        }
                    }
                }
            }
        }
    }

    private fun clickListeners() {
        binding.apply {
            llBilling.setOnClickListener {
                navController.navigate(
                    ShoppingNavGraphDirections.actionGlobalBillingFragment()
                )
            }
            constraintProfile.setOnClickListener {
                navController.navigate(
                    ProfileFragmentDirections.actionProfileFragmentToUserAccountFragment(user)
                )
            }
            llAllOrders.setOnClickListener {
                navController.navigate(ProfileFragmentDirections.actionProfileFragmentToOrdersFragment())
            }
            llLogout.setOnClickListener { signOut() }
        }
    }

    private fun signOut() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Log Out")
            setMessage("Are you want to log out ?")
            setIcon(R.drawable.baseline_logout_24)
            setPositiveButton ("Logout"){ dialog,which->
                auth.signOut()
                dialog.dismiss()
                startActivity(Intent(requireActivity(), AuthActivity::class.java))
                requireActivity().finish()

            }
            setNegativeButton ("No"){ dialog,which->
                dialog.dismiss()
            }
            show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
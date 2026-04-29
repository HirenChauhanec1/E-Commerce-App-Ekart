package com.codewithhiren.ekart.utils

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codewithhiren.ekart.R
import com.codewithhiren.ekart.ui.shopping.activity.ShoppingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


fun Fragment.hideBottomNav() {
    (activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bottom_nav).hide()
}
fun Fragment.showBottomNav() {
    (activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bottom_nav).show()
}

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(requireActivity(), message, duration).show()
}
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}

fun View.show(){ isVisible = true }
fun View.hide(){ isVisible = false }

fun <T>RecyclerView.setRecyclerViewData(
    lifeCycleOwner: LifecycleOwner,
    products: MutableStateFlow<NetworkResponse<List<T>>>,
    adapter: ListAdapter<T, *>,
    progressBar : ProgressBar
) {
    setHasFixedSize(true)
    lifeCycleOwner.apply {
        lifecycleScope.launch {
            repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                products.collect {
                    progressBar.hide()
                    when (it) {
                        is NetworkResponse.Success -> {
                            this@setRecyclerViewData.adapter = adapter
                            adapter.submitList(it.data)
                        }

                        is NetworkResponse.Error -> this@setRecyclerViewData.context.showToast(it.error)
                        is NetworkResponse.Loading -> progressBar.show()
                    }
                }
            }
        }
    }
}


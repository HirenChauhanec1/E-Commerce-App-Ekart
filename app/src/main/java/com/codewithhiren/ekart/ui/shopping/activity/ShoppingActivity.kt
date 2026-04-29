package com.codewithhiren.ekart.ui.shopping.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.codewithhiren.ekart.R
import com.codewithhiren.ekart.databinding.ActivityShoppingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {

    private val binding: ActivityShoppingBinding by lazy { ActivityShoppingBinding.inflate(layoutInflater) }
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            findViewById<View>(R.id.main).updatePadding(bottom = systemBars.bottom)
            return@setOnApplyWindowInsetsListener WindowInsetsCompat.CONSUMED
        }

        navController = (supportFragmentManager.findFragmentById(R.id.fragmentContainerViewShopping) as NavHostFragment)
                .navController

        binding.bottomNav.setupWithNavController(navController)


    }
}
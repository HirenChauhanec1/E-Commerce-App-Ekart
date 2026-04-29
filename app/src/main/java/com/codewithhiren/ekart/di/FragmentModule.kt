package com.codewithhiren.ekart.di

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.codewithhiren.ekart.ShoppingNavGraphDirections
import com.codewithhiren.ekart.model.Product
import com.codewithhiren.ekart.ui.shopping.adapter.BestDealsProductAdapter
import com.codewithhiren.ekart.ui.shopping.adapter.BestProductsAdapter
import com.codewithhiren.ekart.ui.shopping.adapter.ClickListeners
import com.codewithhiren.ekart.ui.shopping.adapter.SpecialProductAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent


@InstallIn(FragmentComponent::class)
@Module
class FragmentModule {

    @Provides
    fun getClickListeners(fragment: Fragment) : ClickListeners{
        return object : ClickListeners{
            override fun showProduct(product: Product) {
                fragment.findNavController().navigate(ShoppingNavGraphDirections.actionGlobalProductDetailsFragment4(product))
            }
        }
    }

//    @Provides
//    fun getBestProductsAdapter(clickListeners: ClickListeners) : BestProductsAdapter{
//        return BestProductsAdapter(clickListeners)
//    }
//
//    @Provides
//    fun getBestDealsProductAdapter(clickListeners: ClickListeners) : BestDealsProductAdapter{
//        return BestDealsProductAdapter(clickListeners)
//    }
//
//    @Provides
//    fun getSpecialProductAdapter(clickListeners: ClickListeners) : SpecialProductAdapter{
//        return SpecialProductAdapter(clickListeners)
//    }

//    @Provides
//    fun getMainCategoryViewmodel(fragment: Fragment) : MainCategoryViewmodel{
//        val mainCategoryViewmodel: MainCategoryViewmodel by fragment.viewModels()
//        return mainCategoryViewmodel
//    }

}
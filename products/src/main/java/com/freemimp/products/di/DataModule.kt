package com.freemimp.products.di

import com.freemimp.products.data.RetrofitProductsApi
import com.freemimp.products.domain.ProductsApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindProductApi(productsApi: RetrofitProductsApi): ProductsApi
}

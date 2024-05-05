package com.freemimp.products.di

import com.freemimp.products.data.ProductsRepositoryImpl
import com.freemimp.products.domain.repositories.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent



@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {

    @Binds
    abstract fun bindMarvelRepository(impl: ProductsRepositoryImpl): ProductRepository
}

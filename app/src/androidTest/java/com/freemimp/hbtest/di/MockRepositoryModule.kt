package com.freemimp.hbtest.di

import com.freemimp.products.di.ViewModelModule
import com.freemimp.products.domain.repositories.ProductRepository
import com.freemimp.products.domain.model.Product
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.coEvery
import io.mockk.mockk
import javax.inject.Singleton

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ViewModelModule::class]
)
@Module
object MockRepositoryModule {

    @Singleton
    @Provides
    fun provideMockRepository(): ProductRepository {
        return mockk<ProductRepository>(relaxed = true).apply {
            coEvery { this@apply.getProducts() } returns Result.success((1..20).map {
                Product(
                    sku = "$it",
                    name = "Product $it",
                    price = "$0.00",
                    description = "Description$it",
                    brand = "Brand$it",
                )
            })
        }
    }
}

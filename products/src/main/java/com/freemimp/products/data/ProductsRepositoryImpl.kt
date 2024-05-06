package com.freemimp.products.data

import com.freemimp.products.domain.model.Product
import com.freemimp.products.domain.repositories.ProductRepository
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(private val productsApi: ProductsApi) :
    ProductRepository {
    override suspend fun getProducts(): Result<List<Product>> {
        return productsApi.getProducts()
    }
}
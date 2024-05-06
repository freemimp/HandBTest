package com.freemimp.products.domain.usecases

import com.freemimp.products.domain.repositories.ProductRepository
import com.freemimp.products.domain.model.Product
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(private val repository: ProductRepository) {
    suspend fun execute(): Result<List<Product>> {
        return repository.getProducts()
    }
}
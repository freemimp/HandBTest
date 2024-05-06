package com.freemimp.products.domain.repositories

import com.freemimp.products.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(): Result<List<Product>>
}
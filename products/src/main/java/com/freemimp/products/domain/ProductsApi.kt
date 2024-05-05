package com.freemimp.products.domain

import com.freemimp.products.domain.model.Product

interface ProductsApi {
    suspend fun getProducts(): Result<List<Product>>
}
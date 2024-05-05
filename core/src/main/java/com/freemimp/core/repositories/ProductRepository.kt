package com.freemimp.core.repositories

import com.freemimp.core.model.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>
}
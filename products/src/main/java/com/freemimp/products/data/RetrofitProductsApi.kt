package com.freemimp.products.data

import com.freemimp.products.domain.model.Product
import com.freemimp.products.domain.ProductsApi
import javax.inject.Inject

class RetrofitProductsApi @Inject constructor(private val productsService: ProductsService) :
    ProductsApi {
    override suspend fun getProducts(): Result<List<Product>> {
        return try {
            val response = productsService.getProducts()
            if (response.isSuccessful) {
                val body = requireNotNull(response.body()) {
                    "Response body must not be null"
                }
                Result.success(body.map {
                    Product(
                        sku = it.sku,
                        name = it.name,
                        price = "£${"%.2f".format(it.price)}",
                        description = it.description,
                        brand = it.brand
                    )
                })
            } else {
                Result.failure(Exception(response.message() ?: "Error"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
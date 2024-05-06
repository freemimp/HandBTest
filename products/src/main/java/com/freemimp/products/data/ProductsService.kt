package com.freemimp.products.data

import com.freemimp.products.data.model.ProductsResponseItem
import retrofit2.Response
import retrofit2.http.GET

interface ProductsService {

    @GET("products")
    suspend fun getProducts(): Response<List<ProductsResponseItem>>
}

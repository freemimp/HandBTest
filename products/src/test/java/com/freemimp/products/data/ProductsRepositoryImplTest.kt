package com.freemimp.products.data

import com.freemimp.products.domain.model.Product
import com.freemimp.products.domain.repositories.ProductRepository
import com.freemimp.products.utils.TestException
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class ProductsRepositoryImplTest {
    private val productsApi: ProductsApi = mockk(relaxed = true)
    private val sut: ProductRepository = ProductsRepositoryImpl(productsApi)

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `given getProducts is executed, when api call is successful, then return Result_success with products`() {
        runTest {
            val products = listOf<Product>(mockk())
            coEvery { productsApi.getProducts() } returns Result.success(products)

            val result = sut.getProducts()
            val expected = Result.success(products)

            assertEquals(expected, result)
        }
    }

    @Test
    fun `given getProducts is executed, when api call is NOT successful, then return Result_failure with error`() {
        runTest {
            coEvery { productsApi.getProducts() } returns Result.failure(TestException)

            val result = sut.getProducts()
            val expected = Result.failure<List<Product>>(TestException)

            assertEquals(expected, result)
        }
    }
}
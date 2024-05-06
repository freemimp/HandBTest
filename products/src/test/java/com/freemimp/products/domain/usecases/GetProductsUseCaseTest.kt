package com.freemimp.products.domain.usecases

import com.freemimp.products.domain.model.Product
import com.freemimp.products.domain.repositories.ProductRepository
import com.freemimp.products.utils.TestException
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class GetProductsUseCaseTest {
    private val repository: ProductRepository = mockk(relaxed = true)

    private val sut: GetProductsUseCase = GetProductsUseCase(repository)

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `given execute is invoked, when repository call is successful, then return Result_success with products`() {
        runTest {
            val products = listOf<Product>(mockk())
            coEvery { repository.getProducts() } returns Result.success(products)

            val result = sut.execute()
            val expected = Result.success(products)

            assertEquals(expected, result)
        }
    }

    @Test
    fun `given execute is invoked, when repository call is NOT successful, then return Result_failure with error`() {
        runTest {
            coEvery { repository.getProducts() } returns Result.failure(TestException)

            val result = sut.execute()
            val expected = Result.failure<List<Product>>(TestException)

            assertEquals(expected, result)
        }
    }
}
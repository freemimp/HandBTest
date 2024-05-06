package com.freemimp.products.data

import com.freemimp.products.domain.model.Product
import com.freemimp.products.data.model.ProductsResponseItem
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerifySequence
import org.junit.jupiter.api.extension.ExtendWith
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import retrofit2.Response
import java.net.HttpURLConnection

@ExtendWith(MockKExtension::class)
class RetrofitProductsApiTest {
    private val service: ProductsService = mockk(relaxed = true)

    private val sut: ProductsApi = RetrofitProductsApi(service)

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `given getProducts is executed, when service an mapper calls are successful, then return Result_success with Products`() {
        runTest {
            val response = listOf(
                ProductsResponseItem(
                    sku = "sku",
                    name = "name",
                    price = 1.00,
                    description = "description",
                    brand = "brand"
                )
            )
            val products = listOf(
                Product(
                    sku = "sku",
                    name = "name",
                    price = "£1.00",
                    description = "description",
                    brand = "brand"
                )
            )
            coEvery { service.getProducts() } returns Response.success(
                HttpURLConnection.HTTP_OK,
                response
            )

            val result = sut.getProducts()
            val expected = Result.success(products)

            assertEquals(expected, result)
            coVerifySequence {
                service.getProducts()
            }
        }
    }

    @Test
    fun `given getProducts is executed, when service call is not successful, but mapper is, then return Result_failure with error`() {
        runTest {
            val response = mockk<ResponseBody>(relaxed = true)
            coEvery { service.getProducts() } returns Response.error(
                HttpURLConnection.HTTP_NOT_FOUND,
                response
            )

            val result = sut.getProducts()
            val expected = Result.failure<List<Product>>(Throwable("api issues"))

            assertEquals(
                expected.exceptionOrNull()?.message,
                result.exceptionOrNull()?.message
            )
            coVerifySequence {
                service.getProducts()
            }
        }
    }
}
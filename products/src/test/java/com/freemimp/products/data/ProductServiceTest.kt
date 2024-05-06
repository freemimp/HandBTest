package com.freemimp.products.data

import com.freemimp.products.utils.JsonResourseToStringMapper
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@ExtendWith(MockKExtension::class)
class ProductServiceTest {
    private lateinit var sut: ProductsService

    private lateinit var server: MockWebServer

    private val response =
        JsonResourseToStringMapper.getJsonStringFromFile("response.json")

    @BeforeEach
    fun setUp() {
        server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(response)
        )
        server.start()
        sut = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ProductsService::class.java)

    }

    @AfterEach
    fun tearDown() {
        server.shutdown()
    }

    @Nested
    @DisplayName("given getProducts is executed")
    inner class GetProducts {

        @Test
        fun `then api call is made with correct path`() {
            runBlocking {
                sut.getProducts()

                val expected = "/products"
                val actual = server.takeRequest().path

                Assertions.assertEquals(expected, actual)
            }
        }

        @Test
        fun `then api call is made with correct http method`() {
            runBlocking {
                sut.getProducts()

                val expected = "GET"
                val actual = server.takeRequest().method

                Assertions.assertEquals(expected, actual)
            }
        }

        @Test
        fun `then the response is correctly parsed`() {
            runBlocking {
                sut.getProducts()

                // This will throw no error if it was parsed successfully
                server.takeRequest()
            }
        }
    }
}
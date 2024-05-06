package com.freemimp.products.ui

import app.cash.turbine.test
import com.freemimp.products.domain.model.Product
import com.freemimp.products.domain.usecases.GetProductsUseCase
import com.freemimp.products.utils.TestCoroutineExtension
import com.freemimp.products.utils.TestException
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class, TestCoroutineExtension::class)
class ProductListViewModelTest {

    private val getProductsUseCase: GetProductsUseCase = mockk(relaxed = true)

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `given viewmodel created, when use case call is successful, then state is Success with products`() {
        runTest {
            val products = listOf(
                Product(
                    sku = "sku",
                    name = "name",
                    price = "£1.00",
                    description = "description",
                    brand = "brand"
                )
            )
            coEvery { getProductsUseCase.execute() } returns Result.success(products)

            val sut = createSut()

            sut.state.test {
                assertEquals(ProductListState.Success(products), awaitItem())
            }
        }
    }

    @Test
    fun `given viewmodel created, when use case call is NOT successful, then state is Error with error message`() {
        runTest {
            coEvery { getProductsUseCase.execute() } returns Result.failure(TestException)

            val sut = createSut()

            sut.state.test {
                assertEquals(ProductListState.Error(TestException.toString()), awaitItem())
            }
        }
    }

    @Test
    fun `given viewmodel created, when UiEvent is OnSearchTextChange with valid search query, then new state is ProductListState_Success with filtered products`() {
        runTest {
            val products = listOf(
                Product(
                    sku = "sku",
                    name = "name",
                    price = "£1.00",
                    description = "description",
                    brand = "brand"
                ),
                Product(
                    sku = "sku",
                    name = "name",
                    price = "£1.00",
                    description = "completely different",
                    brand = "brand"
                )
            )
            coEvery { getProductsUseCase.execute() } returns Result.success(products)

            val sut = createSut()

            sut.state.test {
                sut.handleUiEvent(UiEvent.OnSearchTextChange("des"))

                // State, when viewmodel is created and products successfully fetched
                assertEquals(ProductListState.Success(products), awaitItem())
                // After event was fired, new state is Success with filtered products, when search query is valid
                assertEquals(ProductListState.Success(listOf(products[0])), awaitItem())
            }
        }
    }

    @Test
    fun `given viewmodel created, when UiEvent is OnSearchTextChange with invalid search query, then new state is ProductListState_EmptySearch`() {
        runTest {
            val products = listOf(
                Product(
                    sku = "sku",
                    name = "name",
                    price = "£1.00",
                    description = "description",
                    brand = "brand"
                )
            )
            coEvery { getProductsUseCase.execute() } returns Result.success(products)

            val sut = createSut()

            sut.state.test {
                sut.handleUiEvent(UiEvent.OnSearchTextChange("abc"))

                // State, when viewmodel is created and products successfully fetched
                assertEquals(ProductListState.Success(products), awaitItem())
                // After event was fired, new state is EmptySearch, when search query is invalid
                assertEquals(ProductListState.EmptySearch, awaitItem())
            }
        }
    }

    private fun createSut(): ProductListViewModel = ProductListViewModel(getProductsUseCase)
}
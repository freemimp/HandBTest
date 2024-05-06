package com.freemimp.hbtest

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.freemimp.hbtest.utils.TestException
import com.freemimp.products.di.ViewModelModule
import com.freemimp.products.domain.repositories.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Singleton

@UninstallModules(ViewModelModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ProductListScreenErrorTest {

    @InstallIn(SingletonComponent::class)
    @Module
    object MockRepositoryModule {

        @Singleton
        @Provides
        fun provideMockRepository(): ProductRepository {
            return mockk<ProductRepository>(relaxed = true).apply {
                coEvery { this@apply.getProducts() } returns Result.failure(TestException)
            }
        }
    }

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun givenStateIsSuccessThenShowListOfProducts() {
        composeTestRule.onNodeWithTag(ERROR_TAG).assertIsDisplayed()
    }
}

private const val ERROR_TAG = "errorTag"

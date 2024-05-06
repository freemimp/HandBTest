package com.freemimp.hbtest

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.freemimp.products.di.ViewModelModule
import com.freemimp.products.domain.model.Product
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
class ProductListScreenTest {

    @InstallIn(SingletonComponent::class)
    @Module
    object MockRepositoryModule {

        @Singleton
        @Provides
        fun provideMockRepository(): ProductRepository {
            return mockk<ProductRepository>(relaxed = true).apply {
                coEvery { this@apply.getProducts() } returns Result.success((1..20).map {
                    Product(
                        sku = "$it",
                        name = "Product $it",
                        price = "$0.00",
                        description = "Description$it",
                        brand = "Brand$it",
                    )
                })
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
        (1..3).forEach {
            composeTestRule.onNodeWithTag("${LIST_ITEM_TAG}_$it").assertIsDisplayed()
        }
    }

    @Test
    fun givenProductListDisplayedWhenInputtingValidSearchQueryThenFilteredSingleItemListIsShown() {
        composeTestRule.onNodeWithTag(SEARCH_TEXT_TAG).performTextInput("11")

        composeTestRule.onNodeWithTag("${LIST_ITEM_TAG}_11").assertIsDisplayed()
    }

    @Test
    fun givenProductListDisplayedWhenInputtingValidSearchQueryThenFilteredMultipleItemListIsShown() {
        composeTestRule.onNodeWithTag(SEARCH_TEXT_TAG).performTextInput("1")

        composeTestRule.onNodeWithTag("${LIST_ITEM_TAG}_1").assertIsDisplayed()
        composeTestRule.onNodeWithTag("${LIST_ITEM_TAG}_10").assertIsDisplayed()
        composeTestRule.onNodeWithTag("${LIST_ITEM_TAG}_11").assertIsDisplayed()
    }

    @Test
    fun givenProductListDisplayedWhenInputtingInvalidSearchQueryThenEmptySearchSectionIsShown() {
        composeTestRule.onNodeWithTag(SEARCH_TEXT_TAG).performTextInput("abc")

        composeTestRule.onNodeWithTag(EMPTY_SEARCH_TAG).assertIsDisplayed()
    }
}

private const val LIST_ITEM_TAG = "listItemTag"
private const val SEARCH_TEXT_TAG = "searchTextTag"
private const val EMPTY_SEARCH_TAG = "emptySearchTag"
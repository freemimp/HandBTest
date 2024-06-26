package com.freemimp.products.ui

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.freemimp.products.R
import com.freemimp.products.domain.model.Product

@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel = viewModel(),
) {
    val state = viewModel.state.collectAsState().value
    ProductListContent(state = state, uiEvent = viewModel::handleUiEvent)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListContent(
    state: ProductListState,
    uiEvent: (UiEvent) -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            val context = LocalContext.current as Activity
            TopAppBar(title = { Text(text = stringResource(R.string.products_app_bar_title)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                navigationIcon = {
                    IconButton(onClick = {
                        context.finish()
                    }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_content_description)
                        )
                    }
                })
            if (state !is ProductListState.Error) {
                TopSearchSection(uiEvent)
            }

            when (state) {
                is ProductListState.Loading -> LoadingSection()
                is ProductListState.Error -> ErrorSection(
                    modifier = Modifier,
                    message = state.message
                )

                is ProductListState.Success -> ProductListSection(
                    products = state.products,
                    uiEvent = uiEvent
                )

                ProductListState.EmptySearch -> NotFoundSection(modifier = Modifier)
            }
        }
    }
}

@Composable
private fun TopSearchSection(uiEvent: (UiEvent) -> Unit) {
    var text by remember { mutableStateOf("") }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(SEARCH_TEXT_TAG),
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = stringResource(R.string.search_icon_content_description)
            )
        },
        value = text,
        onValueChange = {
            text = it
            uiEvent(UiEvent.OnSearchTextChange(it))
        })
}

@Composable
fun ProductListSection(products: List<Product>, uiEvent: (UiEvent) -> Unit) {
    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
        items(products) { product ->
            ProductItem(product) {
                uiEvent(UiEvent.OnProductClicked(product))
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, uiEvent: (UiEvent) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .testTag("${LIST_ITEM_TAG}_${product.sku}")
            .clickable(onClick = {
                uiEvent(UiEvent.OnProductClicked(product))
            }),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ItemSkuBrandPriceHeaderSection()
            ItemSkuBrandPriceValueSection(product)
            Spacer(modifier = Modifier.width(16.dp))
            ItemNameSection(product)
            Spacer(modifier = Modifier.width(16.dp))
            ItemDescriptionSection(product)
        }
    }
}

@Composable
private fun ItemDescriptionSection(
    product: Product
) {
    var isTextExpanded by remember { mutableStateOf(false) }
    Text(
        text = stringResource(R.string.description_label),
        style = MaterialTheme.typography.titleMedium
    )
    Text(
        text = HtmlCompat.fromHtml(product.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
            .toString(),
        maxLines = when (isTextExpanded) {
            false -> 3
            else -> Int.MAX_VALUE
        },
        style = MaterialTheme.typography.titleSmall,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.clickable {
            isTextExpanded = !isTextExpanded
        }
    )
}

@Composable
private fun ItemNameSection(product: Product) {
    Text(
        text = stringResource(R.string.name_label),
        style = MaterialTheme.typography.titleMedium
    )
    Text(
        text = product.name,
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
private fun ItemSkuBrandPriceValueSection(product: Product) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = product.sku,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = product.brand,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = product.price,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun ItemSkuBrandPriceHeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.sku_label),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = stringResource(R.string.brand_label),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = stringResource(R.string.price_label),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun LoadingSection() {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorSection(modifier: Modifier, message: String) {
    Row(
        modifier = modifier
            .padding(16.dp)
            .testTag(ERROR_TAG)
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = message)
    }
}

@Composable
fun NotFoundSection(modifier: Modifier) {
    Row(
        modifier = modifier
            .padding(16.dp)
            .testTag(EMPTY_SEARCH_TAG)
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.nothing_found_please_try_with_a_different_search_query))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val products =
        (1..20).map {
            Product(
                sku = "$it",
                name = "Product $it",
                price = "$0.00",
                description = "Description$it",
                brand = "Brand$it",
            )
        }
    ProductListSection(products = products, uiEvent = {})
}

private const val LIST_ITEM_TAG = "listItemTag"
private const val SEARCH_TEXT_TAG = "searchTextTag"
private const val EMPTY_SEARCH_TAG = "emptySearchTag"
private const val ERROR_TAG = "errorTag"

package com.freemimp.products.ui

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.freemimp.products.domain.model.Product

@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel = viewModel(),
) {
    val state = viewModel.state.collectAsState().value
    ProductListContent(state = state, uiEvent = viewModel::handleUiEvent)
}


@Composable
fun ProductListContent(
    state: ProductListState,
    uiEvent: (UiEvent) -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
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
            }
        }
    }
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
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
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
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = HtmlCompat.fromHtml(product.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    .toString(),
                style = MaterialTheme.typography.titleMedium
            )
        }
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
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = message)
    }
}

private const val LIST_ITEM_TAG = "listItemTag"

package com.freemimp.products.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freemimp.products.domain.usecases.GetProductsUseCase
import com.freemimp.products.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(private val getProductsUseCase: GetProductsUseCase) :
    ViewModel() {
    private val _state = MutableStateFlow<ProductListState>(ProductListState.Loading)
    val state: StateFlow<ProductListState> = _state

    private lateinit var cachedProducts: List<Product>

    init {
        fetchProducts()
    }


    fun handleUiEvent(event: UiEvent) {
        viewModelScope.launch {
            when (event) {
                is UiEvent.OnProductClicked -> {
                    // Navigate to details screen
                }

                is UiEvent.OnSearchTextChange -> {
                    val filteredProducts = cachedProducts.filter {
                        it.description.contains(event.searchQuery, ignoreCase = true)
                    }
                    if (filteredProducts.isEmpty()) {
                        _state.value = ProductListState.EmptySearch
                    } else {
                        _state.value = ProductListState.Success(filteredProducts)
                    }
                }
            }
        }
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            try {
                val products: List<Product> = if (::cachedProducts.isInitialized.not()) {
                    getProductsUseCase.execute().getOrThrow()
                } else {
                    cachedProducts
                }
                cachedProducts = products
                _state.value = ProductListState.Success(products)
            } catch (e: Exception) {
                _state.value = ProductListState.Error(e.toString())
            }
        }
    }
}

sealed class ProductListState {
    data object Loading : ProductListState()
    data class Success(val products: List<Product>) : ProductListState()
    data class Error(val message: String) : ProductListState()
    data object EmptySearch : ProductListState()
}

sealed interface UiEvent {
    data class OnSearchTextChange(val searchQuery: String) : UiEvent
    data class OnProductClicked(val product: Product) : UiEvent
}

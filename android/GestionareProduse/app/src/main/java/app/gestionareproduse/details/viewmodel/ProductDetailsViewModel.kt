package app.gestionareproduse.details.viewmodel

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.gestionareproduse.products.SingleProductItem
import app.gestionareproduse.products.domain.Product
import app.gestionareproduse.products.usecase.GetProductsUseCase
import app.gestionareproduse.products.viewmodel.ProductsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsDetailsViewModel @Inject constructor(
    private val useCase: GetProductsUseCase
): ViewModel() {
    fun updateProduct(product: Product) {
        useCase.updateProduct(product)
    }

    fun deleteProduct(product: Product) {
        useCase.deleteProduct(product)
    }
}
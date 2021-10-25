package app.gestionareproduse.newProduct.ViewModel

import androidx.lifecycle.ViewModel
import app.gestionareproduse.products.domain.Product
import app.gestionareproduse.products.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewProductViewModel @Inject constructor(
    private val useCase: GetProductsUseCase
): ViewModel() {
    fun saveProduct(product: Product) {
        useCase.saveProduct(product)
    }
}
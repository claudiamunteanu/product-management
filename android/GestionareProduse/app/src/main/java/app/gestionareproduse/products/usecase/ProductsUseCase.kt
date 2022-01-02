package app.gestionareproduse.products.usecase

import androidx.compose.runtime.MutableState
import app.gestionareproduse.products.domain.Product
import app.gestionareproduse.products.repo.ProductsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ProductsUseCase{
    suspend fun getAllLocalProducts(warehouseId: Long):Flow<List<Product>>
    suspend fun getAllProducts(warehouseId: Long, showError: (String) -> Unit, progressIndicatorVisibility : MutableState<Boolean>)
    suspend fun updateProduct(product: Product, showMessage: (Boolean, String) -> Unit, progressIndicatorVisibility : MutableState<Boolean>)
    suspend fun deleteProduct(productId: Long, showMessage: (Boolean, String) -> Unit, progressIndicatorVisibility : MutableState<Boolean>)
    suspend fun saveProduct(product: Product, showError: (Boolean, String) -> Unit, progressIndicatorVisibility : MutableState<Boolean>)
    suspend fun saveProductLocally(product: Product)
    suspend fun syncProducts()
}

class ProductsUseCaseImpl @Inject constructor(
    val repo: ProductsRepository
) : ProductsUseCase{

    override suspend fun getAllLocalProducts(warehouseId: Long):Flow<List<Product>> {
        return repo.getAllLocalProducts(warehouseId)
    }

    override suspend fun getAllProducts(warehouseId: Long, showError: (String) -> Unit, progressIndicatorVisibility : MutableState<Boolean>) {
        repo.loadAllProducts(warehouseId, showError, progressIndicatorVisibility)
    }

    override suspend fun updateProduct(product: Product, showMessage: (Boolean, String) -> Unit, progressIndicatorVisibility : MutableState<Boolean>) {
        repo.updateProduct(product, showMessage, progressIndicatorVisibility)
    }

    override suspend fun deleteProduct(productId: Long, showMessage: (Boolean, String) -> Unit, progressIndicatorVisibility : MutableState<Boolean>) {
        repo.deleteProduct(productId, showMessage, progressIndicatorVisibility)
    }

    override suspend fun saveProduct(product: Product, showMessage: (Boolean, String) -> Unit, progressIndicatorVisibility : MutableState<Boolean>) {
        repo.saveProduct(product, showMessage, progressIndicatorVisibility)
    }

    override suspend fun saveProductLocally(product: Product) {
        repo.saveProductLocally(product)
    }

    override suspend fun syncProducts() {
        repo.syncProducts()
    }
}

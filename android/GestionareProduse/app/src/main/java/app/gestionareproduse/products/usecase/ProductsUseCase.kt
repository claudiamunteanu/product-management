package app.gestionareproduse.products.usecase

import app.gestionareproduse.products.domain.Product
import app.gestionareproduse.products.domain.SortField
import app.gestionareproduse.products.repo.ProductsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ProductsUseCase{
    suspend fun getAllProducts(field: SortField, warehouseId: Long):Flow<List<Product>>
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(productId: Long)
    suspend fun saveProduct(product: Product)
}

class ProductsUseCaseImpl @Inject constructor(
    val repo: ProductsRepository
) : ProductsUseCase{

    override suspend fun getAllProducts(field: SortField, warehouseId: Long):Flow<List<Product>> {
        return repo.getAllProducts(field, warehouseId)
    }

    override suspend fun updateProduct(product: Product) {
        repo.updateProduct(product)
    }

    override suspend fun deleteProduct(productId: Long) {
        repo.deleteProduct(productId)
    }

    override suspend fun saveProduct(product: Product) {
        repo.saveProduct(product)
    }
}

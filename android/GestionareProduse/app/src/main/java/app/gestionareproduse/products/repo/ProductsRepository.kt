package app.gestionareproduse.products.repo

import androidx.annotation.WorkerThread
import app.gestionareproduse.products.domain.Product
import app.gestionareproduse.products.domain.SortField
import kotlinx.coroutines.flow.Flow

interface ProductsRepository{
    fun getAllProducts(field: SortField, warehouseId: Long): Flow<List<Product>>
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(productId: Long)
    suspend fun saveProduct(product: Product)
}

class ProductsRepositoryImpl(
    private val productDao: ProductDao
) : ProductsRepository{

    override fun getAllProducts(field: SortField, warehouseId: Long):Flow<List<Product>>{
        return productDao.getAllProducts(warehouseId)
    }

    // You must call this on a non-UI thread or your app will crash. So we're making this a
    // suspend function so the caller methods know this.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun saveProduct(product: Product){
        productDao.insert(product)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun updateProduct(product: Product) {
        productDao.update(product)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun deleteProduct(productId: Long) {
        productDao.delete(productId)
    }

}
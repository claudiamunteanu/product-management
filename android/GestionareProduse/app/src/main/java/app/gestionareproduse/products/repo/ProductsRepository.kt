package app.gestionareproduse.products.repo

import androidx.annotation.WorkerThread
import androidx.compose.runtime.MutableState
import app.gestionareproduse.products.domain.Product
import app.gestionareproduse.products.service.ProductService
import app.gestionareproduse.utils.logd
import app.gestionareproduse.utils.loge
import com.google.android.material.progressindicator.BaseProgressIndicator
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

interface ProductsRepository{
    fun getAllLocalProducts(warehouseId: Long): Flow<List<Product>>
    fun loadAllProducts(warehouseId: Long, showError: (String) -> Unit, progressIndicatorVisibility : MutableState<Boolean>)
    suspend fun updateProduct(product: Product, showMessage: (Boolean, String) -> Unit, progressIndicatorVisibility : MutableState<Boolean>)
    suspend fun deleteProduct(productId: Long, showMessage: (Boolean, String) -> Unit, progressIndicatorVisibility : MutableState<Boolean>)
    suspend fun saveProduct(product: Product, showMessage: (Boolean, String) -> Unit, progressIndicatorVisibility : MutableState<Boolean>)
    suspend fun saveProductLocally(product: Product)
    suspend fun syncProducts()
}

class ProductsRepositoryImpl(
    private val productDao: ProductDao,
    private val productService: ProductService
) : ProductsRepository{

    override fun getAllLocalProducts(warehouseId: Long):Flow<List<Product>>{
        return productDao.getAllProducts(warehouseId)
    }

    override fun loadAllProducts(warehouseId: Long, showError: (String) -> Unit, progressIndicatorVisibility : MutableState<Boolean>) {
        productService.getAllProducts(warehouseId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<List<Product>>() {
                override fun onCompleted() {
                    logd("Product Service completed")
                    progressIndicatorVisibility.value = false
                }

                override fun onError(e: Throwable) {
                    loge("Error while loading the products", e)
                    showError("Not able to retrieve the data. Displaying local data!")
                    progressIndicatorVisibility.value = false
                }

                override fun onNext(products: List<Product>) {
                    Thread(Runnable {
                        productDao.deleteAllProducts()
                        productDao.saveProducts(products)
                    }).start()
                    logd("Loaded products")
                }

                override fun onStart() {
                    logd("Loading products")
                    progressIndicatorVisibility.value = true
                }
            })
    }

    override suspend fun saveProduct(product: Product, showMessage: (Boolean, String) -> Unit, progressIndicatorVisibility : MutableState<Boolean>){
        productService.addProduct(product)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<Product>() {
                override fun onCompleted() {
                    logd("Product Service completed")
                    progressIndicatorVisibility.value = false
                }

                override fun onError(e: Throwable) {
                    loge("Error while persisting a product", e)
                    showMessage(false, "Not able to connect to the server, will not persist!")
                    progressIndicatorVisibility.value = false
                }

                override fun onNext(product: Product) {
                    Thread(Runnable { productDao.insert(product) }).start()
                    showMessage(true, "Product saved successfully!")
                    logd("Product persisted")
                }

                override fun onStart() {
                    logd("Saving product $product")
                    progressIndicatorVisibility.value = true
                }
            })
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun saveProductLocally(product: Product) {
        productDao.insert(product)
    }

    override suspend fun syncProducts() {
        productDao.getOfflineProducts().collect {
            productService.addProducts(it)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<List<Product>>() {
                    override fun onCompleted() {
                        logd("Product Service completed")
                    }

                    override fun onError(e: Throwable) {
                        loge("Error while syncing a products", e)
                    }

                    override fun onNext(products: List<Product>) {
                        Thread(Runnable {
                            productDao.deleteOfflineProducts()
                            productDao.saveProducts(products)
                        }).start()
                        logd("Products persisted")
                    }

                    override fun onStart() {
                        logd("Syncing products")
                    }
                })
        }
    }

    override suspend fun updateProduct(product: Product, showMessage: (Boolean, String) -> Unit, progressIndicatorVisibility : MutableState<Boolean>) {
        productService.updateProduct(product)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<Product>() {
                override fun onCompleted() {
                    logd("Product Service completed")
                    progressIndicatorVisibility.value = false
                }

                override fun onError(e: Throwable) {
                    loge("Error while updating a product", e)
                    showMessage(false, "Not able to connect to the server, will not update!")
                    progressIndicatorVisibility.value = false
                }

                override fun onNext(product: Product) {
                    Thread(Runnable { productDao.update(product) }).start()
                    showMessage(true, "Product updated successfully!")
                    logd("Product updated")
                }

                override fun onStart() {
                    logd("Updating product $product")
                    progressIndicatorVisibility.value = true
                }
            })
    }

    override suspend fun deleteProduct(productId: Long, showMessage: (Boolean, String) -> Unit, progressIndicatorVisibility : MutableState<Boolean>) {
        try{
            logd("Deleting product with id $productId")
            progressIndicatorVisibility.value = true
            val response = productService.deleteProduct(productId);
            if(response.isSuccessful) {
                Thread(Runnable { productDao.delete(productId) }).start()
                showMessage(true, "Product deleted successfully!")
                logd("Product deleted")
                logd("Product Service completed")
                progressIndicatorVisibility.value = false
            }
        } catch (exception: Exception){
            showMessage(false, "Not able to connect to the server, will not delete!")
            loge("Error while deleting a product", exception)
            progressIndicatorVisibility.value = false
        }
    }
}